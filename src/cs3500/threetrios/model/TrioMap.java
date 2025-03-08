package cs3500.threetrios.model;

import java.util.Map;

/**
 * A representation of a Game Board in the Three Trios Game.
 *
 * @param <C> Type of Card.
 */
public interface TrioMap<C extends Card<C>> {

  /**
   * Given a tile returns a Compass keyed map that is of all the adjacent tiles.
   *
   * @param tile a tile on the playspace.
   * @return a mapped collection of all the adjacent tiles.
   */
  Map<Compass, Cell<C>> getAdjacentTiles(Cell<C> tile);

  /**
   * Finds the number of playable Tiles on this grid.
   *
   * @return an int of the total playable tiles.
   */
  int getNumberOfPlayableTiles();

  /**
   * Counts the cards of a specific color.
   *
   * @param color color of the cards
   * @return number of cards of said color
   * @throws IllegalArgumentException if color is null
   * @throws IllegalArgumentException if color is NONE
   */
  int getColorCount(PlayerColor color);

  /**
   * Checks if the board has no more playableSpaces available.
   *
   * @return true if all tiles are either holes or tiles with cards on them.
   */
  boolean isFull();

  /**
   * Fetches a Tile From Grid.
   *
   * @param row the row in the array.
   * @param col the col in that array.
   * @return a tile at row, col.
   * @throws IllegalArgumentException if position isn't valid
   */
  Cell<C> getTile(int row, int col);

  void flipTiles(Cell<C> startTile, PlayerColor color);

  /**
   * Fetches the grid width.
   *
   * @return the int of the width of the grid.
   */
  int getWidth();

  /**
   * Fetches the grid height.
   *
   * @return the int of the height of the grid.
   */
  int getHeight();

  /**
   * Creates a clone for our grid to prevent mutability.
   *
   * @return a clone of the Grid.
   */
  TrioMap<C> cloneGrid();
}
