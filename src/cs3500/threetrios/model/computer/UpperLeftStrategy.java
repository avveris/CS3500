package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;

/**
 * A strategy with only one valid move, it will always return a valid move or throw otherwise.
 *
 * @param <C> type of Card
 */
public class UpperLeftStrategy<C extends Card<C>> implements InfallableStrategy<C> {

  @Override
  public Move chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player)
      throws IllegalStateException {
    if (model.isGameOver()) {
      throw new IllegalStateException("There is no valid move because the game over.");
    }

    for (int row = 0; row < model.getGridHeight(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.getTile(col, row).getSpace() == null) {
          return (new Move(0, col, row, 0));
        }
      }
    }

    throw new IllegalStateException("There is no valid moves");
  }
}
