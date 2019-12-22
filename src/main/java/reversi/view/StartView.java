package reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Implementation of a view class to visualize the main menu and handle the user input. */
public class StartView extends JPanel {

  private static final long serialVersionUID = 1L;

  private final Controller controller;
  private JLabel headline;
  private JLabel menu;
  private JButton hotseatGameButton;
  private JButton singleplayerButton;
  private JButton networkButton;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);
  private static final int FONTSIZE_HEADLINE = 50;
  private static final int FONTSIZE_MENU = 20;

  /**
   * Creates a view where all necessary elements are set up to navigate through the game.
   *
   * @param controller Validates and forwards any user input.
   */
  StartView(Controller controller) {
    this.controller = Objects.requireNonNull(controller);
    createDesign();
    setActionListener();
  }

  private void createDesign() {
    setLayout(null);
    setBackground(BACKGROUND_COLOR);
    setPreferredSize(new Dimension(600, 600));
    headline = new JLabel();
    headline.setForeground(FONT_COLOR);
    headline.setText("Reversi");
    headline.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE));
    headline.setBounds(200, -50, 300, 200);

    menu = new JLabel();
    menu.setForeground(FONT_COLOR);
    menu.setText("Main menu");
    menu.setFont(new Font("Serif", Font.BOLD, FONTSIZE_MENU));
    menu.setBounds(230, 20, 200, 200);

    hotseatGameButton = new JButton("Hotseat");
    hotseatGameButton.setToolTipText("Starts an hotseat game");
    hotseatGameButton.setBounds(240, 200, 60, 25);
    setUpButton(hotseatGameButton);

    singleplayerButton = new JButton("Singleplayer");
    singleplayerButton.setToolTipText("Starts an singleplayer game");
    singleplayerButton.setBounds(225, 250, 100, 25);
    setUpButton(singleplayerButton);

    networkButton = new JButton("Network");
    networkButton.setToolTipText("Go to game lobby");
    networkButton.setBounds(225, 300, 100, 25);
    setUpButton(networkButton);

    add(hotseatGameButton);
    add(singleplayerButton);
    add(networkButton);
    add(headline);
    add(menu);
  }

  private void setUpButton(JButton button) {
    button.setForeground(FONT_COLOR);
    button.setBackground(BACKGROUND_COLOR);
    button.setEnabled(true);
    button.setBorderPainted(false);
    button.setBorder(null);
  }

  private void setActionListener() {
    hotseatGameButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.startHotseatGame();
          }
        });

    singleplayerButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.startAiGame();
          }
        });

    networkButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            controller.startLobby();
          }
        });
  }
}
