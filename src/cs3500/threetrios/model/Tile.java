package cs3500.threetrios.model;

/**
 * An implementation of Cell for a ThreeTrios Game.
 *
 * @param <C> type of Card.
 */
public class Tile<C extends Card<C>> implements Cell<C> {

  //Defines if this tile is a playable space or a hole
  private final boolean isHole;
  //Defines the Card on this tile, null means empty tile
  private C space;

  /**
   * Constructs a Tile based on if it's a placeable tile and its adjacent tiles.
   *
   * @param isHole defines if the tile will be a playable space.
   */
  public Tile(boolean isHole) {
    this.isHole = isHole;
  }

  @Override
  public void playToTile(C card) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    if (space != null) {
      throw new IllegalArgumentException("Card already occupied");
    }
    if (isHole) {
      throw new IllegalArgumentException("Tile is hole");
    }
    space = card;
  }

  @Override
  public C getSpace() {
    return space;
  }

  @Override
  public boolean isHole() {
    return isHole;
  }

  @Override
  public String toString() {
    if (isHole) {
      return "Hole" + ", " + hashCode();
    }
    return "Playable Tile: " + space + ", " + hashCode();
  }

  @Override
  public boolean hasCard() {
    return space != null;
  }

}
