package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import java.util.List;

/**
 * A strategy that mashes two strategies together.
 */
public class TwoStategies<C extends Card<C>> implements Strategy<C> {

  Strategy<C> primaryStrategy;
  Strategy<C> secondaryStrategy;

  public TwoStategies(Strategy<C> primary, Strategy<C> secondary) {
    this.primaryStrategy = primary;
    this.secondaryStrategy = secondary;
  }

  @Override
  public List<Move> chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player) {
    List<Move> primaryMoves = primaryStrategy.chooseMove(model, player);
    if (!primaryMoves.isEmpty()) {
      return primaryMoves;
    }
    return secondaryStrategy.chooseMove(model, player);
  }
}
