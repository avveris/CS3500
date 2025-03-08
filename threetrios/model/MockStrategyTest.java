package cs3500.threetrios.model;

import cs3500.threetrios.controller.ModelWatcher;
import cs3500.threetrios.controller.TriosReaderImpl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A stub implementation of a Three Trios Model to test strategies properly check.
 */
public class MockStrategyTestModel implements TrioModel<PlayCard> {

  private Appendable log;
  private List<PlayCard> mockHand;
  //Swap between the two to see how Strategy 3 reacts
  private List<PlayCard> opponentHand;
  private TrioMap<PlayCard> grid;
  private IPlayer<PlayCard> redPlayer;
  private IPlayer<PlayCard> bluePlayer;
  //Cards for testing
  private PlayCard allA;
  private PlayCard filler;

  /**
   * Constructs a Mock for testing with a log to read transcript.
   *
   * @param log an appendable that is written to with commands.
   */
  public MockStrategyTestModel(Appendable log) {
    this.log = log;
    mockHand = new ArrayList<>();
    opponentHand = new ArrayList<>();
    Map<Compass, Value> validValues = new HashMap<>();
    validValues.put(Compass.NORTH, Value.ONE);
    validValues.put(Compass.SOUTH, Value.ONE);
    validValues.put(Compass.EAST, Value.ONE);
    validValues.put(Compass.WEST, Value.ONE);
    filler = new PlayCard("Filler", validValues);

    validValues.put(Compass.NORTH, Value.A);
    validValues.put(Compass.SOUTH, Value.A);
    validValues.put(Compass.EAST, Value.A);
    validValues.put(Compass.WEST, Value.A);
    allA = new PlayCard("A", validValues);

    validValues.put(Compass.NORTH, Value.ONE);
    validValues.put(Compass.SOUTH, Value.TWO);
    validValues.put(Compass.EAST, Value.FOUR);
    validValues.put(Compass.WEST, Value.THREE);
    mockHand.add(new PlayCard("Far Left", validValues));

    validValues.put(Compass.NORTH, Value.ONE);
    validValues.put(Compass.SOUTH, Value.TWO);
    validValues.put(Compass.EAST, Value.FOUR);
    validValues.put(Compass.WEST, Value.THREE);
    mockHand.add(new PlayCard("Middle Card", validValues));

    validValues.put(Compass.NORTH, Value.ONE);
    validValues.put(Compass.SOUTH, Value.THREE);
    validValues.put(Compass.EAST, Value.FOUR);
    validValues.put(Compass.WEST, Value.FIVE);
    mockHand.add(new PlayCard("End Card", validValues));

    redPlayer = new HumanPlayer<PlayCard>(this);
    redPlayer.setColor(PlayerColor.RED);
    bluePlayer = new HumanPlayer<PlayCard>(this);
    bluePlayer.setColor(PlayerColor.BLUE);
    redPlayer.colorHand(mockHand);
    fillOpponentHand();
    String gridPath = "docs" + File.separator + "3_3Grid.config";
    File gridFile = new File(gridPath);
    grid = new Grid<PlayCard>(new TriosReaderImpl().read(gridFile));
  }


  private void fillOpponentHand() {
    opponentHand.add(allA);
    opponentHand.add(allA);
    //Uncomment to examine strategy 3 behavior.
    //opponentHand.add(filler);
    //opponentHand.add(filler);
    //bluePlayer.colorHand(opponentHand);
  }

  @Override
  public void placeCard(int handIndex, int posY, int posX) {
    grid.getTile(posY, posX).playToTile(filler);
  }

  @Override
  public void initializeGame(String mapName, String deckName, boolean shuffle,
      IPlayer<PlayCard> redPlayer, IPlayer<PlayCard> bluePlayer) {
    writeMessage("Calling to start game");
  }

  @Override
  public void addListener(ModelWatcher<PlayCard> observer) {
    writeMessage("Trying to add observer");
  }

  @Override
  public TrioMap<PlayCard> getGrid() {
    return grid.cloneGrid();
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
  public IPlayer<PlayCard> getTurn() {
    return null;
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
  public int getScore(IPlayer<PlayCard> player) {
    return 0;
  }

  @Override
  public int getFlipTotal(IPlayer<PlayCard> player, PlayCard card, int x, int y) {
    if (x == 2 && y == 2) {
      return 10;
    }
    if (x == 0 && y == 2 && card == mockHand.get(2) && grid.getTile(1, 1).getSpace() != null) {
      return 10;
    }
    return 1;
  }

  @Override
  public List<PlayCard> getPlayerHand(IPlayer<PlayCard> player) {
    if (player == redPlayer) {
      return mockHand;
    } else {
      return opponentHand;
    }
  }

  @Override
  public Cell<PlayCard> getTile(int x, int y) {
    writeMessage("Checking tile (" + x + ", " + y + ")\n");
    return grid.getTile(y, x);
  }

  @Override
  public int getGridHeight() {
    return 3;
  }

  @Override
  public int getGridWidth() {
    return 3;
  }

  private void writeMessage(String msg) {
    try {
      log.append(msg);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
