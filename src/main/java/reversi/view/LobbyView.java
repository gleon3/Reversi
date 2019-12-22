package reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Implementation of a view class to visualize a game lobby for joining and starting new network
 * games.
 */
public class LobbyView extends JPanel {

  private static final long serialVersionUID = 1L;

  private Controller controller;
  private JLabel headline;
  private JButton quit;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);
  private static final int FONTSIZE = 40;

  /**
   * Creates a view where all elements are set up to handle network gaming.
   *
   * @param controller Validates and forwards any user input.
   */
  public LobbyView(Controller controller) {
    this.controller = controller;
    createDesign();
    setActionListener();
  }

  private void createDesign() {
    setLayout(null);
    setBackground(BACKGROUND_COLOR);
    setPreferredSize(new Dimension(600, 600));
    headline = new JLabel();
    headline.setForeground(FONT_COLOR);
    headline.setText("Game Lobby");
    headline.setFont(new Font("Serif", Font.BOLD, FONTSIZE));
    headline.setBounds(140, -100, 400, 300);

    quit = new JButton("Quit");
    quit.setToolTipText("Quit the game");
    quit.setBounds(250, 510, 50, 25);
    setUpButton(quit);
    add(quit);
    add(headline);
  }

  private void setUpButton(JButton button) {
    button.setForeground(FONT_COLOR);
    button.setBackground(BACKGROUND_COLOR);
    button.setEnabled(true);
    button.setBorderPainted(false);
    button.setBorder(null);
  }

  private void setActionListener() {
    quit.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            handleQuit();
          }
        });
  }

  private void handleQuit() {
    int result =
        JOptionPane.showConfirmDialog(
            this,
            "Do you really want to quit this game?",
            "Please confirm your choice",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    // do nothing if user didn't click on the 'yes'-option
    if (result == JOptionPane.YES_OPTION) {
      controller.showStartView();
    }
  }
}
