package reversi.model;

/** Enum class representing all different players in the reversi game. */
public enum Player {
  WHITE("White"),
  BLACK("Black");

  private final String playerName;

  static final int DISK_COUNT_START = 32;
  private int diskCount = DISK_COUNT_START;

  /**
   * Creates a new <code>Player</code>-object that takes a string argument for the internal
   * representation.
   *
   * @param playerName The string-representation of the constant.
   */
  Player(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public String toString() {
    return playerName;
  }

  /**
   * Return the current amount of disks the player has left.
   *
   * @return the amount of disks.
   */
  public int getDiskCount() {
    return diskCount;
  }

  /**
   * Set the amount of disks the player holds.
   *
   * @param diskCount The amount of disks the player holds now.
   */
  void setDiskCount(int diskCount) {
    this.diskCount = diskCount;
  }

  /**
   * Return the opponent of the passed <code>player</code>-object.
   *
   * @param player The player whose opponent is to be determined.
   * @return The opponent player that is either {@link Player#BLACK} or {@link Player#WHITE}.
   */
  public static Player getOpponentOf(Player player) {
    switch (player) {
      case BLACK:
        return WHITE;
      case WHITE:
        return BLACK;
      default:
        throw new AssertionError("Unhandled player: " + player);
    }
  }
}
