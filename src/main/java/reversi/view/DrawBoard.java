package reversi.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

/**
 * Implementation of a DrawBoard class where the game board is being drawn and the colors and tokens
 * are being set.
 */
public class DrawBoard extends JPanel {

  private static final long serialVersionUID = 1L;
  private Graphics2D g2d;
  private static final int FRAME_SIZE = 600;
  private static final int BOARD_SIZE = 400;
  private static final int FIELD_SIZE = 50;
  private static final int X_COORDINATE_ABOVE_LEFT = 95;
  private static final int Y_COORDINATE_ABOVE_LEFT = 100;
  private static final int X_COORDINATE_SECOND_ROW = 145;
  private static final int Y_COORDINATE_SECOND_ROW = 150;
  private static final int X_COORDINATE_DOWNRIGHT = 445;
  private static final int Y_COORDINATE_DOWNRIGHT = 450;
  private static final Color FRAME = new Color(0, 153, 0);
  private static final Color FIELD_DARK = new Color(0, 80, 0);
  private static final Color FIELD_BRIGHT = new Color(0, 140, 0);
  private static final Color BOARD_FRAME = Color.BLACK;
  private static final int NUMBER_OF_ROWS = 8;
  private static final int NUMBER_OF_COLUMNS = 8;
  // private Controller controller;

  /**
   * Initialize a new drawBoard.
   *
   * @param controller Controller which handles the validation of the user input.
   */
  public DrawBoard(Controller controller) {
    // this.controller = controller;
  }

  @Override
  protected void paintComponent(Graphics g) {
    g2d = (Graphics2D) g;

    // Paint the area around the reversi field.
    g2d.setColor(FRAME);
    g2d.fillRect(0, 0, FRAME_SIZE, FRAME_SIZE);

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

    // According to the game field place the tokens on the reversi board.
    for (int i = 0; i < NUMBER_OF_ROWS; i++) {
      for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
        if (true) { // TODO
          drawToken();
        }
      }
    }
  }

  /** Draw a token on the current selected cell. */
  private void drawToken() {
    // TODO
  }
}
