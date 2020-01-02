package reversi.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** Implementation of a controller class that handles and validates the user input. */
public class ReversiController implements Controller, MouseListener {

  private View view;
  // private static final int DIFFERENCE_X = 97;
  // private static final int DIFFERENCE_Y = 101;
  // private static final int FIELD_SIZE = 50;
  // private int mouseY;
  // private int mouseX;
  // ArrayList<Integer> possibleMoves;
  // private Model model

  public ReversiController() {
    view = new BasicView(this);
  }

  public void start() {
    view.showStartMenu();
    view.showView();
  }

  @Override
  public void setView(View view) { // TODO Auto-generated method stub
  }

  @Override
  public void resetGame() { // TODO Auto-generated method stub
  }

  @Override
  public void showStartView() {
    view.showStartMenu();
  }

  @Override
  public void startHotseatGame() {
    // model = new Reversi();
    view.showHotseatGame();
  }

  @Override
  public void startAiGame() {
    // model = new AiReversi();
    view.showHotseatGame();
  }

  @Override
  public void startLobby() {
    view.showLobby();
  }

  @Override
  public void startClient() { // TODO Auto-generated method stub
  }

  @Override
  public void startServer() { // TODO Auto-generated method stub
  }

  @Override
  public boolean move() { // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void dispose() { // TODO Auto-generated method stub
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // Calculate the coordinates on the reversi board.
    /* if ((e.getX() - DIFFERENCE_X) > 0 && (e.getY() - DIFFERENCE_Y) > 0) {
      mouseX = ((e.getX() - DIFFERENCE_X) / FIELD_SIZE);
      mouseY = ((e.getY() - DIFFERENCE_Y) / FIELD_SIZE);
    }*/
    // TODO
  }

  @Override
  public void mousePressed(MouseEvent e) { // TODO Auto-generated method stub
  }

  @Override
  public void mouseReleased(MouseEvent e) { // TODO Auto-generated method stub
  }

  @Override
  public void mouseEntered(MouseEvent e) { // TODO Auto-generated method stub
  }

  @Override
  public void mouseExited(MouseEvent e) { // TODO Auto-generated method stub
  }
}
