package reversi.model.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import reversi.model.NetworkReversi;
import reversi.model.Player;

/**
 * Class responsible for setting up the lobby, updating, creating games, joining games and leaving
 * games. Sets up network components, links them to the corresponding game models and connects the
 * network components to each other.
 */
public class Lobby {

  private final NetworkReversi reversi;
  private InetAddress serverAddress;
  private Client client;

  private List<Game> openGames;

  /**
   * Creates a new Lobby.
   *
   * @param reversi game model to use
   * @param serverAddress internet address of the server. Clients will connect to this address.
   */
  public Lobby(NetworkReversi reversi, InetAddress serverAddress) {
    this.reversi = reversi;
    this.serverAddress = serverAddress;
    this.openGames = new ArrayList<>(Server.EXPECTED_GAMES);
  }

  /**
   * Starts the client to connect to a game.
   *
   * @throws IOException if server- or client-creation or a network connection fails
   */
  public void start() throws IOException {
    client = new Client(reversi, serverAddress);
    client.start();
  }

  /**
   * Stops the lobby and all involved network components.
   *
   * @throws IOException if exception occurs on closing sockets
   */
  public void stop() throws IOException {
    if (client != null) {
      client.close();
    }
  }

  /**
   * Makes the client start a new game.
   *
   * @param player type of player.
   * @throws IOException
   */
  public void startGame(Player player) throws IOException {
    client.startGame(player);
  }

  /**
   * Makes the client join an existing game.
   *
   * @param player type of player.
   * @throws IOException
   */
  public void joinGame(int gameID, Player player) throws IOException {
    client.joinGame(gameID, player);
  }

  /**
   * Makes the client start an existing game.
   *
   * @throws IOException
   */
  public void leaveGame() throws IOException {
    client.leaveGame();
  }

  /**
   * Updates other clients and server about active games.
   *
   * @param games active games.
   */
  public void updateLobby(List<Game> games) {
    this.openGames = games;
  }

  /**
   * Returns a list of all open games on the server with server address (serverAddress).
   *
   * @return list of all open games.
   */
  public List<Game> getOpenGames() {
    return openGames;
  }
}
