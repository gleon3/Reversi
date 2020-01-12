package reversi.view;

import java.net.InetAddress;

import reversi.model.Cell;

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
  void startNetworkGame(InetAddress serverAddress);

  /** Sets a Lobby to create a new network game and shows not started games. */
  void startLobby();

  /**
   * Validates the input and in case of success asks the model to execute a move on the reversi
   * board.
   */
  void move(Cell to);
}
