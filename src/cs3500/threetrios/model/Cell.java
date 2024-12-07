package cs3500.threetrios.model;

/**
 * Represents a position on the game space. A tile is either a hole or a playable space Once a card
 * is placed on a Tile it can never change or be edited.
 */
public interface Cell<C extends Card<C>> {

  /**
   * Sets the card to this tile.
   *
   * @param card card to place
   * @throws IllegalArgumentException if card is null.
   * @throws IllegalArgumentException if tile is occupied.
   * @throws IllegalArgumentException if tile is a hole
   */
  void playToTile(C card);

  /**
   * Gets the current Card on this space, null if the Cell is empty.
   *
   * @return a Card on this Cell.
   */
  C getSpace();

  /**
   * Yields if this tile is playable or a hole.
   *
   * @return true if hole.
   */
  boolean isHole();

  /**
   * Yields if the Cell holds a card.
   *
   * @return true if there is a card on this space.
   */
  boolean hasCard();
}
