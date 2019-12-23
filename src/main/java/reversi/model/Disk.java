package reversi.model;

/**
 * A class representing a single game piece called disk of the reversi board including its
 * corresponding color.
 */
public class Disk {

  private final Player player;

  /**
   * Create a new <code>Disk</code>-object that is owned by the specified player.
   *
   * @param player The owner of the disk.
   */
  Disk(Player player) {
    this.player = player;
  }

  /**
   * Return the {@link Player} that is the owner of this <code>Disk</code>.
   *
   * @return The owning player.
   */
  public Player getPlayer() {
    return player;
  }
}
