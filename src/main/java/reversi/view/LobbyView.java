package reversi.view;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import reversi.model.Model;
import reversi.model.Player;
import reversi.model.network.Game;

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
  private JPanel test;
  private JButton quit;
  private JButton startGame;
  private JButton showOpenGames;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);
  private static final int FONTSIZE_HEADLINE = 40;
  private static final int FONTSIZE_HEADLINE2 = 20;
  private static final int FONTSIZE_CLIENTS = 15;

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
    /*
    setLayout(null);
    setBackground(BACKGROUND_COLOR);
    setPreferredSize(new Dimension(600, 600));

    headline = new JLabel();
    headline.setForeground(FONT_COLOR);
    headline.setText("Lobby");
    headline.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE));
    //headline.setBounds(310, 10, 200, 60);

    showOpenGames = new JButton("Show Games");
    //showOpenGames.setBounds(320, 250, 100, 25);
    setUpButton(showOpenGames);

    headline2 = new JLabel();
    headline2.setForeground(FONT_COLOR);
    headline2.setText("To start a new game choos a side ");
    headline2.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE2));
    //headline2.setBounds(220, 70, 380, 60);

    JPanel headline2Panel = new JPanel();
    headline2Panel.add(headline2);
    headline2Panel.setBackground(BACKGROUND_COLOR);
    //headline2Panel.setBounds(180, 70, 380, 60);

    quit = new JButton("Back");
    quit.setToolTipText("Go back to main menu");
    //quit.setBounds(350, 700, 50, 25);
    setUpButton(quit);
    add(quit);

    startGame = new JButton("startGame");
    startGame.setToolTipText("Start a new game.");
    //startGame.setBounds(350, 500, 50, 25);
    setUpButton(startGame);

    add(showOpenGames);
    add(startGame);
    add(headline);
    add(headline2Panel);
    */
    setLayout(new BorderLayout());


    JPanel panel = new JPanel(new FlowLayout());
    test = new JPanel(new FlowLayout());

    startGame = new JButton("start");
    startGame.setEnabled(true);
    showOpenGames = new JButton("show");
    showOpenGames.setEnabled(true);
    quit = new JButton("back");
    quit.setEnabled(true);

    panel.add(startGame);
    panel.add(quit);
    panel.add(showOpenGames);

    add(panel);
    add(test, BorderLayout.SOUTH);

    showGames();
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
  /*
    for (int i = 0; i < model.getOpenGames().size(); i++) {
      JButton joinPlayerBlack = new JButton("join as black");
      joinPlayerBlack.setEnabled(true);

      joinPlayerBlack.addActionListener(
              new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  System.out.println("Clicked join Player Black button of game");
                }
              }
      );

    JButton joinPlayerWhite = new JButton("join as white");
    joinPlayerBlack.setEnabled(true);
    joinPlayerWhite.addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked join Player White button of game");
              }
            }
    );
      System.out.println(model.getOpenGames().get(i).getHasPlayerBlack());
      System.out.println(model.getOpenGames().get(i).getHasPlayerWhite());

    if(!model.getOpenGames().get(i).getHasPlayerBlack()){
      System.out.println("test black");
      test.add(joinPlayerBlack);
      joinPlayerBlack.setEnabled(true);
    }

      if(!model.getOpenGames().get(i).getHasPlayerWhite()){
        System.out.println("test white");
        test.add(joinPlayerWhite);
        joinPlayerWhite.setEnabled(true);
      }

      add(test, BorderLayout.SOUTH);
    }
    */
  }

  private void setActionListener() {
    quit.addActionListener(
            new ActionListener() {

              @Override
              public void actionPerformed(ActionEvent event) {
                controller.stopLobby();
              }
            });

    showOpenGames.addActionListener(
            new ActionListener() {

              @Override
              public void actionPerformed(ActionEvent event) {
                showGames();
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

  private void handleStartGameClick(){
    int result =
            JOptionPane.showConfirmDialog(
                    this,
                    "Yes to start as Player black. No to start as Player White.",
                    "Start new game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

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
    }else if(result == JOptionPane.NO_OPTION){
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

    }
}
