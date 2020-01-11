package reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Implementation of a view class to visualize a game lobby for joining and starting new network
 * games.
 */
public class LobbyView extends JPanel {

  private static final long serialVersionUID = 1L;

  private Controller controller;
  private JLabel headline;
  private JLabel headline2;
  private JLabel serverAddressLabel;
  private JButton quit;
  private JButton networkGameButton;
  private JTextField serverAddress;
  private InetAddress address;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);
  private static final int FONTSIZE_HEADLINE = 40;
  private static final int FONTSIZE_HEADLINE2 = 20;

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
    headline.setText("Lobby");
    headline.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE));
    headline.setBounds(310, 10, 200, 60);

    serverAddressLabel = new JLabel("Server address: ");
    serverAddressLabel.setForeground(FONT_COLOR);
    serverAddressLabel.setBounds(205, 90, 150, 50);

    serverAddress = new JTextField("127.0.0.1", 20);
    serverAddress.setBounds(315, 105, 100, 20);

    networkGameButton = new JButton("Start network game");
    networkGameButton.setBounds(290, 150, 160, 25);
    setUpButton(networkGameButton);

    headline2 = new JLabel();
    headline2.setForeground(FONT_COLOR);
    headline2.setText("Not started Reversi games: ");
    headline2.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE2));
    headline2.setBounds(50, 200, 400, 60);

    quit = new JButton("Back");
    quit.setToolTipText("Go back to main menu");
    quit.setBounds(350, 700, 50, 25);
    setUpButton(quit);
    add(serverAddressLabel);
    add(serverAddress);
    add(networkGameButton);
    add(quit);
    add(headline);
    add(headline2);
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
            controller.showStartView();
          }
        });

    networkGameButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            try {
              address = InetAddress.getByName(serverAddress.getText());
            } catch (UnknownHostException e) {
              e.printStackTrace();
            }
            controller.startNetworkGame(address);
          }
        });
  }
}
