package reversi.view;

import java.io.IOException;
import java.net.InetAddress;

import reversi.model.Cell;
import reversi.model.Player;

/**
 * The main controller interface of the reversi game. It takes the actions from the user and handles
 * them accordingly. This is done by invoking the necessary model-methods.
 */
public interface Controller {

  /** Initializes and starts the user interface. */
  void start();

  /** Resets a game such that the game is in its initial state afterwards. */
  void resetGame();

  /** Sets the start screen up on which the user can select between different game modes. */
  void showStartView();

  /** Sets a hotseat game up that the user can afterwards play on. */
  void startHotseatGame();

  /** Sets a singleplayer game up that allows the user to play against an ai player. */
  void startAiGame();

  /** Start a client that can be connected to a server. */
  void startNetworkGame(Player player) throws IOException;

  /** Sets a Lobby to create a new network game and shows not started games. */
  void startLobby(InetAddress serverAddress);

  /** Leaves the Lobby and shows the start menu */
  void stopLobby();

  /** Method for joining an open game with an specific game id. */
  void joinNetworkGame(int gameID, Player player) throws IOException;

  /**
   * Validates the input and in case of success asks the model to execute a move on the reversi
   * board.
   */
  void move(Cell to);
}
