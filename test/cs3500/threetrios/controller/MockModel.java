package cs3500.threetrios.controller;

import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.Tile;
import cs3500.threetrios.model.TrioMap;
import cs3500.threetrios.model.TrioModel;
import cs3500.threetrios.model.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock model test for controller tests.
 */
class MockModel implements TrioModel<PlayCard> {

  private boolean initialized = false;
  private boolean cardPlaced = false;
  private final List<ModelWatcher<PlayCard>> modelWatchers;
  private IPlayer<PlayCard> currentPlayer;
  private IPlayer<PlayCard> redPlayer;
  private IPlayer<PlayCard> bluePlayer;

  /**
   * Constructs the mock.
   */
  public MockModel() {
    this.modelWatchers = new ArrayList<>();
  }

  /**
   * Helper method for testing: set's current turn.
   *
   * @param color the turn color.
   */
  public void setCurrentTurn(PlayerColor color) {
    currentPlayer = (color == PlayerColor.RED) ? redPlayer : bluePlayer;
  }

  /**
   * Helper method for testing: checks if card was placed.
   *
   * @return true if yes false otherwise.
   */
  public boolean wasCardPlaced() {
    return cardPlaced;
  }

  /**
   * Helper method for testing: Notifies a model watcher for a new turn.
   */
  public void notifyTurn() {
    for (ModelWatcher<PlayCard> watcher : modelWatchers) {
      watcher.signalTurn();
    }
  }

  @Override
  public void initializeGame(String mapName, String deckName, boolean shuffle,
      IPlayer<PlayCard> redPlayer, IPlayer<PlayCard> bluePlayer) {
    this.initialized = true;
    this.redPlayer = redPlayer;
    this.bluePlayer = bluePlayer;
    this.currentPlayer = redPlayer;
  }

  @Override
  public void addListener(ModelWatcher<PlayCard> observer) {
    if (observer != null) {
      modelWatchers.add(observer);
    }
  }

  @Override
  public void placeCard(int handIndex, int row, int col) {
    if (!initialized) {
      throw new IllegalStateException("game hasnt started");
    }
    if (getTurn() == currentPlayer) {
      cardPlaced = true;
    }
  }

  @Override
  public IPlayer<PlayCard> getTurn() {
    if (!initialized) {
      throw new IllegalStateException("game hasnt started");
    }
    return currentPlayer;
  }

  @Override
  public IPlayer<PlayCard> getRedPlayer() {
    return redPlayer;
  }

  @Override
  public IPlayer<PlayCard> getBluePlayer() {
    return bluePlayer;
  }

  @Override
  public List<PlayCard> getPlayerHand(IPlayer<PlayCard> player) {
    List<PlayCard> hand = new ArrayList<>();
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.ONE);
    values.put(Compass.SOUTH, Value.TWO);
    values.put(Compass.EAST, Value.THREE);
    values.put(Compass.WEST, Value.FOUR);
    hand.add(new PlayCard("MockCard", values));
    return hand;
  }

  @Override
  public Cell<PlayCard> getTile(int x, int y) {
    return new Tile<>(false);
  }

  @Override
  public int getGridHeight() {
    return 3;
  }

  @Override
  public int getGridWidth() {
    return 3;
  }

  @Override
  public TrioMap<PlayCard> getGrid() {
    return null;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public IPlayer<PlayCard> getWinner() {
    return null;
  }

  @Override
  public int getScore(IPlayer<PlayCard> player) {
    return 0;
  }

  @Override
  public int getFlipTotal(IPlayer<PlayCard> player, PlayCard card, int x, int y) {
    return 0;
  }
}