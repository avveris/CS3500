package cs3500.threetrios.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for our Grid Class, which represents the board for our three-trios card
 * game.  Tests edge cases and constructors.
 */
public class GridTest {
  private PlayCard card;
  private PlayCard card2;

  @Before
  public void setup() {
    Tile<PlayCard>[][] grid = new Tile[2][2];
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        if (row == 0 && col == 0) {
          grid[row][col] = new Tile<PlayCard>(true);
        } else {
          grid[row][col] = new Tile<PlayCard>(false);
        }
      }
    }
    Grid<PlayCard> gameGrid = new Grid<PlayCard>(grid);

    Map<Compass, Value> validValues = new HashMap<>();
    validValues.put(Compass.NORTH, Value.ONE);
    validValues.put(Compass.SOUTH, Value.TWO);
    validValues.put(Compass.EAST, Value.FOUR);
    validValues.put(Compass.WEST, Value.THREE);
    card = new PlayCard("Test", validValues);

    Map<Compass, Value> validValues2 = new HashMap<>();
    validValues2.put(Compass.NORTH, Value.TWO);
    validValues2.put(Compass.SOUTH, Value.THREE);
    validValues2.put(Compass.EAST, Value.ONE);
    validValues2.put(Compass.WEST, Value.FOUR);
    card2 = new PlayCard("Test2", validValues2);

    Tile<PlayCard> playableTile = new Tile<>(false);
    Tile<PlayCard> hole = new Tile<>(true);
  }

  @Test
  public void testLShapedGridCornerAdjacency() {
    Tile<PlayCard>[][] lGrid = new Tile[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        lGrid[i][j] = new Tile<>(!(i == 0 || j == 0));  // true = hole
      }
    }

    Grid<PlayCard> grid = new Grid<>(lGrid);
    Map<Compass, Cell<PlayCard>> adjacentTiles = grid.getAdjacentTiles(
            grid.getTile(1, 0));

    assertEquals(3, adjacentTiles.size());
    assertTrue(adjacentTiles.containsKey(Compass.NORTH));
    assertTrue(adjacentTiles.containsKey(Compass.SOUTH));
    assertTrue(adjacentTiles.containsKey(Compass.EAST));
  }

  @Test
  public void testMixedContentOperations() {
    Tile<PlayCard>[][] mixedGrid = new Tile[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        mixedGrid[i][j] = new Tile<>((i + j) % 2 == 0);
      }
    }
    Grid<PlayCard> mixed = new Grid<>(mixedGrid);

    int playableSpaces = mixed.getNumberOfPlayableTiles();
    card.setColor(PlayerColor.BLUE);
    card2.setColor(PlayerColor.RED);

    int blueCount = 0;
    int redCount = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (!mixed.getTile(i, j).isHole()) {
          if ((i + j) % 2 == 0) {
            mixed.getTile(i, j).playToTile(card);
            blueCount++;
          } else {
            mixed.getTile(i, j).playToTile(card2);
            redCount++;
          }
        }
      }
    }
    assertTrue(mixed.isFull());
    assertEquals(blueCount, mixed.getColorCount(PlayerColor.BLUE));
    assertEquals(redCount, mixed.getColorCount(PlayerColor.RED));
  }

  @Test
  public void testNeighborPatternVariations() {
    Tile<PlayCard>[][] xGrid = new Tile[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        xGrid[i][j] = new Tile<>(i == j || i + j == 2);
      }
    }
    Grid<PlayCard> xShaped = new Grid<>(xGrid);

    Map<Compass, Cell<PlayCard>> centerNeighbors = xShaped.getAdjacentTiles(xShaped.getTile(
            1, 1));
    assertEquals(4, centerNeighbors.size());
  }


  @Test
  public void testExtendedColorOperations() {
    Tile<PlayCard>[][] fullGrid = new Tile[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        fullGrid[i][j] = new Tile<>(false);
      }
    }
    Grid<PlayCard> colorGrid = new Grid<>(fullGrid);

    card.setColor(PlayerColor.BLUE);

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        colorGrid.getTile(i, j).playToTile(card);
      }
    }

    assertEquals(9, colorGrid.getColorCount(PlayerColor.BLUE));
    assertEquals(0, colorGrid.getColorCount(PlayerColor.RED));
  }

  // tests that neighbors are consistent
  @Test
  public void testNeighborConsistency() {
    Tile<PlayCard>[][] grid = new Tile[2][2];
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        grid[i][j] = new Tile<>(false);
      }
    }
    Grid<PlayCard> testGrid = new Grid<>(grid);

    // checking that adjacent tiles reference each other
    Cell<PlayCard> tile00 = testGrid.getTile(0, 0);
    Cell<PlayCard> tile01 = testGrid.getTile(0, 1);
    Cell<PlayCard> tile10 = testGrid.getTile(1, 0);
    Cell<PlayCard> tile11 = testGrid.getTile(1, 1);

    Map<Compass, Cell<PlayCard>> neighbors00 = testGrid.getAdjacentTiles(tile00);
    Map<Compass, Cell<PlayCard>> neighbors11 = testGrid.getAdjacentTiles(tile11);

    assertEquals(tile01, neighbors00.get(Compass.EAST));
    assertEquals(tile10, neighbors00.get(Compass.SOUTH));
  }


  // testing diagonal placement pattern
  @Test
  public void testDiagonalCards() {
    Tile<PlayCard>[][] diagGrid = new Tile[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        diagGrid[i][j] = new Tile<>(false);
      }
    }
    Grid<PlayCard> diagonal = new Grid<>(diagGrid);

    card.setColor(PlayerColor.BLUE);
    card2.setColor(PlayerColor.RED);

    // place cards diagonally
    diagonal.getTile(0, 0).playToTile(card);
    diagonal.getTile(1, 1).playToTile(card2);
    diagonal.getTile(2, 2).playToTile(card);

    assertEquals(2, diagonal.getColorCount(PlayerColor.BLUE));
    assertEquals(1, diagonal.getColorCount(PlayerColor.RED));
    assertFalse(diagonal.isFull());
  }
}