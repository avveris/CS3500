package cs3500.threetrios.model;

/**
 * Defines a HumanPlayer and it's available actions.
 *
 * @param <C> type of Card.
 */
public class HumanPlayer<C extends Card<C>> extends AbstractPlayer<C> {

  /**
   * Constructs a Human Player.
   *
   * @param model the given model the player will be tied to.
   */
  public HumanPlayer(ReadOnlyTrioModel<C> model) {
    super(model);
  }


  @Override
  public void callMove() {
    //Do nothing
  }
}
