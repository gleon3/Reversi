package reversi.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import reversi.model.Model;
import reversi.model.Phase;
import reversi.model.Player;

/**
 * Implementation of the main view to visualize a reversi game. It provides functionalities for
 * quitting and reseting the game and by the press of a button show and delete possible moves. Also
 * it shows which player has a turn.
 */
public class ReversiView extends JPanel implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;

  private DrawBoard drawBoard;
  private JLabel headline;
  private JLabel infoLabel;
  private JLabel errorLabel;
  private JButton quit;
  private JButton reset;
  private JButton possibleMoves;
  private JButton deleteMoves;
  private static final Color BACKGROUND_COLOR = new Color(0, 153, 0);
  private static final Color FONT_COLOR = new Color(240, 240, 240);
  private static final int FONTSIZE_HEADLINE = 50;
  private static final int FONTSIZE_INFO_LABEL = 20;
  private static final int FONTSIZE_ERROR_LABEL = 15;

  private ReversiController controller;
  private Model model;

  /**
   * Creates a view where all elements are set up to play a reversi game.
   *
   * @param controller Validates and forwards any user input.
   * @param model Model that handles the logic of the game.
   */
  ReversiView(Model model, Controller controller) {
    this.model = model;
    this.controller = (ReversiController) controller;
    drawBoard = new DrawBoard(model, controller);
    createDesign();
    setActionListener();
    model.addPropertyChangeListener(this);
  }

  /**
   * Method that sets up the design of the view and positions all the objects like the game board
   * and buttons.
   */
  private void createDesign() {

    drawBoard.setBounds(110, 100, 560, 560);

    setLayout(null);
    setBackground(BACKGROUND_COLOR);

    headline = new JLabel();
    headline.setForeground(FONT_COLOR);
    headline.setText("Reversi");
    headline.setFont(new Font("Serif", Font.BOLD, FONTSIZE_HEADLINE));
    headline.setBounds(290, 5, 400, 50);

    infoLabel = new JLabel();
    infoLabel.setForeground(FONT_COLOR);
    infoLabel.setFont(new Font("Serif", Font.BOLD, FONTSIZE_INFO_LABEL));
    infoLabel.setBounds(280, 50, 400, 50);

    errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED);
    errorLabel.setFont(new Font("Serif", Font.BOLD, FONTSIZE_ERROR_LABEL));
    errorLabel.setBounds(350, 730, 600, 40);

    quit = new JButton("Quit");
    quit.setToolTipText("Quit the game");
    quit.setBounds(265, 680, 50, 25);
    setUpButton(quit);

    reset = new JButton("Reset");
    reset.setToolTipText("Reset the game");
    reset.setBounds(320, 680, 50, 25);
    setUpButton(reset);

    possibleMoves = new JButton("Moves");
    possibleMoves.setToolTipText("Shows the possible moves on the board.");
    possibleMoves.setBounds(385, 680, 50, 25);
    setUpButton(possibleMoves);

    deleteMoves = new JButton("Delete");
    deleteMoves.setToolTipText("Deletes the showing moves from the board");
    deleteMoves.setBounds(450, 680, 50, 25);
    setUpButton(deleteMoves);

    add(reset);
    add(quit);
    add(possibleMoves);
    add(deleteMoves);
    add(headline);
    add(infoLabel);
    add(errorLabel);
    add(drawBoard);
    updateCurrentPlayerInfo();
    setBoard();
    addMouseListener((MouseListener) controller);
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
    quit.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            handleQuit();
          }
        });

    reset.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            handleReset();
          }
        });

    possibleMoves.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            showPossibleMoves();
          }
        });

    deleteMoves.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            deletePossibleMoves();
          }
        });
  }

  /**
   * Sets the game board in the moment two clients are connected to the server, otherwise a info
   * message is shown.
   */
  private void setBoard() {
    if (model.getState().getCurrentPhase() == Phase.RUNNING || model.getState().getCurrentPhase() == Phase.FINISHED) {
      drawBoard.setVisible(true);
      infoLabel.setBounds(280, 50, 400, 50);
    } else {
      drawBoard.setVisible(false);
      setInfoLabelText("Waiting for player two!");
      infoLabel.setBounds(270, 300, 400, 50);
    }
  }

  /** Shows a message and asks the user if he wants to quit the game. */
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

  /** Shows a message and asks the user if he wants to reset the game. */
  private void handleReset() {
    int result =
        JOptionPane.showConfirmDialog(
            this,
            "Do you really want to reset this game?",
            "Please confirm your choice",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    // do nothing if user didn't click on the 'yes'-option
    if (result == JOptionPane.YES_OPTION) {
      controller.resetGame();
    }
  }

  /**
   * Disposes the reversiView by unsubscribing it from the model and calling the drawBoard dispose()
   * function.
   */
  void dispose() {
    model.removePropertyChangeListener(this);
    drawBoard.dispose();
  }

  /** Shows an error message. */
  void showErrorMessage(String message) {
    errorLabel.setText(message);
  }

  /** Hides an error message that was previously displayed to the user. */
  private void hideErrorMessage() {
    errorLabel.setText("");
  }

  /** Shows all possible moves on the reversi game board. */
  private void showPossibleMoves() {
    controller.possibleMoves = model.getPossibleMovesForPlayer(model.getState().getCurrentPlayer());
    drawBoard.repaint();
  }

  /** Deletes all current shown possible moves from the game board. */
  private void deletePossibleMoves() {
    if (controller.possibleMoves != null) {
      controller.possibleMoves.clear();
    }
    drawBoard.repaint();
  }

  /** Updates the label text that informs the user about the current player. */
  private void updateCurrentPlayerInfo() {
    setInfoLabelText("Current player: " + model.getState().getCurrentPlayer());
  }

  /**
   * Displays a message in the view.
   *
   * @param message The message to be displayed
   */
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
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      updateCurrentPlayerInfo();
      openDialogIfGameIsOver();
      openDialogIfGameIsDisconnected();
      hideErrorMessage();
      setBoard();
    }
  }

  /**
   * Checks the model if the game has ended. In that case, a dialog is shown to the user in which a
   * respective message with the winner is shown.
   */
  private void openDialogIfGameIsOver() {
    if (model.getState().getCurrentPhase() != Phase.FINISHED) {
      return;
    }

    Optional<Player> playerOpt = model.getState().getWinner();
    if (playerOpt.isPresent()) {
      Player p = playerOpt.get();
      showDialogWindow("Finished!", "There's a winner: Player " + p);
    } else {
      showDialogWindow("Finished!", "Game is over. It's a tie!");
    }
  }

  /**
   * Checks the model if the game has disconnected. In that case, a dialog is shown to the user in
   * which a respective message is shown.
   */
  private void openDialogIfGameIsDisconnected() {
    if (model.getState().getCurrentPhase() != Phase.DISCONNECTED) {
      return;
    }
    int result =
            JOptionPane.showConfirmDialog(
                    this,
                    "The network connection disconnected.",
                    "Disconnect",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE);
    if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.OK_OPTION) {
      controller.showLobby();
    }
  }

  /**
   * Shows a message pane that displays the outcome of the game.
   *
   * @param header The header text.
   * @param message An elaborate message why the game has ended.
   */
  private void showDialogWindow(String header, String message) {
    JOptionPane.showMessageDialog(this, message, header, JOptionPane.INFORMATION_MESSAGE);
  }
}
