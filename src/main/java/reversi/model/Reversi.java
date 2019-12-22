package reversi.model;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of a reversi class that handles the logic for a reversi game.
 * It is the entry-class to the internal structure of the game-model and provides all methods
 * necessary for an user-interface to playing a game successfully.
 */
public class Reversi implements Model {

    private static final int DIRECTION_UPWARD = 1;
    private static final int DIRECTION_DOWNWARD = -1;

    private static final int EXPECTED_HISTORY_LENGTH = 60;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final Deque<GameState> stateHistory = new ArrayDeque<>(EXPECTED_HISTORY_LENGTH);

    private GameState state;

    /**
     * Initialize a new Reversi-Game in which everything is set up in its initial position. The game is
     * ready to be played immediately after.
     */
    public Reversi() {
        newGame();
    }

    /**
     * Creates a new reversi object in which the passed {@link GameState}-object is taken as new
     * instance of this class.
     *
     * @param gameState A non-null {@link GameState}-object.
     */
    public Reversi(GameState gameState) {
        state = requireNonNull(gameState);
    }


    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener pcl) {
        requireNonNull(pcl);
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener pcl) {
        requireNonNull(pcl);
        support.removePropertyChangeListener(pcl);
    }

    /**
     * Invokes the firing of an event, such that any attached observer (i.e., {@link
     * PropertyChangeListener}) is notified that a change happened to this model.
     */
    protected void notifyListeners() {
        notifyListeners(true);
        //TODO
    }

    private void notifyListeners(boolean wasActiveChange) {
        support.firePropertyChange(STATE_CHANGED, null, this);
        if (wasActiveChange) {
            support.firePropertyChange(NEW_MOVE, null, this);
        }
        //TODO
    }

    @Override
    public synchronized GameState getState() {
        return state;
    }

    /**
     * Sets the game state to the given value. Can be used to override the existing game state. After
     * updating the game state, listeners will be notified about the change.
     *
     * @param state the new game state
     */
    synchronized void setState(GameState state) {
        this.state = state;
        notifyListeners(false);
    }

    @Override
    public synchronized void newGame() {
        state = new GameState(new GameField());

        notifyListeners();
    }

    @Override
    public synchronized boolean move(Cell to) {

        if (state.getCurrentPhase() != Phase.RUNNING) {
            return false;
        }

        //TODO
		//wenn diskCount > 30
			//if to is within middle 4 cells && the cell is empty
				//execute move
				//return true
			//sonst
				//return false

		//sonst
			//if state.getField().get(to).isEmpty (1.)
				//if isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn+1, to.getRow)).get().isOppositeColor 
				//oder isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn-1, to.getRow)).get().isOppositeColor
				//oder isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn, to.getRow+1)).isOppositeColor
				//oder isWithinBound && isPresent && state.getField().get(new Cell(to.getColumn, to.getRow-1)).isOppositeColor (2.)
					//sets definieren;
					//if (check3Vertical || check3Horizontal || check3Diagonal)(3.)
							
						//we have multiple sets of cells in different directions, cutSetAt(currentPlayer) for all of these sets, then add sets together to new set, this is the set of disks that have to get flipped
							//flip these disks
						

						//getState().increaseMoveCounter();
						//notifyListeners();

						//return true;
        return true;
    }

    @Override
    public synchronized void undoMove() {
        state = stateHistory.pop();
        notifyListeners();
    }

    //TODO: javadoc
	private void flipDisks(Set<Cell> set){
		for(Cell cell : set) {
            state.getField().set(cell, new Disk(Player.getOpponentOf(state.getCurrentPlayer())));
        }
	}

    //TODO: javadoc
	private Set<Cell> cutSetAt(Player player, Set<Cell> set){
		Set newSet = new HashSet<Cell>();

		for(Cell cell : set) {
            if(state.getField().get(cell).isPresent() && state.getField().get(cell).get().getPlayer().equals(player)) {
                break;
            }else {
                newSet.add(cell);
            }
        }
		return newSet;
	}

	private boolean check3Vertical(Cell to){
		
		//for Cell cell in diskOnStraightLineVertical
			//if(cell.isPresent && isOwnColor)
				//break;
			//if(cell.isEmpty)
				//return false
		//return true
        return true;
	}

	private boolean check3Horizontal(Cell to){
		//for Cell cell in diskOnStraightLineHorizontal
			//if(cell.isPresent && isOwnColor)
				//break;
			//if(cell.isEmpty)
				//return false
		//return true
        return true;
	}

	private boolean check3Diagonal(Cell to){
		//for Cell cell in diskOnStraightLineDiagonal
			//if(cell.isPresent && isOwnColor)
				//break;
			//if(cell.isEmpty)
				//return false
		//return true
        return true;
	}

	private Set<Cell> diskOnStraightLineDiagonal(Cell to){
        return new HashSet<>();
        //TODO: split each case into own method
		
		//if opposite disk is on immediate upper right cell of to
			//HashSet set = new HashSet<>()
			/*for i oppositeDisk.getRow bis 8
				state.getField().get(new Cell(to.getColumn + i, to.getRow + i)).add cell to set

			return set;*/
		
		//if opposite disk is on immediate upper left cell of to
			//HashSet set = new HashSet<>()
			/*for i oppositeDisk.getRow bis 0
				state.getField().get(new Cell(to.getColumn - i, to.getRow + i).add cell to set

			return set;*/

		//if opposite disk is on immediate lower right cell of to
			//HashSet set = new HashSet<>()
			/*for i oppositeDisk.getRow bis 8
				state.getField().get(new Cell(to.getColumn + i, to.getRow - i)).add cell to set
					

			return set;*/
		
		//if opposite disk is on immediate lower left cell of to
			//HashSet set = new HashSet<>()
			/*for i oppositeDisk.getRow bis 0
				state.getField().get(new Cell(to.getColumn - i, to.getRow - i)).add cell to set

			return set;*/
	}

	private Set<Cell> disksVertical(Cell to){
        Set disksVertical = new HashSet<Set<Set<Cell>>>();

        Optional<Disk> pawnForward = state.getField().get(new Cell(to.getColumn(), to.getRow()+1));
        Optional<Disk> pawnBehind = state.getField().get(new Cell(to.getColumn(), to.getRow()-1));

        if(pawnForward.isPresent() && pawnForward.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))){
            Set disksForward = new HashSet<Cell>();

            for(int i = to.getRow() + 1; i < GameField.SIZE - 1; i++){
                disksForward.add(state.getField().get(new Cell(i, to.getRow())));
            }

            disksVertical.add(disksForward);
        }

        if(pawnBehind.isPresent() && pawnBehind.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))){
            Set disksVerticalBehind = new HashSet<Cell>();

            for(int i = to.getRow() - 1; i > 0; i--){
                disksVerticalBehind.add(state.getField().get(new Cell(i, to.getRow())));
            }

            disksVertical.add(disksVerticalBehind);
        }

        return disksVertical;
	}

	private Set<Set<Cell>> disksHorizontal(Cell to){
        Set disksHorizontal = new HashSet<Set<Set<Cell>>>();

        Optional<Disk> pawnRight = state.getField().get(new Cell(to.getColumn()+1, to.getRow()));
        Optional<Disk> pawnLeft = state.getField().get(new Cell(to.getColumn()-1, to.getRow()));

        if(pawnRight.isPresent() && pawnRight.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))){
            Set disksRight = new HashSet<Cell>();

            for(int i = to.getColumn() + 1; i < GameField.SIZE - 1; i++){
                disksRight.add(state.getField().get(new Cell(i, to.getRow())));
            }

            disksHorizontal.add(disksRight);
        }

        if(pawnLeft.isPresent() && pawnLeft.get().getPlayer().equals(Player.getOpponentOf(state.getCurrentPlayer()))){
            Set disksLeft = new HashSet<Cell>();

            for(int i = to.getColumn() - 1; i > 0; i--){
                disksLeft.add(state.getField().get(new Cell(i, to.getRow())));
            }

            disksHorizontal.add(disksLeft);
        }

        return disksHorizontal;
	}


    /**
     * Ends the game by setting its current phase to {@link Phase#FINISHED}. The winner is also set
     * depending on the given input.
     *
     * <p>Afterwards, an event is fired in which all observers are notified about the changed phase.
     *
     * @param winner An {@link Optional} containing either the winning player, or empty otherwise (if
     *     the game ended in a draw).
     */
    private void setGameFinished(Optional<Player> winner) {
        state.setCurrentPhase(Phase.FINISHED);
        state.setWinner(winner);

        notifyListeners();
    }

    /**
     * Checks a player if he is able to execute a move.
     *
     * @param player The player to be checked.
     * @return <code>true</code> if player is able to move, <code>false</code> otherwise.
     */
    private boolean canExecuteMove(Player player) {
        Set<Cell> cells = state.getAllCellsOfPlayer(player);
        for (Cell cell : cells) {
            Set<Cell> possibleMoves = getPossibleMovesForPlayer(player);
            if (!possibleMoves.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized Set<Cell> getPossibleMovesForPlayer(Player player) {
        //TODO
        return new HashSet<>();
    }
}
