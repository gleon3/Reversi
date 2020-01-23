package reversi.view;

import static java.util.Objects.requireNonNull;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reversi.model.Model;

/**
 * Implementation of a BasicView to handle the other views via a cardLayout, so the views can be
 * changed by user input.
 */
public class BasicView extends JFrame implements View {

  private static final long serialVersionUID = 1L;

  private static final int MINIMUM_FRAME_HEIGHT = 800;
  private static final int MINIMUM_FRAME_WIDTH = 800;

  private static final String START_VIEW = "Start View";
  private static final String GAME_VIEW = "Game View";

  private final Controller controller;
  private final StartView startView;
  private ReversiView reversiView;
  private LobbyView lobbyView;

  private Container contentPane;
  private CardLayout cardLayout;

  /** Creates a basic view that handles the other views and sets a start view for the start. */
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
    setResizable(false);
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
  public void showGame(Model model) {
    reversiView = new ReversiView(model, controller);
    contentPane.add(reversiView, GAME_VIEW);
    cardLayout.show(getContentPane(), GAME_VIEW);
  }

  @Override
  public void showLobby(Model model) {
    lobbyView = new LobbyView(model, controller);
    contentPane.add(lobbyView, GAME_VIEW);
    cardLayout.show(getContentPane(), GAME_VIEW);
  }

  @Override
  public void removeGame() {
    if (reversiView != null) {
      contentPane.remove(reversiView);
      reversiView.dispose();
      reversiView = null;
    }
  }

  @Override
  public void showErrorMessage(String message) {
    if (reversiView != null) {
      reversiView.showErrorMessage(message);
    } else {
      JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
