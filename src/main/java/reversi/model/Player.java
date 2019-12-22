package reversi.model;

/** Enum class representing all different players in the reversi game. */
public enum Player {
    WHITE("White"),
    BLACK("Black");

    private final String playerName;
	//private int diskCount = 32;

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

	/*public int getDiskCount(){
		return diskCount;
	}


	void setDiskCount(int diskCount){
		this.diskCount = diskCount;
	}*/

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
