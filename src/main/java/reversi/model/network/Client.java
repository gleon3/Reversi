package reversi.model.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import reversi.model.GameState;
import reversi.model.NetworkReversi;
import reversi.model.Player;
import reversi.model.Reversi;

class Client implements NetworkModule, PropertyChangeListener {

  private Socket clientSocket;
  private ObjectOutputStream toServer;

  private NetworkReversi reversi;
  private InetAddress serverAddress;

  Client(NetworkReversi reversi, InetAddress serverAddress) {
    this.reversi = reversi;
    this.serverAddress = serverAddress;
    reversi.addPropertyChangeListener(this);
  }

  /**
   * Starts the client and connects to the server at the address given for this object's
   * construction.
   *
   * @throws IOException if no server is available at the given server address and port {@link
   *     NetworkModule#REVERSI_PORT}
   */
  @Override
  public void start() throws IOException {
    clientSocket = new Socket(serverAddress, NetworkModule.REVERSI_PORT);
    toServer = new ObjectOutputStream(clientSocket.getOutputStream());
    Thread clientThread =
        new Thread(
            () -> {
              try (ObjectInputStream fromServer =
                  new ObjectInputStream(clientSocket.getInputStream())) {
                Object received = fromServer.readObject();

                while (received != null) {
                  Gson gson = new Gson();
                  JsonObject receivedJson =
                      gson.fromJson((String) received, JsonElement.class).getAsJsonObject();

                  // check which type of message receivedJson is
                  String messageType = receivedJson.get("message-type").getAsString();

                  System.out.println(
                      "Client received message from server: "
                          + receivedJson
                          + " with message-type: "
                          + messageType);

                  switch (messageType) {
                    case "GameState":
                      JsonElement stateJson = receivedJson.get("GameState");
                      GameState receivedState = gson.fromJson(stateJson, GameState.class);

                      reversi.setState(receivedState);
                      break;
                    case "StartGame":
                      reversi.startPhase();
                      break;
                    case "EndGame":
                      reversi.endGame();
                      break;
                    case "UpdateLobby":
                      JsonElement arraylistAsJson = receivedJson.get("open-games").getAsJsonArray();

                      Type openGamesType = new TypeToken<List<Game>>() {}.getType();
                      List<Game> openGames = gson.fromJson(arraylistAsJson, openGamesType);

                      reversi.updateLobby(openGames);
                      break;
                    default:
                      throw new AssertionError(
                          "Server received unhandled message of type " + messageType);
                  }

                  received = fromServer.readObject();
                }
              } catch (IOException e) {
                cleanUpConnection();
              } catch (ClassNotFoundException e) {
                throw new AssertionError(e);
              }
            });
    clientThread.setDaemon(true);
    clientThread.start();
  }

  /**
   * Client sends message with message-type StartGame to the server.
   *
   * @param player type of player.
   * @throws IOException if not able to write to server
   */
  void startGame(Player player) throws IOException {
    if (toServer != null) {
      toServer.reset();

      Gson gson = new Gson();
      JsonObject messageJson = new JsonObject();
      messageJson.addProperty("message-type", "StartGame");
      messageJson.addProperty("player", player.toString());

      String toSend = gson.toJson(messageJson);

      System.out.println("Client sent startgame message: " + toSend);
      toServer.writeObject(toSend);
    }
  }

  /**
   * Client sends message with message-type JoinGame to the server.
   *
   * @param gameID ID number of the game.
   * @param player type of player.
   * @throws IOException if not able to write to server
   */
  void joinGame(int gameID, Player player) throws IOException {
    if (toServer != null) {
      toServer.reset();

      Gson gson = new Gson();
      JsonObject messageJson = new JsonObject();
      messageJson.addProperty("message-type", "JoinGame");
      messageJson.addProperty("gameID", gameID);
      messageJson.addProperty("player", player.toString());

      String toSend = gson.toJson(messageJson);

      System.out.println("Client sent joingame message: " + toSend);
      toServer.writeObject(toSend);
    }
  }

  /**
   * Client sends message with message-type EndGame to the server.
   *
   * @throws IOException if not able to write to server
   */
  void leaveGame() throws IOException {
    if (toServer != null) {
      toServer.reset();

      Gson gson = new Gson();
      JsonObject messageJson = new JsonObject();
      messageJson.addProperty("message-type", "EndGame");

      String toSend = gson.toJson(messageJson);

      System.out.println("Client sent endgame message: " + toSend);
      toServer.writeObject(toSend);
    }
  }

  @Override
  public boolean isRunning() {
    return clientSocket != null && !clientSocket.isClosed();
  }

  @Override
  public void propertyChange(PropertyChangeEvent e) {

    if (e.getPropertyName().equals(Reversi.NEW_MOVE)) {
      if (toServer != null) {
        try {
          System.out.println("New move");
          toServer.reset();
          Gson gson = new Gson();

          JsonElement stateJson = gson.toJsonTree(reversi.getState());

          JsonObject messageJson = new JsonObject();
          messageJson.addProperty("message-type", "GameState");
          messageJson.add("GameState", stateJson);

          String toSend = gson.toJson(messageJson);

          System.out.println("Client sent gamestate message: " + toSend);
          toServer.writeObject(toSend);
        } catch (IOException ex) {
          cleanUpConnection();
        }
      }
    }
  }

  private void cleanUpConnection() {
    try {
      close();
      clientSocket = null;
      toServer = null;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public void close() throws IOException {
    if (clientSocket != null) {
      clientSocket.close();
    }
  }
}
