package reversi.model.network;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/** Class responsible for containing all the components of a game and returning them if called. */
public class Game {

  private boolean hasPlayerWhite = false;
  private boolean hasPlayerBlack = false;
  private transient ObjectOutputStream playerBlack;
  private transient ObjectOutputStream playerWhite;
  private transient int lastStepSent = -1;

  Game(ObjectOutputStream client1, ObjectOutputStream client2) {
    playerBlack = client1;
    playerWhite = client2;
    if (playerBlack != null) {
      hasPlayerBlack = true;
    }
    if (playerWhite != null) {
      hasPlayerWhite = true;
    }
  }

  /**
   * Sets clients to the Player color BLACK.
   *
   * @param client
   */
  void setPlayerBlack(ObjectOutputStream client) {
    playerBlack = client;
    hasPlayerBlack = true;
  }

  /**
   * Sets clients to the Player color WHITE.
   *
   * @param client
   */
  void setPlayerWhite(ObjectOutputStream client) {
    playerWhite = client;
    hasPlayerWhite = true;
  }

  void setLastStepSent(int value) {
    lastStepSent = value;
  }

  int getLastStepSent() {
    return lastStepSent;
  }

  ObjectOutputStream getPlayerBlack() {
    return playerBlack;
  }

  ObjectOutputStream getPlayerWhite() {
    return playerWhite;
  }

  void setHasPlayerBlack(boolean value) {
    hasPlayerBlack = value;
  }

  void setHasPlayerWhite(boolean value) {
    hasPlayerWhite = value;
  }

  /** @return true if a Player BLACK is existing in the game. */
  public boolean getHasPlayerBlack() {
    return hasPlayerBlack;
  }

  /** @return true if a Player WHITE is existing in the game. */
  public boolean getHasPlayerWhite() {
    return hasPlayerWhite;
  }

  boolean clientInGame(ObjectOutputStream from) {
    return ((playerBlack != null && playerBlack.equals(from))
        || (playerWhite != null && playerWhite.equals(from)));
  }

  List<ObjectOutputStream> getBothClients() {
    List<ObjectOutputStream> list = new ArrayList<>();
    list.add(playerBlack);
    list.add(playerWhite);
    return list;
  }
}
