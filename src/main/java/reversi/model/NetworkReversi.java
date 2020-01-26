package reversi.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import reversi.model.network.Game;
import reversi.model.network.Lobby;

// TODO
public class NetworkReversi implements Model {

  private final InetAddress serverAddress;
  private Reversi delegate = new Reversi();

  private Lobby lobby;
  private Player assignedPlayer;

  public NetworkReversi(InetAddress serverAddress) {
    this.serverAddress = serverAddress;
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    delegate.addPropertyChangeListener(pcl);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    delegate.removePropertyChangeListener(pcl);
  }

  @Override
  public void newGame() {
    delegate.newGame();
  }

  @Override
  public void startLobby() throws IOException {
    lobby = new Lobby(this, serverAddress);
    lobby.start();
  }

  @Override
  public void stopGame() throws IOException {
    delegate.stopGame();
    lobby.leaveGame();
  }

  @Override
  public void startGame(Player player) throws IOException {
    delegate.newGame(false);
    getState().setCurrentPhase(Phase.WAITING);
    assignedPlayer = player;
    lobby.startGame(player);
  }

  @Override
  public void joinGame(int gameID, Player player) throws IOException {
    delegate.newGame(false);
    getState().setCurrentPhase(Phase.WAITING);
    assignedPlayer = player;
    lobby.joinGame(gameID, player);
  }

  @Override
  public void leaveLobby() throws IOException {
    lobby.stop();
  }

  public void updateLobby(List<Game> games) {
    lobby.updateLobby(games);
    delegate.notifyListeners(false);
  }

  public void startPhase() {
    getState().setCurrentPhase(Phase.RUNNING);
    delegate.notifyListeners(false);
  }

  public void endGame() {
    getState().setCurrentPhase(Phase.DISCONNECTED);
    // delegate.notifyListeners(false);
  }

  @Override
  public List<Game> getOpenGames() {
    return lobby.getOpenGames();
  }

  public void setState(GameState state) {
    delegate.setState(state);
  }

  @Override
  public GameState getState() {
    return delegate.getState();
  }

  @Override
  public boolean move(Cell to) {
    if (getState().getCurrentPlayer().equals(assignedPlayer)) {
      return delegate.move(to);
    }

    return false;
  }

  @Override
  public void undoMove() {
    delegate.undoMove();
  }

  @Override
  public Set<Cell> getPossibleMovesForPlayer(Player player) {
    if (player.equals(assignedPlayer)) {
      return delegate.getPossibleMovesForPlayer(player);
    } else {
      return Collections.emptySet();
    }
  }
}
