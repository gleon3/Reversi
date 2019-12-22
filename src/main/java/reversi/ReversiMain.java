package reversi;

import javax.swing.SwingUtilities;

import reversi.view.ReversiController;

public class ReversiMain {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> showGame());
  }

  private static void showGame() {
    new ReversiController().start();
  }
}
