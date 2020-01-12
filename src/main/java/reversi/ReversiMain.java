package reversi;

import javax.swing.SwingUtilities;

import reversi.view.ReversiController;

/** Main class of the reversi application. Its only purpose is to start the application. */
public class ReversiMain {

  /**
   * Invokes the actual starting method on the <code>
   * AWT event dispatching thread</code>. This causes the method to be executed asynchronously after
   * all pending AWT events have been processed.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> showGame());
  }

  /**
   * Initializes the main {@link Controller} class of this game, which is then responsible for
   * setting the game up such that a user can further interact with it.
   */
  private static void showGame() {
    new ReversiController().start();
  }
}
