package reversi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Implementation of a view class to visualize a reversi game and handle the user input. */
public class ReversiView extends JPanel implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;

  private DrawBoard drawboard;
  private JLabel headline;
  private JLabel infoLabel;
  private JButton quit;
  private JButton reset;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);

  private Controller controller;

  /**
   * Creates a view where all elements are set up to play a reversi game.
   *
   * @param controller
   */
  ReversiView(Controller controller) {
    this.controller = controller;
    drawboard = new DrawBoard(controller);
    createDesign();
    setActionListener();
    // model.addPropertyChangeListener(this);
  }

  private void createDesign() {
    int fontsize_headline = 40;
    int fontsize_infoLabel = 18;

    drawboard.setBounds(0, 0, 600, 600);

    setLayout(null);
    setBackground(BACKGROUND_COLOR);
    setPreferredSize(new Dimension(600, 600));

    headline = new JLabel();
    headline.setForeground(FONT_COLOR);
    headline.setText("Reversi");
    headline.setFont(new Font("Comic Sans MS", Font.BOLD, fontsize_headline));
    headline.setBounds(215, -70, 200, 200);

    infoLabel = new JLabel();
    infoLabel.setForeground(FONT_COLOR);
    // For test purpose only.
    infoLabel.setText("Current Player: White");
    infoLabel.setFont(new Font("Comic Sans MS", Font.BOLD, fontsize_infoLabel));
    infoLabel.setBounds(190, -20, 300, 200);

    quit = new JButton("Quit");
    quit.setToolTipText("Quit the game");
    quit.setBounds(220, 510, 50, 25);
    setUpButton(quit);

    reset = new JButton("Reset");
    reset.setToolTipText("Reset the game");
    reset.setBounds(300, 510, 50, 25);
    setUpButton(reset);

    add(reset);
    add(quit);
    add(headline);
    add(infoLabel);
    add(drawboard);
    addMouseListener((MouseListener) controller);
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

  private void updateCurrentPlayerInfo() {
    setInfoLabelText("Current Player: ");
  }

  private void setInfoLabelText(String message) {
    infoLabel.setText(message);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    SwingUtilities.invokeLater(
        new Runnable() {

          @Override
          public void run() {
            handleChangeEvent(evt);
          }
        });
  }

  /**
   * The observable (= model) has just published that it has changed its state. The GUI needs to be
   * updated accordingly here.
   *
   * @param event The event that has been fired by the model.
   */
  private void handleChangeEvent(PropertyChangeEvent event) {
    // Call paintComponent() when model has changed.
    drawboard.repaint();
    // TODO
  }
}
