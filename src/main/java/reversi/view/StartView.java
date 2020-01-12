package reversi.view;

import java.awt.Color;
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
    headline = new JLabel();
    headline.setForeground(FONT_COLOR);
    headline.setText("Reversi");
    headline.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE));

    JPanel headlinePanel = new JPanel();
    headlinePanel.add(headline);
    headlinePanel.setBackground(BACKGROUND_COLOR);
    headlinePanel.setBounds(280, 80, 200, 60);

    menu = new JLabel();
    menu.setForeground(FONT_COLOR);
    menu.setText("Main menu");
    menu.setFont(new Font("Serif", Font.BOLD, FONTSIZE_MENU));

    JPanel menuPanel = new JPanel();
    menuPanel.add(menu);
    menuPanel.setBackground(BACKGROUND_COLOR);
    menuPanel.setBounds(310, 170, 130, 30);

    hotseatGameButton = new JButton("Hotseat");
    hotseatGameButton.setToolTipText("Starts an hotseat game");
    hotseatGameButton.setBounds(345, 270, 60, 25);
    setUpButton(hotseatGameButton);

    singleplayerButton = new JButton("Singleplayer");
    singleplayerButton.setToolTipText("Starts an singleplayer game");
    singleplayerButton.setBounds(330, 320, 100, 25);
    setUpButton(singleplayerButton);

    networkButton = new JButton("Network");
    networkButton.setToolTipText("Go to game lobby");
    networkButton.setBounds(330, 370, 100, 25);
    setUpButton(networkButton);

    add(menuPanel);
    add(hotseatGameButton);
    add(singleplayerButton);
    add(networkButton);
    add(headlinePanel);
  }

  /** Method for setting up the buttons in a similar fashion. */
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
