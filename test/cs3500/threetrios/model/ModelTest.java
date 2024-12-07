package cs3500.threetrios.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Class for our Game Model, tests the implementation of the different pieces of
 * the model's interaction, which seperates it from the other test classes.  This tests the
 * constructors of the model, the methods themsleves, and the gamestate as a result of
 * actions.
 */
public class ModelTest {
  private ThreeTrioModel model;

  @Before
  public void setup() {
    model = new ThreeTrioModel(42);
  }

  private PlayCard createCard(String name, int north, int south, int east, int west) {
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, getValue(north));
    values.put(Compass.SOUTH, getValue(south));
    values.put(Compass.EAST, getValue(east));
    values.put(Compass.WEST, getValue(west));
    return new PlayCard(name, values);
  }

  private Value getValue(int num) {
    switch (num) {
      case 1:
        return Value.ONE;
      case 2:
        return Value.TWO;
      case 3:
        return Value.THREE;
      case 4:
        return Value.FOUR;
      case 5:
        return Value.FIVE;
      case 6:
        return Value.SIX;
      case 7:
        return Value.SEVEN;
      case 8:
        return Value.EIGHT;
      case 9:
        return Value.NINE;
      default:
        return Value.A;
    }
  }

  private Tile<PlayCard>[][] createBasicGrid() {
    Tile<PlayCard>[][] tiles = new Tile[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        tiles[i][j] = new Tile<>(i == 1 && j == 1); // Middle tile is a hole
      }
    }
    return tiles;
  }


  // tests that getting a grid before initialization throws exception
  @Test
  public void testGetGridBeforeInit() {
    try {
      model.getGrid();
      fail("you can't have a grid without a game, should have thrown IllegalStateException");
    } catch (IllegalStateException e) {
      // test passes
    }
  }

  // tests that placing card before initialization will throw exception
  @Test
  public void testPlaceBeforeInit() {
    try {
      model.placeCard(0, 0, 0);
      fail("you can't  place a card with no game, should have thrown IllegalStateException");
    } catch (IllegalStateException e) {
      // test passes
    }
  }

  // tests that having a turn before game initialization will throw exception
  @Test
  public void testTurnBeforeInit() {
    try {
      model.getTurn();
      fail("you can't have a turn without a game, should have thrown IllegalStateException");
    } catch (IllegalStateException e) {
      // Test passes
    }
  }

  // tests that checking game over before game initialization throws exception
  @Test
  public void testGameOverE() {
    try {
      model.isGameOver();
      fail("game can't be over before game starts, should have thrown an IllegalStateException");
    } catch (IllegalStateException e) {
      // Test passes
    }
  }

  // tests that getting winner before game initialization will throw an exception
  @Test
  public void testNoWinnerBeforeInit() {
    try {
      model.getWinner();
      fail("you can't have a winner with no game, should have thrown ISE");
    } catch (IllegalStateException e) {
      // Test passes
    }
  }


  // tests initialization of tile state is correct
  @Test
  public void testInitState() {
    Tile<PlayCard> tile = new Tile<>(false);
    assertFalse(tile.isHole());
    assertFalse(tile.hasCard());
    assertNull(tile.getSpace());
  }

  // tests hole tile state is correct
  @Test
  public void testHoleState() {
    Tile<PlayCard> tile = new Tile<>(true);
    assertTrue(tile.isHole());
    assertFalse(tile.hasCard());
  }

  // tests valid card placement works correctly
  @Test
  public void testCardPlacement() {
    Tile<PlayCard> tile = new Tile<>(false);
    PlayCard card = createCard("test", 5, 5, 5, 5);
    tile.playToTile(card);
    assertTrue(tile.hasCard());
    assertEquals(card, tile.getSpace());
  }

  // tests that placing a null card throws exception
  @Test
  public void testNullCardPlace() {
    Tile<PlayCard> tile = new Tile<>(false);
    try {
      tile.playToTile(null);
      fail("you can't place a null card, should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passes
    }
  }

  // tests that placing a card on an occupied tile throws exception
  @Test
  public void testOccupiedTile() {
    Tile<PlayCard> tile = new Tile<>(false);
    PlayCard card = createCard("test", 5, 5, 5, 5);
    tile.playToTile(card);
    try {
      tile.playToTile(card);
      fail("you can't place a card on a taken tile, should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passes
    }
  }

  // tests that placing a card on a hole tile throws exception
  @Test
  public void testCardInHole() {
    Tile<PlayCard> tile = new Tile<>(true);
    PlayCard card = createCard("test", 5, 5, 5, 5);
    try {
      tile.playToTile(card);
      fail("you can't place a card in hole, should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passes
    }
  }

  // tests card creation with valid values works correctly
  @Test
  public void testCardMaker() {
    PlayCard card = createCard("test", 1, 2, 3, 4);
    assertEquals("test", card.getName());
    assertEquals(Value.ONE, card.getValue(Compass.NORTH));
    assertEquals(Value.TWO, card.getValue(Compass.SOUTH));
    assertEquals(Value.THREE, card.getValue(Compass.EAST));
    assertEquals(Value.FOUR, card.getValue(Compass.WEST));
  }

  // tests that creating a card with null throws exception
  @Test
  public void testCardNull() {
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.ONE);
    values.put(Compass.SOUTH, Value.ONE);
    values.put(Compass.EAST, Value.ONE);
    values.put(Compass.WEST, Value.ONE);
    try {
      new PlayCard(null, values);
      fail("can't have a null card,should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passes
    }
  }

  // tests that creating a card with null map throws exception
  @Test
  public void testNullMap() {
    try {
      new PlayCard("test", null);
      fail("you can't have a card with null value, should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passes
    }
  }

  // tests that creating a card with incomplete value map will throw an exception
  @Test
  public void testCardOneValue() {
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.ONE);
    try {
      new PlayCard("test", values);
      fail("you need four values, should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passes
    }
  }

  @Test
  public void testInvalidColorSetting() {
    PlayCard card = createCard("test", 5, 5, 5, 5);

    try {
      card.setColor(PlayerColor.NONE);
      fail("Should throw IAE for NONE color");
    } catch (IllegalArgumentException e) {
      // pass
    }

    try {
      card.setColor(null);
      fail("Should throw IAE for null color");
    } catch (IllegalArgumentException e) {
      //pass
    }
  }

  // tests color counting works correctly
  @Test
  public void testGridColorCount() {
    Grid<PlayCard> grid = new Grid<>(createBasicGrid());
    PlayCard redCard = createCard("red", 5, 5, 5, 5);
    redCard.setColor(PlayerColor.RED);
    PlayCard blueCard = createCard("blue", 5, 5, 5, 5);
    blueCard.setColor(PlayerColor.BLUE);

    grid.getTile(0, 0).playToTile(redCard);
    grid.getTile(0, 1).playToTile(blueCard);

    assertEquals(1, grid.getColorCount(PlayerColor.RED));
    assertEquals(1, grid.getColorCount(PlayerColor.BLUE));
  }

  // tests that counting a null color throws exception
  @Test
  public void testCountColorNull() {
    Grid<PlayCard> grid = new Grid<>(createBasicGrid());
    try {
      grid.getColorCount(null);
      fail("color can't be null, should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passes
    }
  }

  // tests battle mechanics with equal card values,,, a tie
  @Test
  public void testBattleTie() {
    Grid<PlayCard> grid = new Grid<>(createBasicGrid());
    PlayCard card1 = createCard("card1", 5, 5, 5, 5);
    card1.setColor(PlayerColor.RED);
    PlayCard card2 = createCard("card2", 5, 5, 5, 5);
    card2.setColor(PlayerColor.BLUE);

    grid.getTile(0, 0).playToTile(card1);
    grid.getTile(0, 1).playToTile(card2);

    assertEquals(PlayerColor.RED, grid.getTile(0, 0).getSpace().getColor());
    assertEquals(PlayerColor.BLUE, grid.getTile(0, 1).getSpace().getColor());
  }


  // Verifies that accessing invalid grid position throws exception
  @Test
  public void testInvalidPosition() {
    Grid<PlayCard> grid = new Grid<>(createBasicGrid());
    try {
      grid.getTile(-1, 0);
      fail("invalid grid posn, should have thrown IAE");
    } catch (IllegalArgumentException e) {
      // test passes
    }
  }

  // tests a situation with battle in multiple directions
  @Test
  public void testBattleMultiDirections() {
    Grid<PlayCard> grid = new Grid<>(createBasicGrid());

    PlayCard winnerCard = createCard("winner", 9, 9, 9, 9);
    winnerCard.setColor(PlayerColor.RED);

    PlayCard loserCard1 = createCard("loser1", 1, 1, 1, 1);
    loserCard1.setColor(PlayerColor.BLUE);
    PlayCard loserCard2 = createCard("loser2", 1, 1, 1, 1);
    loserCard2.setColor(PlayerColor.BLUE);
    PlayCard loserCard3 = createCard("loser3", 1, 1, 1, 1);
    loserCard3.setColor(PlayerColor.BLUE);

    grid.getTile(0, 1).playToTile(loserCard1);
    grid.getTile(1, 0).playToTile(loserCard2);
    grid.getTile(1, 2).playToTile(loserCard3);
    grid.getTile(0, 0).playToTile(winnerCard);

    assertEquals(PlayerColor.BLUE, grid.getTile(0, 1).getSpace().getColor());
  }

  // tests the directional preference
  @Test
  public void testDirectionPref() {
    Grid<PlayCard> grid = new Grid<>(createBasicGrid());

    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.NINE);
    values.put(Compass.SOUTH, Value.ONE);
    values.put(Compass.EAST, Value.ONE);
    values.put(Compass.WEST, Value.ONE);
    PlayCard directionalCard = new PlayCard("directional", values);
    directionalCard.setColor(PlayerColor.RED);

    PlayCard northCard = createCard("north", 2, 8, 5, 5);
    northCard.setColor(PlayerColor.BLUE);
    PlayCard southCard = createCard("south", 8, 2, 5, 5);
    southCard.setColor(PlayerColor.BLUE);

    grid.getTile(0, 1).playToTile(northCard);
    grid.getTile(2, 1).playToTile(southCard);

    assertEquals(PlayerColor.BLUE, grid.getTile(2, 1).getSpace().getColor());
  }

  // tests for the tie-breaker card
  @Test
  public void testTieBreaker() {
    Grid<PlayCard> grid = new Grid<>(createBasicGrid());

    PlayCard redCard1 = createCard("red1", 5, 5, 5, 5);
    PlayCard redCard2 = createCard("red2", 5, 5, 5, 5);
    PlayCard blueCard1 = createCard("blue1", 5, 5, 5, 5);
    PlayCard blueCard2 = createCard("blue2", 5, 5, 5, 5);

    redCard1.setColor(PlayerColor.RED);
    redCard2.setColor(PlayerColor.RED);
    blueCard1.setColor(PlayerColor.BLUE);
    blueCard2.setColor(PlayerColor.BLUE);

    grid.getTile(0, 0).playToTile(redCard1);
    grid.getTile(0, 1).playToTile(blueCard1);
    grid.getTile(1, 0).playToTile(redCard2);

    assertEquals(2, grid.getColorCount(PlayerColor.RED));
  }
}