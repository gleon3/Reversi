package reversi.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import reversi.model.Cell;
import reversi.model.Model;
import reversi.model.Player;

/**
 * A custom painting class that is responsible for drawing the complete reversi board along with all
 * necessary informations on it. This includes a proper display of each single reversi field as well
 * as the available disks in the game. Also, all possible moves for a single disk are painted, by a
 * click of a button.
 */
public class DrawBoard extends JPanel implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;
  private Graphics2D g2d;
  private static final int BOARD_SIZE = 560;
  private static final int FIELD_SIZE = 70;
  private static final int X_COORDINATE_ABOVE_LEFT = 0;
  private static final int Y_COORDINATE_ABOVE_LEFT = 0;
  private static final int X_COORDINATE_DOWNRIGHT = 490;
  private static final int Y_COORDINATE_DOWNRIGHT = 490;
  private static final Color FIELD = new Color(0, 160, 0);
  private static final Color BOARD_FRAME = Color.BLACK;
  private static final Color FIELD_FRAME = Color.BLACK;
  private static final int NUMBER_OF_ROWS = 8;
  private static final int NUMBER_OF_COLUMNS = 8;
  private static final Color POSSIBLE_MOVES_WHITE = new Color(245, 245, 220, 127);
  private static final Color POSSIBLE_MOVES_BLACK = new Color(0, 0, 0, 127);
  private static final Color DISK_COLOR_BLACK = Color.BLACK;
  private static final Color DISK_COLOR_BRIGHT = new Color(245, 245, 220);
  private ReversiController controller;
  private Set<Cell> possibleMoves;
  private Model model;

  /**
   * Creates a drawboard used to draw the reversi field. It requires a model for being able to
   * retrieve the position of the disks, and a controller for retrieving the necessary informations
   * to draw the possible moves on the board.
   *
   * @param model The model for getting the necessary informations about the disks.
   * @param controller A controller for retrieving possible move information.
   */
  public DrawBoard(Model model, Controller controller) {
    this.controller = (ReversiController) controller;
    this.model = model;
    model.addPropertyChangeListener(this);
  }

  @Override
  protected void paintComponent(Graphics g) {
    g2d = (Graphics2D) g;

    // Paint frame and surface of the reversi board.
    g2d.setColor(FIELD);
    g2d.fillRect(X_COORDINATE_ABOVE_LEFT, Y_COORDINATE_ABOVE_LEFT, BOARD_SIZE, BOARD_SIZE);
    g2d.setColor(BOARD_FRAME);
    Stroke oldStroke = g2d.getStroke();
    g2d.setStroke(new BasicStroke(4));
    g2d.drawRect(X_COORDINATE_ABOVE_LEFT, Y_COORDINATE_ABOVE_LEFT, BOARD_SIZE, BOARD_SIZE);
    g2d.setStroke(oldStroke);

    // Paint the edges of the field.
    for (int i = X_COORDINATE_ABOVE_LEFT; i <= X_COORDINATE_DOWNRIGHT; i += FIELD_SIZE) {
      for (int j = Y_COORDINATE_ABOVE_LEFT; j <= Y_COORDINATE_DOWNRIGHT; j += FIELD_SIZE) {
        {
          g2d.setColor(FIELD_FRAME);
          g2d.drawRect(i, j, FIELD_SIZE, FIELD_SIZE);
        }
      }
    }

    // According to the game field place the disks on the reversi board.
    for (int i = 0; i < NUMBER_OF_ROWS; i++) {
      for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
        if (model.getState().getField().get(new Cell(i, j)).isEmpty()) {

          // Paint the disks, according to the state of the model.
        } else if (model.getState().getField().get(new Cell(i, j)).get().getPlayer()
            == Player.WHITE) {
          g2d.setColor(DISK_COLOR_BRIGHT);
          g2d.fillOval(
              i * FIELD_SIZE + X_COORDINATE_ABOVE_LEFT,
              j * FIELD_SIZE + Y_COORDINATE_ABOVE_LEFT,
              FIELD_SIZE,
              FIELD_SIZE);
        } else if (model.getState().getField().get(new Cell(i, j)).get().getPlayer()
            == Player.BLACK) {
          g2d.setColor(DISK_COLOR_BLACK);
          g2d.fillOval(
              i * FIELD_SIZE + X_COORDINATE_ABOVE_LEFT,
              j * FIELD_SIZE + Y_COORDINATE_ABOVE_LEFT,
              FIELD_SIZE,
              FIELD_SIZE);
        }
      }
    }

    possibleMoves = controller.getPossibleMoves();

    if (possibleMoves == null) {

      // Paint the possible moves, according to the position of the disks.
    } else if (model.getState().getCurrentPlayer() == Player.WHITE) {
      for (Cell possibleMove : possibleMoves) {
        g2d.setColor(POSSIBLE_MOVES_WHITE);
        g2d.fillOval(
            possibleMove.getColumn() * FIELD_SIZE + X_COORDINATE_ABOVE_LEFT,
            possibleMove.getRow() * FIELD_SIZE + Y_COORDINATE_ABOVE_LEFT,
            FIELD_SIZE,
            FIELD_SIZE);
      }
    } else if (model.getState().getCurrentPlayer() == Player.BLACK) {
      for (Cell possibleMove : possibleMoves) {
        g2d.setColor(POSSIBLE_MOVES_BLACK);
        g2d.fillOval(
            possibleMove.getColumn() * FIELD_SIZE + X_COORDINATE_ABOVE_LEFT,
            possibleMove.getRow() * FIELD_SIZE + Y_COORDINATE_ABOVE_LEFT,
            FIELD_SIZE,
            FIELD_SIZE);
      }
    }
  }

  /** Disposes the drawboard by unsubscribing it from the model. */
  void dispose() {
    model.removePropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(
        new Runnable() {

          @Override
          public void run() {
            handlePropertyChange(event);
          }
        });
  }

  /**
   * The model has just announced that it has changed its state. The board will be repainted, so the
   * situation on the board corresponded to the state of the game.
   *
   * @param event The event that has been fired by the model.
   */
  private void handlePropertyChange(PropertyChangeEvent event) {
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      repaint();
    }
  }
}
