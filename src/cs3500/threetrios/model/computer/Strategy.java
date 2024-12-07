package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import java.util.List;

/**
 * A fallible strategy for ThreeTrios.
 *
 * @param <C> Type of Card.
 */
public interface Strategy<C extends Card<C>> {

  /**
   * Chooses a moved based on several kinds of implementations. These strategies can fail, if there
   * are no good positions as defined by the strategy.
   *
   * @param model  a read only version of the current model.
   * @param player who the strategy is playing for.
   * @return a list of the "best" moves with an empty list if no valid moves exist.
   */
  List<Move> chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player);
}
