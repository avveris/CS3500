package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import java.util.List;

/**
 * Will break ties on Strategies to give a single move. Breaks ties first on distance to 0,0 on a
 * grid (Top Left position). Then by how close the card is to the left most point on the hand (index
 * 0).
 *
 * @param <C> Type of Card
 */
public class SingleMove<C extends Card<C>> implements InfallableStrategy<C> {

  Strategy<C> strategyToTry;

  public SingleMove(Strategy<C> strategyToTry) {
    this.strategyToTry = strategyToTry;
  }

  @Override
  public Move chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player)
      throws IllegalStateException {
    List<Move> possibilities = strategyToTry.chooseMove(model, player);
    if (possibilities.isEmpty()) {
      return new UpperLeftStrategy<C>().chooseMove(model, player);
    }
    if (possibilities.size() == 1) {
      return possibilities.get(0);
    }

    possibilities.sort(new TieBreaker());
    return possibilities.get(0);
  }
}
