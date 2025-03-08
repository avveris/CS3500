package cs3500.threetrios.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.Grid;
import cs3500.threetrios.model.HumanPlayer;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import cs3500.threetrios.model.Tile;
import cs3500.threetrios.model.Value;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ThreeTrioConsoleView implementation. Tests the specific text-based view
 * requirements from the assignment.
 */
public class ViewTest {

  private StringBuilder output;
  private MockModel mockModel;
  private ThreeTrioConsoleView view;

  @Before
  public void setUp() {
    output = new StringBuilder();
    mockModel = new MockModel();
    view = new ThreeTrioConsoleView(mockModel, output);
  }

  // testing the constructor, throws IAE when its null
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullModel() {
    new ThreeTrioConsoleView(null, new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullAppendable() {
    new ThreeTrioConsoleView(mockModel, null);
  }

  // test rendering of empty grid represented by underscore
  @Test
  public void testRenderEmptyGrid() throws IOException {
    mockModel.setGrid(createEmptyGrid(3, 3));
    mockModel.setCurrentTurn(createTestPlayer(PlayerColor.RED));

    view.render();

    String expected =
        "Player: RED" + System.lineSeparator()
        + "___" + System.lineSeparator()
        + "___" + System.lineSeparator()
        + "___" + System.lineSeparator()
        + "Hand:" + System.lineSeparator();

    assertEquals(expected, output.toString());
  }

  // tests that view throws exception before the game is initialized
  @Test(expected = IllegalStateException.class)
  public void testRenderBeforeGameStart() throws IOException {
    // Don't set any grid
    view.render();
  }

  // tests rendering of the current game with current player
  @Test
  public void testRenderInProgressGame() throws IOException {
    mockModel.setGrid(createEmptyGrid(2, 2));
    IPlayer<PlayCard> currentPlayer = createTestPlayer(PlayerColor.BLUE);
    mockModel.setCurrentTurn(currentPlayer);
    mockModel.setGameOver(false);

    view.render();

    String output = this.output.toString();
    assertTrue(output.contains("Player: BLUE"));
  }

  // tests that view throws exception if it tries to render after the game is over
  @Test(expected = IllegalStateException.class)
  public void testRenderAfterGameOver() throws IOException {
    mockModel.setGrid(createEmptyGrid(2, 2));
    mockModel.setGameOver(true);
    mockModel.setWinner(createTestPlayer(PlayerColor.RED));

    view.render();
  }

  // tests the cards with values have proper formatting
  @Test
  public void testRenderPlayerHandWithValues() throws IOException {
    mockModel.setGrid(createEmptyGrid(2, 2));
    IPlayer<PlayCard> player = createTestPlayer(PlayerColor.RED);
    List<PlayCard> hand = new ArrayList<>();

    Map<Compass, Value> values1 = new HashMap<>();
    values1.put(Compass.NORTH, Value.SEVEN);
    values1.put(Compass.SOUTH, Value.THREE);
    values1.put(Compass.EAST, Value.NINE);
    values1.put(Compass.WEST, Value.A);

    hand.add(new PlayCard("playa", values1));
    player.colorHand(hand);
    mockModel.setCurrentTurn(player);

    view.render();

    String expected =
        "Player: RED" + System.lineSeparator()
        + "__" + System.lineSeparator()
        + "__" + System.lineSeparator()
        + "Hand:" + System.lineSeparator()
        + "playa 7 3 9 A" + System.lineSeparator();

    assertEquals(expected, output.toString());
  }


  private Grid<PlayCard> createEmptyGrid(int rows, int cols) {
    Tile<PlayCard>[][] tiles = new Tile[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        tiles[i][j] = new Tile<>(false);
      }
    }
    return new Grid<>(tiles);
  }

  private Grid<PlayCard> createGridWithPattern(String[] pattern) {
    int rows = pattern.length;
    int cols = pattern[0].length();
    Tile<PlayCard>[][] tiles = new Tile[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        char cell = pattern[i].charAt(j);
        boolean isHole = cell == 'X' || cell == ' ';
        tiles[i][j] = new Tile<>(isHole);

        if (!isHole) {
          if (cell == 'R' || cell == 'B') {
            PlayCard card = createTestCard("Test",
                cell == 'R' ? PlayerColor.RED : PlayerColor.BLUE);
            tiles[i][j].playToTile(card);
          }
        }
      }
    }
    return new Grid<>(tiles);
  }

  private PlayCard createTestCard(String name, PlayerColor color) {
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.ONE);
    values.put(Compass.SOUTH, Value.ONE);
    values.put(Compass.EAST, Value.ONE);
    values.put(Compass.WEST, Value.ONE);
    PlayCard card = new PlayCard(name, values);
    card.setColor(color);
    return card;
  }

  private PlayCard createCardWithValues(String name, String north, String south,
      String east, String west) {
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.valueOf(north));
    values.put(Compass.SOUTH, Value.valueOf(south));
    values.put(Compass.EAST, Value.valueOf(east));
    values.put(Compass.WEST, Value.valueOf(west));
    return new PlayCard(name, values);
  }

  private IPlayer<PlayCard> createTestPlayer(PlayerColor color) {
    return new HumanPlayer<PlayCard>(mockModel);
  }


  /**
   * Mock implementation of ReadOnlyTrioModel for testing our view.
   */
  public static class MockModel implements ReadOnlyTrioModel<PlayCard> {

    private Grid<PlayCard> grid;
    private IPlayer<PlayCard> currentPlayer;
    private IPlayer<PlayCard> winner;
    private boolean isGameOver;

    public MockModel() {
      this.isGameOver = false;
    }

    public void setGrid(Grid<PlayCard> grid) {
      this.grid = grid;
    }

    public void setCurrentTurn(IPlayer<PlayCard> player) {
      this.currentPlayer = player;
    }

    public void setWinner(IPlayer<PlayCard> player) {
      this.winner = player;
      this.isGameOver = true;
    }

    public void setGameOver(boolean gameOver) {
      this.isGameOver = gameOver;
    }

    @Override
    public Grid<PlayCard> getGrid() {
      if (grid == null) {
        throw new IllegalStateException("game didn't start yet");
      }
      return grid;
    }

    @Override
    public IPlayer<PlayCard> getTurn() {
      if (grid == null) {
        throw new IllegalStateException("game didn't start yet");
      }
      if (isGameOver) {
        throw new IllegalStateException("game is already over");
      }
      return currentPlayer;
    }

    @Override
    public IPlayer<PlayCard> getRedPlayer() {
      return null;
    }

    @Override
    public IPlayer<PlayCard> getBluePlayer() {
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

    @Override
    public List<PlayCard> getPlayerHand(IPlayer<PlayCard> player) {
      return List.of();
    }


    public Tile<PlayCard> getTile(int x, int y) {
      return null;
    }

    @Override
    public int getGridHeight() {
      return 0;
    }

    @Override
    public int getGridWidth() {
      return 0;
    }

    @Override
    public boolean isGameOver() {
      if (grid == null) {
        throw new IllegalStateException("game hasnt started");
      }
      return isGameOver;
    }

    @Override
    public IPlayer<PlayCard> getWinner() {
      if (grid == null) {
        throw new IllegalStateException("game didnt start yet");
      }
      if (!isGameOver) {
        throw new IllegalStateException("game isnt over yet");
      }
      return winner;
    }
  }

  @Test
  public void testRenderWithBasicDeckCards() throws IOException {
    mockModel.setGrid(createEmptyGrid(2, 2));
    IPlayer<PlayCard> player = createTestPlayer(PlayerColor.RED);
    List<PlayCard> hand = new ArrayList<>();

    Map<Compass, Value> chlorisValues = new HashMap<>();
    chlorisValues.put(Compass.NORTH, Value.A);
    chlorisValues.put(Compass.SOUTH, Value.A);
    chlorisValues.put(Compass.EAST, Value.A);
    chlorisValues.put(Compass.WEST, Value.A);
    hand.add(new PlayCard("Chloris", chlorisValues));

    Map<Compass, Value> txilarValues = new HashMap<>();
    txilarValues.put(Compass.NORTH, Value.SIX);
    txilarValues.put(Compass.SOUTH, Value.SIX);
    txilarValues.put(Compass.EAST, Value.TWO);
    txilarValues.put(Compass.WEST, Value.NINE);
    hand.add(new PlayCard("Txilar", txilarValues));

    player.colorHand(hand);
    mockModel.setCurrentTurn(player);
    view.render();
    String expected =
        "Player: RED" + System.lineSeparator()
        + "__" + System.lineSeparator()
        + "__" + System.lineSeparator()
        + "Hand:" + System.lineSeparator()
        + "Chloris A A A A" + System.lineSeparator()
        + "Txilar 6 6 2 9" + System.lineSeparator();
    assertEquals(expected, output.toString());
  }

  @Test
  public void testPlacedBasicDeckCards() throws IOException {
    String[] pattern = {"BB_", "_R_", "__B"
    };
    mockModel.setGrid(createGridWithPattern(pattern));

    IPlayer<PlayCard> player = createTestPlayer(PlayerColor.BLUE);
    List<PlayCard> hand = new ArrayList<>();

    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.ONE);
    values.put(Compass.SOUTH, Value.TWO);
    values.put(Compass.EAST, Value.THREE);
    values.put(Compass.WEST, Value.FOUR);
    hand.add(new PlayCard("Daneiris", values));

    player.colorHand(hand);
    mockModel.setCurrentTurn(player);
    view.render();
    String expected =
        "Player: BLUE" + System.lineSeparator()
        + "BB_" + System.lineSeparator()
        + "_R_" + System.lineSeparator()
        + "__B" + System.lineSeparator()
        + "Hand:" + System.lineSeparator()
        + "Daneiris 1 2 3 4" + System.lineSeparator();

    assertEquals(expected, output.toString());
  }
}
