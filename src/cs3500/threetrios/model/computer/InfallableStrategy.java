package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;

/**
 * A combination of strategies that will always produce a single move.
 *
 * @param <C> Type of Card.
 */
public interface InfallableStrategy<C extends Card<C>> {

  /**
   * Chooses a move based off a given implementation of the strategy.
   *
   * @param model  the given current state of the game.
   * @param player the player they are taking the perspective of.
   * @return A move that the strategy thinks a given player should make.
   * @throws IllegalStateException if there is a null move of any kind.
   */
  Move chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player) throws IllegalStateException;
}
