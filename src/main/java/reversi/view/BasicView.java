package reversi.view;

import static java.util.Objects.requireNonNull;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Implementation of a BasicView to handle the other views via a cardLayout, so the views can be
 * changed by user input.
 */
public class BasicView extends JFrame implements View {

  private static final long serialVersionUID = 1L;

  private static final int MINIMUM_FRAME_HEIGHT = 600;
  private static final int MINIMUM_FRAME_WIDTH = 600;

  private static final String START_VIEW = "Start View";
  private static final String GAME_VIEW = "Game View";

  private final Controller controller;
  private final StartView startView;
  private ReversiView reversiView;
  private LobbyView lobbyView;

  private Container contentPane;
  private CardLayout cardLayout;

  BasicView(Controller controller) {
    super("Reversi");
    this.controller = requireNonNull(controller);

    setMinimumSize(new Dimension(MINIMUM_FRAME_WIDTH, MINIMUM_FRAME_HEIGHT));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    startView = new StartView(controller);

    cardLayout = new CardLayout();
    setLayout(cardLayout);

    contentPane = getContentPane();
    contentPane.add(startView, START_VIEW);
  }

  @Override
  public void showView() {
    setLocationByPlatform(true);
    setVisible(true);
  }

  @Override
  public void showStartMenu() {
    cardLayout.show(contentPane, START_VIEW);
  }

  @Override
  public void showHotseatGame() {
    reversiView = new ReversiView(controller);
    contentPane.add(reversiView, GAME_VIEW);
    cardLayout.show(getContentPane(), GAME_VIEW);
  }

  @Override
  public void showLobby() {
    lobbyView = new LobbyView(controller);
    contentPane.add(lobbyView, GAME_VIEW);
    cardLayout.show(getContentPane(), GAME_VIEW);
  }

  @Override
  public void removeGame() { // TODO Auto-generated method stub
  }

  @Override
  public void showErrorMessage(String message) { // TODO Auto-generated method stub
  }
}
