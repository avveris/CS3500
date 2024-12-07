package cs3500.threetrios.model;

/**
 * Enum to represent our cardinal directions for our Cards in our ThreeTrios game.  These
 * directions are important to the battle stages of the game.  We chose enums so they are
 * immutable and organized.
 */
public enum Compass {
  NORTH,
  SOUTH,
  EAST,
  WEST;

  /**
   * Compass for our card game, holds the directions for each card.
   * @return
   */
  public Compass flip() {
    switch (this) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case EAST:
        return WEST;
      case WEST:
        return EAST;
      default:
        return null;
    }
  }

}
