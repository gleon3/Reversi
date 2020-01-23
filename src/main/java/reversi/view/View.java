package reversi.view;

import reversi.model.Model;

/** The main interface of the view. It gets the state it displays directly from the model. */
public interface View {

  /** Show the graphical user interface of the reversi game. */
  void showView();

  /** Shows the start menu to the user. */
  void showStartMenu();

  /** Shows the hotseat game to the user. */
  void showGame(Model model);

  /** Shows the Lobby to the user. */
  void showLobby(Model model);

  /** Removes a game-view if there is currently shown one. */
  void removeGame();

  /**
   * Displays an error message to the user.
   *
   * @param message The message to be displayed
   */
  void showErrorMessage(String message);
}
