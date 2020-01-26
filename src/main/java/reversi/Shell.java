package reversi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import reversi.model.AiReversi;
import reversi.model.Cell;
import reversi.model.Disk;
import reversi.model.GameField;
import reversi.model.GameState;
import reversi.model.Model;
import reversi.model.Phase;
import reversi.model.Player;
import reversi.model.Reversi;

/** This class provides a shell to play reversi. Includes a main method to run the shell. */
public class Shell {

  private static final String PROMPT = "Reversi> ";
  private static final String ERR_MSG = "Error! ";

  private static final char START_LETTER = 'A';
  private static final int START_ROW = 1;

  private static final char CELL_NO_CONTENT = '.';
  private static final char CELL_DISK_WHITE = 'w';
  private static final char CELL_DISK_BLACK = 'b';

  private static final String HOTSEAT = "HOTSEAT";
  private static final String SINGLE = "SINGLE";

  private Model reversi;

  private boolean isHotseatGame;

  /**
   * Read and process input until the quit command has been entered.
   *
   * @param args Command line arguments.
   * @throws IOException Error reading from stdin.
   */
  public static void main(String[] args) throws IOException {
    final Shell shell = new Shell();
    shell.run();
  }

  /**
   * Run the reversi shell. Shows prompt 'Reversi>', takes commands from the user and executes them.
   *
   * @throws IOException Thrown when failing to read from stdin.
   */
  public void run() throws IOException {
    BufferedReader in =
        new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    boolean quit = false;

    while (!quit) {
      System.out.print(PROMPT);

      String input = in.readLine();
      if (input == null) {
        break;
      }

      String[] tokens = input.split("\\s+");
      if (tokens.length < 1 || tokens[0].equals("")) {
        printErrorMsg("Empty command."); // optional
        continue;
      }

      switch (tokens[0].toUpperCase()) {
        case "NEW":
          handleCmdNew(tokens);
          break;
        case "MOVE":
          handleCmdMove(tokens);
          break;
        case "PRINT":
          handleCmdPrint();
          break;
        case "HELP":
          handleCmdHelp();
          break;
        case "QUIT":
          quit = true;
          break;
        default:
          printErrorMsg("Unknown command: " + input);
          break;
      }
    }
  }

  /**
   * Creates a new instance of the reversi-application.
   *
   * @param tokens The passed arguments from the user.
   */
  private void handleCmdNew(String[] tokens) {
    if (!checkArgumentNumberValid(tokens, 2)) {
      return;
    }
    String gameType = tokens[1].toUpperCase();
    switch (gameType) {
      case HOTSEAT:
        reversi = new Reversi();
        isHotseatGame = true;
        break;
      case SINGLE:
        reversi = new AiReversi();
        isHotseatGame = false;
        printErrorMsg("Gamemode not available yet");
        break;
      default:
        printErrorMsg("Unknown game type: " + gameType);
    }
  }

  /**
   * Process the user input and attempt to make a move. In case of success, the application
   * additionally determines whether a player has now won the game.
   *
   * @param tokens The user input which must contain the strings of the source- and target-cell.
   */
  private void handleCmdMove(String[] tokens) {
    if (!checkGameRunning()) {
      return;
    }
    if (!checkArgumentNumberValid(tokens, 2) || !checkValidCellFormat(tokens[1])) {
      return;
    }

    String targetCellToken = tokens[1];

    Cell targetCell = new Cell(parseColumnValue(targetCellToken), parseRowValue(targetCellToken));

    // Get the current player before executing the move, because afterwards the opponent is already
    // set as the active player.
    Player currentPlayer = reversi.getState().getCurrentPlayer();
    if (!reversi.move(targetCell)) {
      printErrorMsg("Could not move disk to " + formatCell(targetCell) + ".");
      return;
    }

    System.out.println(currentPlayer + " moved disk to " + formatCell(targetCell));

    checkNextTurn(currentPlayer);
  }

  /**
   * Print a message if a player has to skip his turn, or if the game has ended.
   *
   * @param currentPlayer The player that has just successfully moved a disk.
   */
  private void checkNextTurn(Player currentPlayer) {
    GameState state = reversi.getState();
    switch (state.getCurrentPhase()) {
      case RUNNING:
        if (state.getCurrentPlayer() == currentPlayer && isHotseatGame) {
          final Player opponent = getOpponentOfPlayer(currentPlayer);
          System.out.println(opponent + " must miss a turn");
        }
        break;
      case FINISHED:
        printWinner(state);
        break;
      default:
        throw new AssertionError("Unhandled game phase: " + reversi.getState().getCurrentPhase());
    }
  }

  /**
   * Helper method that checks if the given token is well-formed, i.e. if it has the correct syntax.
   * The token must consist of exactly two letters, where the former is a letter and the latter a
   * number.
   *
   * @param token A token given as plain string.
   * @return <code>true</code> if the token is well-formed, <code>false</code> otherwise.
   */
  private boolean checkValidCellFormat(String token) {
    if (token == null
        || token.length() != 2
        || !Character.isLetter(token.charAt(0))
        || !Character.isDigit(token.charAt(1))) {
      printErrorMsg("Invalid cell format");
      return false;
    } else {
      return true;
    }
  }

  /**
   * Return the opponent of the current {@link Player}.
   *
   * @param player The current player.
   * @return The opponent player, which is either {@link Player#BLACK} or {@link Player#WHITE}.
   */
  private Player getOpponentOfPlayer(Player player) {
    switch (player) {
      case WHITE:
        return Player.BLACK;
      case BLACK:
        return Player.WHITE;
      default:
        throw new AssertionError("Unhandled player: " + player);
    }
  }

  /**
   * Formats a given {@link Cell} such that it has the correct format when writing it to stdout. For
   * example, a cell with coordinates (0,0) would be transformed to a string-token with value 'A1'.
   *
   * @param cell The cell that is to be written to stdout.
   * @return A string-token that consists of exactly two letters.
   */
  private String formatCell(Cell cell) {
    int column = cell.getColumn();
    int row = cell.getRow();
    return String.valueOf((char) (START_LETTER + column)) + (START_ROW + row);
  }

  /**
   * Prints the winner of the current game. The method expects the {@link Phase} of the game to be
   * {@link Phase#FINISHED finished}.
   *
   * @param state The {@link GameState} of the reversi.
   */
  private void printWinner(GameState state) {
    Phase curPhase = state.getCurrentPhase();
    if (curPhase != Phase.FINISHED) {
      throw new IllegalStateException(
          "Phase must be finished in order to print a winner, but instead the current phase is "
              + curPhase);
    }

    Optional<Player> winner = state.getWinner();
    String outcome = winner.isEmpty() ? "Draw!" : winner.get() + " has won";

    System.out.println("Game over. " + outcome);
  }

  /** Prints the board and the current player to stdout. */
  private void handleCmdPrint() {
    if (!checkGameExists()) {
      return;
    }

    printField(reversi.getState().getField());
    System.out.println("Player's turn: " + reversi.getState().getCurrentPlayer());
  }

  /**
   * Helper method that returns a string of the board in the required format.
   *
   * @param field The {@link GameField} containing the respective data for each cell.
   */
  private void printField(GameField field) {
    StringBuilder sb = new StringBuilder();
    // we go from top to bottom, so we have to start with the highest row (i.e., the last)
    for (int row = GameField.SIZE - 1; row >= 0; row--) {
      // we go from left to right, so we start with the left-most column (i.e., the first)
      sb.append(row + 1).append(" ");
      for (int column = 0; column <= GameField.SIZE - 1; column++) {
        Cell currentCell = new Cell(column, row);
        Optional<Disk> currentContent = field.get(currentCell);
        if (currentContent.isEmpty()) {
          sb.append(CELL_NO_CONTENT);
        } else {
          Player ownerOfDisk = currentContent.get().getPlayer();
          switch (ownerOfDisk) {
            case WHITE:
              sb.append(CELL_DISK_WHITE);
              break;
            case BLACK:
              sb.append(CELL_DISK_BLACK);
              break;
            default:
              throw new AssertionError("Unhandled player: " + ownerOfDisk);
          }
        }
      }
      sb.append("\n");
    }
    // last row of output: print column names
    sb.append(
        "  "); // add 2 white spaces: one for 'missing' row number, one for white space used after
    // row number
    for (char column = 0; column <= GameField.SIZE - 1; column++) {
      sb.append((char) (START_LETTER + column));
    }
    System.out.println(sb.toString());
  }

  /** Prints the helper-text to stdout. */
  private void handleCmdHelp() {
    System.out.println("Reversi!");
    System.out.println("Accepted commands:");
    System.out.println("NEW" + "\t\t" + "initialize a new reversi game with field size '8x8'");
    System.out.println("MOVE t" + "\t" + "move a disk to 't'");
    System.out.println("PRINT" + "\t\t" + "print the current state of board");
    System.out.println("HELP" + "\t\t" + "show this dialog");
    System.out.println("QUIT" + "\t\t" + "quit the program");
  }

  /**
   * Print an error-message in the required format to stdout.
   *
   * @param msg The error message.
   */
  private static void printErrorMsg(String msg) {
    System.out.println(ERR_MSG + msg);
  }

  /**
   * Check if enough tokens are provided. If not, an error message is printed as a side effect.
   *
   * @param tokens The provided tokens.
   * @param requiredLength The required number of tokens.
   * @return {@code true} iff the length of {@code tokens} is equal to {@code number}.
   */
  private static boolean checkArgumentNumberValid(String[] tokens, int requiredLength) {
    if (tokens.length != requiredLength) {
      printErrorMsg("Wrong number of arguments.");
      return false;
    } else {
      return true;
    }
  }

  /**
   * Check whether an instance of the game has already been created. In case of failure, an error
   * message is printed.
   *
   * @return <code>true</code> if a game already exists, <code>false</code> otherwise.
   */
  private boolean checkGameExists() {
    if (reversi == null) {
      printErrorMsg("No game initialized.");
      return false;
    }
    return true;
  }

  /**
   * Checks the game for its current {@link Phase}. Only succeeds in case of {@link Phase#RUNNING}.
   *
   * @return <code>true</code> if the game is currently running; <code>false</code> otherwise.
   */
  private boolean checkGameRunning() {
    if (!checkGameExists()) {
      return false;

    } else if (reversi.getState().getCurrentPhase() != Phase.RUNNING) {
      printErrorMsg("No game running at the moment");
      return false;

    } else {
      return true;
    }
  }

  /**
   * Parse the row value of a given cell string and return the corresponding index from 0-7.
   *
   * <p>For example, <code>parseRowValue("C1") = 0</code>
   *
   * @param value the string to parse
   * @return the corresponding row index, from 0-7
   */
  private int parseRowValue(String value) {
    char number = value.charAt(1);
    if (!Character.isDigit(number)) {
      throw new IllegalArgumentException("Char '" + number + "' is not a number.");
    }
    return Character.getNumericValue(number) - START_ROW;
  }

  /**
   * Parse the column value of a given cell string and return the corresponding index from 0-7.
   *
   * <p>For example, <code>parseColumnValue("C1") = 2</code>
   *
   * @param value the string to parse
   * @return the corresponding row index, from 0-7
   */
  private int parseColumnValue(String value) {
    char letter = value.charAt(0);
    if (!Character.isLetter(letter)) {
      throw new IllegalArgumentException("Char '" + letter + "' is not a letter.");
    }
    return letter - START_LETTER;
  }
}
