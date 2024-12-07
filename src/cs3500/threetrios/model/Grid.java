package cs3500.threetrios.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the grid for the gameboard for ThreeTrios. In here we have our class invariants: - the
 * grid must be rectangular, aka four sides of any symmetrical length. - the tiles cant be null -
 * every tile must have an immutable neighbor map.
 */
public class Grid<C extends Card<C>> implements TrioMap<C> {

  private final int width;
  private final int height;
  private final Cell<C>[][] tileSpace;
  private final Map<Cell<C>, Map<Compass, Cell<C>>> neighbors;

  /**
   * creates a grid out of a 2d array of type Tile. This constructor enforces the class invariants
   * that are listed above.
   *
   * @param tileSpace the 2D array of tiles
   * @throws IllegalArgumentException if any part of the tile is null
   * @throws IllegalArgumentException if tileSpace is size 0
   */
  public Grid(Cell<C>[][] tileSpace) {
    // invariant 1 : the grid must exist and have real dimensions
    if (tileSpace == null || tileSpace.length == 0 || tileSpace[0].length == 0) {
      throw new IllegalArgumentException("grid can't be null or empty");
    }

    // invariant 2 : no null tiles
    for (int row = 0; row < tileSpace.length; row++) {
      for (int col = 0; col < tileSpace[row].length; col++) {
        if (tileSpace[row][col] == null) {
          throw new IllegalArgumentException("grid can't contain null tile");
        }
      }
    }

    height = tileSpace.length;
    width = tileSpace[0].length;
    this.tileSpace = new Tile[height][width];

    for (int row = 0; row < height; row++) {
      System.arraycopy(tileSpace[row], 0, this.tileSpace[row], 0, width);
    }

    // invariants 3 : Create immutable neighbor mappings
    this.neighbors = createNeighbors();

    // Verify all invariants are satisfied after construction
    if (!isGridCorrectHuh()) {
      throw new IllegalStateException("grid invariants aren't correct");
    }
  }

  /**
   * creates an immutable map of each tile's neighbors. This method helps maintain invariant 3.
   */
  private Map<Cell<C>, Map<Compass, Cell<C>>> createNeighbors() {
    Map<Cell<C>, Map<Compass, Cell<C>>> createList = new HashMap<>();
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Map<Compass, Cell<C>> currentAdj = new HashMap<>();
        if (row - 1 >= 0) {
          currentAdj.put(Compass.NORTH, tileSpace[row - 1][col]);
        }
        if (row + 1 < height) {
          currentAdj.put(Compass.SOUTH, tileSpace[row + 1][col]);
        }
        if (col - 1 >= 0) {
          currentAdj.put(Compass.WEST, tileSpace[row][col - 1]);
        }
        if (col + 1 < width) {
          currentAdj.put(Compass.EAST, tileSpace[row][col + 1]);
        }
        createList.put(tileSpace[row][col], Collections.unmodifiableMap(currentAdj));
      }
    }
    return Collections.unmodifiableMap(createList);
  }

  /**
   * Checks if the tiles are correctly made in the grid space. This method verifies invariants 1 and
   * 2 are maintained.
   */
  private boolean isGridCorrectHuh() {
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        if (tileSpace[row][col] == null) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Map<Compass, Cell<C>> getAdjacentTiles(Cell<C> tile) {
    Map<Compass, Cell<C>> copyTile = new HashMap<>();

    Compass[] dir = Compass.values();
    for (Compass c : dir) {
      if (neighbors.get(tile).containsKey(c)) {
        copyTile.put(c, neighbors.get(tile).get(c));
      }
    }
    return copyTile;
  }


  @Override
  public Cell<C> getTile(int row, int col) {
    if (row < 0 || col < 0 || row >= height || col >= width) {
      throw new IllegalArgumentException("invalid position!!");
    }
    return tileSpace[row][col];
  }


  @Override
  public boolean isFull() {
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        if (!tileSpace[row][col].isHole() && tileSpace[row][col].getSpace() == null) {
          return false;
        }
      }
    }
    return true;
  }


  @Override
  public int getColorCount(PlayerColor color) {
    if (color == null) {
      throw new IllegalArgumentException("color can't be null");
    }
    if (color == PlayerColor.NONE) {
      throw new IllegalArgumentException("There should be no NONE colors while game has started");
    }

    int count = 0;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Cell<C> tile = tileSpace[row][col];
        if (!tile.isHole() && tile.getSpace() != null && tile.getSpace().getColor() == color) {
          count++;
        }
      }
    }
    return count;
  }


  @Override
  public int getNumberOfPlayableTiles() {
    int count = 0;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        if (!tileSpace[row][col].isHole()) {
          count++;
        }
      }
    }
    return count;
  }


  @Override
  public int getWidth() {
    return width;
  }


  @Override
  public int getHeight() {
    return height;
  }


  @Override
  public TrioMap<C> cloneGrid() {
    Cell<C>[][] copiedTiles = new Tile[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Cell<C> originalTile = tileSpace[row][col];

        copiedTiles[row][col] = new Tile<>(originalTile.isHole());

        if (originalTile.getSpace() != null) {

          copiedTiles[row][col].playToTile(originalTile.getSpace().clone());
        }
      }
    }
    return new Grid<>(copiedTiles);
  }

  @Override
  public void flipTiles(Cell<C> startTile, PlayerColor color) {
    List<Cell<C>> battleList = new ArrayList<>();
    battleList.add(startTile);

    Compass[] dir = Compass.values();
    while (!battleList.isEmpty()) {
      Cell<C> battleTile = battleList.get(0);
      battleList.remove(0);

      Map<Compass, Cell<C>> neighbors = getAdjacentTiles(battleTile);
      for (Compass c : dir) {
        if (neighbors.get(c) == null || neighbors.get(c).getSpace() == null || neighbors.get(c)
            .isHole() || neighbors.get(c).getSpace().getColor() == color) {
          continue;
        }

        Compass flip = c.flip();
        if (battleTile.getSpace().getValue(c).toInteger() > neighbors.get(c).getSpace()
            .getValue(flip).toInteger()) {
          PlayerColor flipColor = battleTile.getSpace().getColor();
          neighbors.get(c).getSpace().setColor(flipColor);
          battleList.add(neighbors.get(c));
        }
      }
    }

  }
}