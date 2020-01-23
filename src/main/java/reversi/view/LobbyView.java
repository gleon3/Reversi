package reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import reversi.model.Model;
import reversi.model.Player;

/**
 * Implementation of a view class to visualize a game lobby for joining and starting new network
 * games. It offers the possibility to start a new game with a color of your choice. Or join an
 * already started game.
 */
public class LobbyView extends JPanel {

  private static final long serialVersionUID = 1L;

  private Controller controller;
  private Model model;
  private JLabel headline;
  private JLabel headline2;
  private JLabel Game1;
  private JLabel Game2;
  private JLabel Game3;
  private JButton quit;
  private JButton blackButton;
  private JButton whiteButton;
  private JButton joinGameBlackPlayer;
  private JButton joinGameBlackPlayer2;
  private JButton joinGameBlackPlayer3;
  private JButton joinGameWhitePlayer;
  private JButton joinGameWhitePlayer2;
  private JButton joinGameWhitePlayer3;
  private JButton showOpenGames;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);
  private static final int FONTSIZE_HEADLINE = 40;
  private static final int FONTSIZE_HEADLINE2 = 20;
  private static final int FONTSIZE_CLIENTS = 15;
  private static final int GAME_1 = 0;
  private static final int GAME_2 = 1;
  private static final int GAME_3 = 2;
  private static final int OPEN_GAME_1 = 1;
  private static final int OPEN_GAME_2 = 2;
  private static final int OPEN_GAME_3 = 3;

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

  private void createDesign() {
    setLayout(null);
    setBackground(BACKGROUND_COLOR);
    setPreferredSize(new Dimension(600, 600));

    headline = new JLabel();
    headline.setForeground(FONT_COLOR);
    headline.setText("Lobby");
    headline.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE));
    headline.setBounds(310, 10, 200, 60);

    blackButton = new JButton("Black");
    blackButton.setBounds(270, 150, 50, 25);
    setUpButton(blackButton);

    whiteButton = new JButton("White");
    whiteButton.setBounds(420, 150, 50, 25);
    setUpButton(whiteButton);

    joinGameBlackPlayer = new JButton("Join Game");
    joinGameBlackPlayer.setBounds(370, 300, 100, 25);
    setUpButton(joinGameBlackPlayer);

    joinGameBlackPlayer2 = new JButton("Join Game");
    joinGameBlackPlayer2.setBounds(370, 350, 100, 25);
    setUpButton(joinGameBlackPlayer2);

    joinGameBlackPlayer3 = new JButton("Join Game");
    joinGameBlackPlayer3.setBounds(370, 400, 100, 25);
    setUpButton(joinGameBlackPlayer3);

    joinGameWhitePlayer = new JButton("Join Game");
    joinGameWhitePlayer.setBounds(370, 300, 100, 25);
    setUpButton(joinGameWhitePlayer);

    joinGameWhitePlayer2 = new JButton("Join Game");
    joinGameWhitePlayer2.setBounds(370, 350, 100, 25);
    setUpButton(joinGameWhitePlayer2);

    joinGameWhitePlayer3 = new JButton("Join Game");
    joinGameWhitePlayer3.setBounds(370, 400, 100, 25);
    setUpButton(joinGameWhitePlayer3);

    showOpenGames = new JButton("Show Games");
    showOpenGames.setBounds(320, 250, 100, 25);
    setUpButton(showOpenGames);

    headline2 = new JLabel();
    headline2.setForeground(FONT_COLOR);
    headline2.setText("To start a new game choos a side ");
    headline2.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE2));
    headline2.setBounds(220, 70, 380, 60);

    JPanel headline2Panel = new JPanel();
    headline2Panel.add(headline2);
    headline2Panel.setBackground(BACKGROUND_COLOR);
    headline2Panel.setBounds(180, 70, 380, 60);

    Game1 = new JLabel();
    Game1.setForeground(FONT_COLOR);
    Game1.setText("Game 1");
    Game1.setFont(new Font("Serif", Font.BOLD, FONTSIZE_CLIENTS));
    Game1.setBounds(300, 300, 100, 25);

    Game2 = new JLabel();
    Game2.setForeground(FONT_COLOR);
    Game2.setText("Game 2");
    Game2.setFont(new Font("Serif", Font.BOLD, FONTSIZE_CLIENTS));
    Game2.setBounds(300, 350, 100, 25);

    Game3 = new JLabel();
    Game3.setForeground(FONT_COLOR);
    Game3.setText("Game 3");
    Game3.setFont(new Font("Serif", Font.BOLD, FONTSIZE_CLIENTS));
    Game3.setBounds(300, 400, 100, 25);

    joinGameBlackPlayer.setVisible(false);
    joinGameBlackPlayer2.setVisible(false);
    joinGameBlackPlayer3.setVisible(false);

    joinGameWhitePlayer.setVisible(false);
    joinGameWhitePlayer2.setVisible(false);
    joinGameWhitePlayer3.setVisible(false);

    Game1.setVisible(false);
    Game2.setVisible(false);
    Game3.setVisible(false);

    quit = new JButton("Back");
    quit.setToolTipText("Go back to main menu");
    quit.setBounds(350, 700, 50, 25);
    setUpButton(quit);

    add(blackButton);
    add(whiteButton);
    add(joinGameBlackPlayer);
    add(joinGameBlackPlayer2);
    add(joinGameBlackPlayer3);
    add(joinGameWhitePlayer);
    add(joinGameWhitePlayer2);
    add(joinGameWhitePlayer3);
    add(showOpenGames);
    add(quit);
    add(headline);
    add(headline2Panel);
    add(Game1);
    add(Game2);
    add(Game3);
  }

  private void setUpButton(JButton button) {
    button.setForeground(FONT_COLOR);
    button.setBackground(BACKGROUND_COLOR);
    button.setEnabled(true);
    button.setBorderPainted(false);
    button.setBorder(null);
  }

  /** Shows the open games depending on the information of the server. */
  private void showGames() {
    if (model.getOpenGames().size() == OPEN_GAME_1
        && model.getOpenGames().get(GAME_1).getHasPlayerBlack()) {
      Game1.setVisible(true);
      joinGameBlackPlayer.setVisible(true);
    } else if (model.getOpenGames().size() == OPEN_GAME_1
        && model.getOpenGames().get(GAME_1).getHasPlayerWhite()) {
      Game1.setVisible(true);
      joinGameWhitePlayer.setVisible(true);
    } else if (model.getOpenGames().size() == OPEN_GAME_2
        && model.getOpenGames().get(GAME_2).getHasPlayerBlack()) {
      Game1.setVisible(true);
      joinGameBlackPlayer.setVisible(true);
      Game2.setVisible(true);
      joinGameBlackPlayer2.setVisible(true);
    } else if (model.getOpenGames().size() == OPEN_GAME_2
        && model.getOpenGames().get(GAME_2).getHasPlayerWhite()) {
      Game1.setVisible(true);
      joinGameWhitePlayer.setVisible(true);
      Game2.setVisible(true);
      joinGameWhitePlayer2.setVisible(true);
    } else if (model.getOpenGames().size() == OPEN_GAME_3
        && model.getOpenGames().get(GAME_3).getHasPlayerBlack()) {
      Game1.setVisible(true);
      joinGameBlackPlayer.setVisible(true);
      Game2.setVisible(true);
      joinGameBlackPlayer2.setVisible(true);
      Game3.setVisible(true);
      joinGameBlackPlayer3.setVisible(true);
    } else if (model.getOpenGames().size() == OPEN_GAME_3
        && model.getOpenGames().get(GAME_3).getHasPlayerWhite()) {
      Game1.setVisible(true);
      joinGameWhitePlayer.setVisible(true);
      Game2.setVisible(true);
      joinGameWhitePlayer2.setVisible(true);
      Game3.setVisible(true);
      joinGameWhitePlayer3.setVisible(true);
    }
  }

  private void setActionListener() {
    quit.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.showStartView();
          }
        });

    blackButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.startNetworkGame(Player.BLACK);
          }
        });

    whiteButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.startNetworkGame(Player.WHITE);
          }
        });

    joinGameBlackPlayer.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.joinNetworkGame(GAME_1, Player.WHITE);
            controller.startNetworkGame(Player.WHITE);
          }
        });

    joinGameBlackPlayer2.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.joinNetworkGame(GAME_2, Player.WHITE);
            controller.startNetworkGame(Player.WHITE);
          }
        });

    joinGameBlackPlayer3.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.joinNetworkGame(GAME_3, Player.WHITE);
            controller.startNetworkGame(Player.WHITE);
          }
        });

    joinGameWhitePlayer.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.joinNetworkGame(0, Player.BLACK);
            controller.startNetworkGame(Player.BLACK);
          }
        });

    joinGameWhitePlayer2.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.joinNetworkGame(1, Player.BLACK);
            controller.startNetworkGame(Player.BLACK);
          }
        });

    joinGameWhitePlayer3.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.joinNetworkGame(2, Player.BLACK);
            controller.startNetworkGame(Player.BLACK);
          }
        });

    showOpenGames.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            showGames();
          }
        });
  }
}
