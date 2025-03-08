package cs3500.threetrios.model;

import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.model.computer.InfallableStrategy;
import cs3500.threetrios.model.computer.Move;

/**
 * A non-human Player that uses a strategy to make its move.
 *
 * @param <C> type of Card.
 */
public class MachinePlayer<C extends Card<C>> extends AbstractPlayer<C> {

  private final InfallableStrategy<C> strategy;

  /**
   * Constructs the machine with the model to base its moves off of and a strategy to play its cards
   * with.
   *
   * @param model    the given Read only version of the model.
   * @param strategy the given strategy for it to implement.
   */
  public MachinePlayer(ReadOnlyTrioModel<C> model, InfallableStrategy<C> strategy) {
    super(model);
    if (strategy == null) {
      throw new IllegalArgumentException("Strategy cannot be null");
    }
    this.strategy = strategy;
  }


  @Override
  public void callMove() {
    System.out.println("MachinePlayer: callMove() called");
    Move move = strategy.chooseMove(model, this);
    System.out.println("MachinePlayer: Strategy chose move - card: " + move.getHandIndex() +
                       " position: (" + move.getPosX() + "," + move.getPosY() + ")");
    for (PlayerAction observer : listeners) {
      System.out.println("MachinePlayer: Notifying observer of move");
      observer.handleMove(move);
    }
  }
}
