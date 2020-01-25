package reversi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import reversi.model.Model;
import reversi.model.Player;
import reversi.model.network.Game;
import reversi.model.network.Server;

/**
 * Implementation of a view class to visualize a game lobby for joining and starting new network
 * games. It offers the possibility to start a new game with a color of your choice. Or join an
 * already started game.
 */
public class LobbyView extends JPanel implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;

  private Controller controller;
  private Model model;
  private JPanel openGames;
  private JButton quit;
  private JButton startGame;
  JScrollPane scrollPane;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);
  private static final int FONTSIZE_CLIENTS = 20;
  private static final int FONTSIZE_BUTTONS = 20;

  /**
   * Creates a view where all elements are set up to handle network gaming.
   *
   * @param controller Validates and forwards any user input.
   */
  public LobbyView(Model model, Controller controller) {
    this.controller = controller;
    this.model = model;
    createDesign();
    setActionListener();
  }

  /** Method that sets up the design of the view and positions all the buttons and open games. */
  private void createDesign() {

    setLayout(new BorderLayout());

    openGames = new JPanel();
    scrollPane = new JScrollPane();
    JPanel top = new JPanel(new FlowLayout());
    top.setBackground(BACKGROUND_COLOR);
    JPanel bottom = new JPanel(new FlowLayout());
    bottom.setBackground(BACKGROUND_COLOR);

    startGame = new JButton("New Game");
    startGame.setToolTipText("Starts a new game");
    setUpButton(startGame);
    quit = new JButton("Back");
    quit.setToolTipText("Go back to main menu");
    setUpButton(quit);

    top.add(startGame);
    bottom.add(quit);

    add(top, BorderLayout.NORTH);
    add(bottom, BorderLayout.SOUTH);
  }

  private void setUpButton(JButton button) {
    button.setForeground(FONT_COLOR);
    button.setBackground(BACKGROUND_COLOR);
    button.setEnabled(true);
    button.setBorderPainted(false);
    button.setFont(new Font("Serif", Font.BOLD, FONTSIZE_BUTTONS));
  }

  /** Shows all the currently running games on the server. */
  private void showGames() {
    remove(openGames);
    remove(scrollPane);

    openGames = new JPanel();
    openGames.setBackground(BACKGROUND_COLOR);
    openGames.setLayout(new GridBagLayout());
    scrollPane =
        new JScrollPane(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.ipadx = 30;
    c.ipady = 14;
    c.insets = new Insets(25, 10, 10, 10);

    for (JPanel b : getOpenGames()) {
      openGames.add(b, c);
    }
    ;

    scrollPane.setViewportView(openGames);
    add(scrollPane);
    validate();
  }

  /** Sets up the buttons depending on which game color was chosen. */
  private List<JPanel> getOpenGames() {
    List<JPanel> buttonList = new ArrayList<>();
    System.out.println(model.getOpenGames().size());
    int gameCounter = 1;
    for (Game game : model.getOpenGames()) {
      JPanel p = new JPanel();
      p.setBackground(BACKGROUND_COLOR);

      int gameID = model.getOpenGames().indexOf(game);

      JButton button = new JButton("Join Game");
      button.setEnabled(true);

      if (game.getHasPlayerBlack()) {
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
      } else if (game.getHasPlayerWhite()) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
      } else {
        throw new AssertionError("Unhandled!");
      }

      button.addActionListener(
          new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
              try {
                if (game.getHasPlayerBlack()) {
                  controller.joinNetworkGame(gameID, Player.WHITE);
                } else if (game.getHasPlayerWhite()) {
                  controller.joinNetworkGame(gameID, Player.BLACK);
                } else {
                  throw new AssertionError("Unhandled!");
                }
              } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Creating game failed. The following error occurred: " + e.getMessage(),
                    "Error creating game",
                    JOptionPane.ERROR_MESSAGE);
              }
            }
          });

      JLabel text = new JLabel("Game " + gameCounter + ":");
      gameCounter++;
      text.setForeground(FONT_COLOR);
      text.setFont(new Font("Serif", Font.BOLD, FONTSIZE_CLIENTS));
      p.add(text);
      p.add(button);

      buttonList.add(p);
    }

    return buttonList;
  }

  private void setActionListener() {
    quit.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.stopLobby();
          }
        });

    startGame.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            handleStartGameClick();
          }
        });
  }

  private void handleStartGameClick() {
    Object[] options = new String[] {"Black", "White", "Cancel"};

    if (model.getOpenGames().size() < Server.EXPECTED_GAMES) {

      int result =
          JOptionPane.showOptionDialog(
              this,
              "Choose your color.",
              "Start new game",
              JOptionPane.YES_NO_CANCEL_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              options,
              options[2]);

      // do nothing if user didn't click on the 'yes'-option
      if (result == JOptionPane.YES_OPTION) {
        try {
          controller.startNetworkGame(Player.BLACK);
        } catch (IOException e) {
          JOptionPane.showMessageDialog(
              null,
              "Creating game failed. The following error occurred: " + e.getMessage(),
              "Error creating game",
              JOptionPane.ERROR_MESSAGE);
        }
      } else if (result == JOptionPane.NO_OPTION) {
        try {
          controller.startNetworkGame(Player.WHITE);
        } catch (IOException e) {
          JOptionPane.showMessageDialog(
              null,
              "Creating game failed. The following error occurred: " + e.getMessage(),
              "Error creating game",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      JOptionPane.showMessageDialog(
          null,
          "Maximum amount of games (="
              + Server.EXPECTED_GAMES
              + ") running on server. Maybe you can join an existing lobby.",
          "Error creating game",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent pce) {
    System.out.println("property changed");
    showGames();
  }
}
