package cs3500.threetrios.model;

import cs3500.threetrios.controller.PlayerAction;
import java.util.List;

/**
 * A Player is a type of object that can decide moves for a ThreeTrios game.
 *
 * @param <C> the type of Card being used.
 */
public interface IPlayer<C extends Card<C>> {

  /**
   * Given a list of cards it will color the cards and add them to this objects hand.
   *
   * @param hand the list given.
   * @throws IllegalStateException    cannot be called on if a player already has a hand.
   * @throws IllegalArgumentException cannot be given a null hand.
   */
  void colorHand(List<C> hand);

  /**
   * Takes the card from a Players hand removing it from the List as well.
   *
   * @param index card's position in the hand
   * @return card based on its position
   * @throws IllegalArgumentException if index is invalid
   */
  C takeCard(int index);

  /**
   * Gets a copy of the list of a hand.
   */
  List<C> getHand();

  /**
   * Retrieves the color of Player.
   *
   * @return the assigned color.
   */
  PlayerColor getColor();

  /**
   * Assigns a color to the Player.
   *
   * @param color type of color being assigned.
   * @throws IllegalArgumentException if color is null.
   * @throws IllegalStateException    if method is called more than once.
   */
  void setColor(PlayerColor color);

  /**
   * Adds a listener to a Player for Player based emission.
   *
   * @param action the listener to be added and called based on an event.
   * @throws IllegalArgumentException if action is null
   */
  void addListener(PlayerAction action);

  /**
   * Tells that the player wants to play a move, will only be used by non-human players, since human
   * players interact through GUI.
   */
  void callMove();
}
