package reversi.model.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import reversi.model.GameState;

public class Server implements NetworkModule {

  public static final int EXPECTED_GAMES = 10;
  private static final int EXPECTED_CONNECTIONS = EXPECTED_GAMES * 2;
  private List<Socket> clientSockets;
  private List<ObjectOutputStream> clientOutputStreams;

  private List<Game> runningGames;

  private ServerSocket socket;

  private Gson gson;

  Server() {
    clientSockets = new ArrayList<>(EXPECTED_CONNECTIONS);
    clientOutputStreams = new ArrayList<>(EXPECTED_CONNECTIONS);
    runningGames = new ArrayList<>(EXPECTED_GAMES);
    gson = new Gson();
  }

  /**
   * Starts the server.
   *
   * @throws IOException if a server socket is already bound to {@link NetworkModule#REVERSI_PORT}
   */
  public void start() throws IOException {
    socket = new ServerSocket(NetworkModule.REVERSI_PORT);

    acceptConnections(socket);
  }

  /**
   * Starts accepting connections. Close socket after 100 connections are accepted.
   *
   * @param socket that is accepting the clientsockets.
   */
  private void acceptConnections(ServerSocket socket) {
    try {
      while (clientSockets.size() < EXPECTED_CONNECTIONS) {
        Socket newConnection = socket.accept();
        if (newConnection != null) {
          clientSockets.add(newConnection);
          System.out.println(
              "Server accepted connection. Now connected are " + clientSockets.size() + " clients");
          ObjectOutputStream oos = new ObjectOutputStream(newConnection.getOutputStream());
          clientOutputStreams.add(oos);

          // send updated List of games to all clients (so every client "knows" current gamelobby)
          sendToAllClients(getRunningGamesJson());

          Thread receivingThread = new Thread(() -> receiveStates(newConnection, oos));
          receivingThread.setDaemon(true);
          receivingThread.start();
        }
      }
      // close socket after EXPECTED_GAMES connections are accepted so that
      // further connections are rejected.
      // If we just stop to read from the socket, further connections are
      // just ignored and new clients will not know that the server is unavailable
      socket.close();
    } catch (IOException e) {
      // may happen if socket is closed pre-maturely and is thus expected
      e.printStackTrace();
    }
  }

  /**
   * Receive Objects from the clients. Depending on the message type, the server will either add the
   * client to a game, receive and send out game states or end a game.
   */
  private void receiveStates(Socket from, ObjectOutputStream messagesToClient) {
    try (InputStream fromClient = from.getInputStream(); ) {
      ObjectInputStream messagesFromClient = new ObjectInputStream(fromClient);
      Object received = messagesFromClient.readObject();
      while (received != null) {
        JsonObject receivedJson =
            gson.fromJson((String) received, JsonElement.class).getAsJsonObject();
        System.out.println("Server received message: " + receivedJson);

        // check which type of message receivedJson is and act accordingly
        String messageType = receivedJson.get("message-type").getAsString();

        switch (messageType) {
          case "GameState":
            JsonElement stateJson = receivedJson.get("GameState");
            GameState receivedState = gson.fromJson(stateJson, GameState.class);

            if (getGameOf(messagesToClient).isPresent()) {
              String toSend = gson.toJson(receivedJson);
              Game toSendTo = getGameOf(messagesToClient).get();
              if (validateGameState(receivedState, toSendTo)) {
                sendToGame(toSendTo, toSend);
              }
            } else {
              throw new AssertionError("Trying to send state to game that doesn't exist");
            }

            break;

          case "StartGame":
            String playerToStart = receivedJson.get("player").getAsString();

            // add ObjectOutputStream of client that sent message to new game
            switch (playerToStart) {
              case "Black":
                runningGames.add(new Game(messagesToClient, null));
                break;
              case "White":
                runningGames.add(new Game(null, messagesToClient));
                break;
              default:
                throw new AssertionError("Unhandled player: " + playerToStart);
            }

            // send updated List of games to all clients (so every client "knows" current
            // gamelobby)
            sendToAllClients(getRunningGamesJson());
            break;

          case "JoinGame":
            String playerToJoin = receivedJson.get("player").getAsString();
            int gameIdToJoin = receivedJson.get("gameID").getAsInt();

            // add ObjectOutputStream of client that sent message to existing game with id given in
            // received message
            switch (playerToJoin) {
              case "Black":
                if (runningGames.get(gameIdToJoin).getPlayerBlack() == null) {
                  runningGames.get(gameIdToJoin).setPlayerBlack(messagesToClient);
                } else {
                  throw new AssertionError(
                      "trying to join position which already has a player in it");
                }
                break;
              case "White":
                if (runningGames.get(gameIdToJoin).getPlayerWhite() == null) {
                  runningGames.get(gameIdToJoin).setPlayerWhite(messagesToClient);
                } else {
                  throw new AssertionError(
                      "trying to join position which already has a player in it");
                }
                break;
              default:
                throw new AssertionError("Unhandled player: " + playerToJoin);
            }

            // send to both clients in game that game should start
            JsonObject toSendJson = new JsonObject();
            toSendJson.addProperty("message-type", "StartGame");
            String toSend = gson.toJson(toSendJson);
            sendToGame(runningGames.get(gameIdToJoin), toSend);

            // send updated List of games to all clients (so every client "knows" current
            // gamelobby)
            sendToAllClients(getRunningGamesJson());
            break;
          case "EndGame":
            if (getGameOf(messagesToClient).isPresent()) {
              Game game = getGameOf(messagesToClient).get();

              // send endgame message to other client in the game, so both clients "know" that
              // game ended and remove the game of runningGames
              JsonObject endGameJson = new JsonObject();
              endGameJson.addProperty("message-type", "EndGame");
              String endGameJsonString = gson.toJson(endGameJson);
              sendToGame(game, endGameJsonString);

              runningGames.remove(game);

              // send updated List of games to all clients (so every client "knows" current
              // gamelobby)
              sendToAllClients(getRunningGamesJson());
            } else {
              throw new AssertionError("Trying to leave game that doesn't exist");
            }
            break;
          default:
            throw new AssertionError("Server received unhandled message!");
        }
        received = messagesFromClient.readObject();
      }
    } catch (IOException e) {
      // client connection broke off
      System.out.println("client disconnected");
      clientOutputStreams.remove(messagesToClient);
      clientSockets.remove(from);

      if (getGameOf(messagesToClient).isPresent()) {
        Game game = getGameOf(messagesToClient).get();

        // remove the ObjectOutputStream from the game, as it just stated disconnection
        if (game.getPlayerBlack() != null && game.getPlayerBlack().equals(messagesToClient)) {
          game.setPlayerBlack(null);
        }
        if (game.getPlayerWhite() != null && game.getPlayerWhite().equals(messagesToClient)) {
          game.setPlayerWhite(null);
        }

        // send endgame message to other client in the game, so both clients "know" that
        // game ended
        JsonObject endGameJson = new JsonObject();
        endGameJson.addProperty("message-type", "EndGame");
        String endGameJsonString = gson.toJson(endGameJson);
        sendToGame(game, endGameJsonString);

        runningGames.remove(game);

        // send updated List of games to all clients (so every client "knows" current
        // gamelobby)
        sendToAllClients(getRunningGamesJson());
      }
    } catch (ClassNotFoundException e) {
      throw new AssertionError();
    }
  }

  private boolean validateGameState(GameState toSend, Game toSendTo) {
    System.out.println(toSendTo.getLastStepSent());
    if (toSend.getMoveCounter() == 0) {
      toSendTo.setLastStepSent(-1);
      return true;
    }

    if (toSend.getMoveCounter() <= toSendTo.getLastStepSent()) {
      return false;
    }
    toSendTo.setLastStepSent(toSend.getMoveCounter());

    return true;
  }

  private String getRunningGamesJson() {
    JsonElement runningGamesJson = gson.toJsonTree(runningGames).getAsJsonArray();
    JsonObject runningGamesJsonMessage = new JsonObject();
    runningGamesJsonMessage.addProperty("message-type", "UpdateLobby");
    runningGamesJsonMessage.add("open-games", runningGamesJson);

    return gson.toJson(runningGamesJsonMessage);
  }

  private Optional<Game> getGameOf(ObjectOutputStream oos) {
    for (Game game : runningGames) {
      if (game.clientInGame(oos)) {
        return Optional.of(game);
      }
    }
    return Optional.empty();
  }

  private synchronized void sendToAllClients(String message) {
    for (ObjectOutputStream toClient : clientOutputStreams) {
      try {
        toClient.writeObject(message);
        System.out.println("Server sent client: " + message);
      } catch (IOException e) {
        cleanUpConnection();
      }
    }
  }

  private synchronized void sendToGame(Game game, String message) {
    for (ObjectOutputStream toClient : game.getBothClients()) {
      try {
        if (toClient != null) {
          toClient.writeObject(message);
          System.out.println("Server sent to specific game's clients: " + message);
        }
      } catch (IOException e) {
        cleanUpConnection();
      }
    }
  }

  @Override
  public boolean isRunning() {
    return socket != null && !socket.isClosed();
  }

  private void cleanUpConnection() {
    System.out.println("Connection will be cleaned up.");
    try {
      close();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public void close() throws IOException {
    for (Socket clientSocket : clientSockets) {
      if (!clientSocket.isClosed()) {
        clientSocket.close();
      }
    }
    if (socket != null && !socket.isClosed()) {
      socket.close();
    }
  }

  /**
   * Main method of the server.
   * @param args The command line arguments.
   * @throws IOException if any IOException occurs while starting the server.
   */
  public static void main(String[] args) throws IOException {
    System.out.println("Server is online.");
    Server s = new Server();
    s.start();
  }
}
