package reversi.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

import javax.swing.JOptionPane;

import reversi.model.AiReversi;
import reversi.model.Cell;
import reversi.model.Model;
import reversi.model.Reversi;

/** Implementation of a controller class that handles and validates the user input. */
public class ReversiController extends MouseAdapter implements Controller {

  private View view;
  private static final int DIFFERENCE_X = 110;
  private static final int DIFFERENCE_Y = 100;
  private static final int FIELD_SIZE = 70;
  private static final int FIRST_ROW = 0;
  private static final int LAST_ROW = 7;
  private int mouseY;
  private int mouseX;
  private Cell to;
  private Model model;
  Set<Cell> possibleMoves;

  /** Creates a controller object for a given model. */
  public ReversiController() {
    view = new BasicView(this);
  }

  public void start() {
    view.showStartMenu();
    view.showView();
  }

  @Override
  public void resetGame() {
    try {
      model.newGame();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void showStartView() {
    view.removeGame();
    leaveCurrentGame();
    view.showStartMenu();
  }

  private void leaveCurrentGame() {
    if (model != null) {
      model = null;
    }
  }

  @Override
  public void startHotseatGame() {
    model = new Reversi();
    view.showGame(model);
  }

  @Override
  public void startAiGame() {
    model = new AiReversi();
    view.showGame(model);
  }

  @Override
  public void startLobby() {
    view.showLobby();
  }

  @Override
  public void startNetworkGame(InetAddress serverAddress) {
    // model = new NetworkReversi(serverAddress);
    try {
      model.newGame();
      view.showGame(model);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          "Creating network game failed. The following error occurred: " + e.getMessage(),
          "Error creating network game",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void move(Cell to) {
    model.move(to);
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    // Calculate the coordinates on the reversi board.
    if ((e.getX() - DIFFERENCE_X) > 0 && (e.getY() - DIFFERENCE_Y) > 0) {
      mouseX = ((e.getX() - DIFFERENCE_X) / FIELD_SIZE);
      mouseY = ((e.getY() - DIFFERENCE_Y) / FIELD_SIZE);
    }

    // Check if click is not on the field and a player has not been clicked.
    if (mouseX > LAST_ROW || mouseY > LAST_ROW || mouseX < FIRST_ROW || mouseY < FIRST_ROW) {

      // Check which field the user has clicked.
    } else if (model.getState().getField().get(new Cell(mouseX, mouseY)).isEmpty()) {
      to = new Cell(mouseX, mouseY);
      if (possibleMoves != null) {
        possibleMoves.clear();
      }
      move(to);
    }
  }
}
