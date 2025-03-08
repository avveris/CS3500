package cs3500.threetrios.model;

/**
 * A representation of a Card in the Three Trios Game. Holds information about the 4 Values, its
 * color, and it's special identifier.
 */
public interface Card<C extends Card<C>> {

  /**
   * Fetches A Card's Special Identifier.
   *
   * @return a String of the Card's Identifier.
   */
  String getName();

  /**
   * Fetches the current color of the Card.
   *
   * @return the PlayerColor of the Card.
   */
  PlayerColor getColor();

  /**
   * Sets the color of the card.
   *
   * @param color the color the card should be set too.
   * @throws IllegalArgumentException if color is null.
   * @throws IllegalArgumentException if color being set is the current color.
   */
  void setColor(PlayerColor color);

  /**
   * Fetches the value of the card of a given Compass direction.
   *
   * @param dir the given direction.
   * @return the value that matches with the given dir.
   * @throws IllegalArgumentException if dir is null.
   */
  Value getValue(Compass dir);

  C clone();

  @Override
  String toString();
}
