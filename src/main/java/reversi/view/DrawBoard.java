package reversi.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
  private static final int X_COORDINATE_SECOND_ROW = 70;
  private static final int Y_COORDINATE_SECOND_ROW = 70;
  private static final int X_COORDINATE_DOWNRIGHT = 490;
  private static final int Y_COORDINATE_DOWNRIGHT = 490;
  private static final Color FIELD_DARK = new Color(0, 80, 0);
  private static final Color FIELD_BRIGHT = new Color(0, 140, 0);
  private static final Color BOARD_FRAME = Color.BLACK;
  private static final int NUMBER_OF_ROWS = 8;
  private static final int NUMBER_OF_COLUMNS = 8;
  private static final Color POSSIBLE_MOVES_WHITE = new Color(245, 245, 220);
  private static final Color POSSIBLE_MOVES_BLACK = Color.BLACK;
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
    configureActionListener();
  }

  @Override
  protected void paintComponent(Graphics g) {
    g2d = (Graphics2D) g;

    // Paint the dark reversi fields.
    for (int i = X_COORDINATE_ABOVE_LEFT; i <= X_COORDINATE_DOWNRIGHT; i += FIELD_SIZE) {
      for (int j = Y_COORDINATE_ABOVE_LEFT; j <= Y_COORDINATE_DOWNRIGHT; j += FIELD_SIZE) {
        {
          g2d.setColor(FIELD_DARK);
          g2d.fillRect(i, j, FIELD_SIZE, FIELD_SIZE);
        }
      }
    }

    // Paint the bright reversi fields row 1, 3, 5, 7.
    for (int i = X_COORDINATE_ABOVE_LEFT; i <= X_COORDINATE_DOWNRIGHT; i += FIELD_SIZE * 2) {
      for (int j = Y_COORDINATE_ABOVE_LEFT; j <= Y_COORDINATE_DOWNRIGHT; j += FIELD_SIZE * 2) {
        {
          g2d.setColor(FIELD_BRIGHT);
          g2d.fillRect(i, j, FIELD_SIZE, FIELD_SIZE);
        }
      }
    }
    // Paint the bright reversi fields row 2, 4, 6, 8.
    for (int i = X_COORDINATE_SECOND_ROW; i <= X_COORDINATE_DOWNRIGHT; i += FIELD_SIZE * 2) {
      for (int j = Y_COORDINATE_SECOND_ROW; j <= Y_COORDINATE_DOWNRIGHT; j += FIELD_SIZE * 2) {
        {
          g2d.setColor(FIELD_BRIGHT);
          g2d.fillRect(i, j, FIELD_SIZE, FIELD_SIZE);
        }
      }
    }

    // Frame around the reversi board.
    g2d.setColor(BOARD_FRAME);
    Stroke oldStroke = g2d.getStroke();
    g2d.setStroke(new BasicStroke(4));
    g2d.drawRect(X_COORDINATE_ABOVE_LEFT, Y_COORDINATE_ABOVE_LEFT, BOARD_SIZE, BOARD_SIZE);
    g2d.setStroke(oldStroke);

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

    possibleMoves = controller.possibleMoves;

    if (possibleMoves == null) {

      // Paint the possible moves, according to the position of the disks.
    } else if (model.getState().getCurrentPlayer() == Player.WHITE) {
      for (Cell possibleMove : possibleMoves) {
        g2d.setColor(POSSIBLE_MOVES_WHITE);
        Stroke oldStroke1 = g2d.getStroke();
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(
            possibleMove.getColumn() * FIELD_SIZE + X_COORDINATE_ABOVE_LEFT,
            possibleMove.getRow() * FIELD_SIZE + Y_COORDINATE_ABOVE_LEFT,
            FIELD_SIZE,
            FIELD_SIZE);
        g2d.setStroke(oldStroke1);
      }
    } else if (model.getState().getCurrentPlayer() == Player.BLACK) {
      for (Cell possibleMove : possibleMoves) {
        g2d.setColor(POSSIBLE_MOVES_BLACK);
        Stroke oldStroke1 = g2d.getStroke();
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(
            possibleMove.getColumn() * FIELD_SIZE + X_COORDINATE_ABOVE_LEFT,
            possibleMove.getRow() * FIELD_SIZE + Y_COORDINATE_ABOVE_LEFT,
            FIELD_SIZE,
            FIELD_SIZE);
        g2d.setStroke(oldStroke1);
      }
    }
  }

  /** Disposes the drawboard by unsubscribing it from the model. */
  void dispose() {
    model.removePropertyChangeListener(this);
  }

  private void configureActionListener() {
    addComponentListener(
        new ComponentAdapter() {

          @Override
          public void componentResized(ComponentEvent e) {
            repaint();
          }
        });
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
    repaint();
  }
}
