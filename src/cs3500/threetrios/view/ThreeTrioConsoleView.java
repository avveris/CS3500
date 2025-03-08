package cs3500.threetrios.view;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import cs3500.threetrios.model.TrioMap;
import java.io.IOException;
import java.util.List;

/**
 * Implements a view for Three Trio viewable on the console line.
 */
public class ThreeTrioConsoleView<C extends Card<C>> implements ThreeTrioView {

  private static final String HOLES = " ";
  private static final String EMPTY_CELL = "_";
  private static final String RED = "R";
  private static final String BLUE = "B";

  private Appendable appendable;
  private final ReadOnlyTrioModel<?> model;

  /**
   * Prevents null arguments from existing in the constructed view. param model param appendable
   */
  public ThreeTrioConsoleView(ReadOnlyTrioModel<?> model, Appendable appendable) {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Can't have null arguments in Constructing View.");
    }
    this.model = model;
    this.appendable = appendable;
  }

  public ThreeTrioConsoleView(ReadOnlyTrioModel<?> model) {
    this.model = model;
  }

  @Override
  public void render() throws IOException {
    if (appendable == null) {
      throw new IllegalArgumentException("Appendable object is null.");
    }
    appendable.append(toString());
  }

  @Override
  public String toString() {
    String sb = "Player: " +
                model.getTurn().getColor().toString() +
                System.lineSeparator() +
                translateGrid(model.getGrid()) +
                translateHand((IPlayer<C>) model.getTurn());
    return sb;
  }

  private String translateGrid(TrioMap<?> grid) {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < grid.getHeight(); row++) {
      for (int col = 0; col < grid.getWidth(); col++) {
        if (grid.getTile(row, col).isHole()) {
          sb.append(HOLES);
        } else {
          if (grid.getTile(row, col).getSpace() != null) {
            if (grid.getTile(row, col).getSpace().getColor() == PlayerColor.RED) {
              sb.append(RED);
            } else {
              sb.append(BLUE);
            }
          } else {
            sb.append(EMPTY_CELL);
          }
        }
      }
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }

  private String translateHand(IPlayer<C> player) {
    StringBuilder sb = new StringBuilder();
    sb.append("Hand:");
    sb.append(System.lineSeparator());
    List<?> hand = player.getHand();
    for (int elem = 0; elem < hand.size(); elem++) {
      C card = (C) hand.get(elem);
      sb.append(card.getName());
      sb.append(" ");
      sb.append(card.getValue(Compass.NORTH));
      sb.append(" ");
      sb.append(card.getValue(Compass.SOUTH));
      sb.append(" ");
      sb.append(card.getValue(Compass.EAST));
      sb.append(" ");
      sb.append(card.getValue(Compass.WEST));
      sb.append(System.lineSeparator());
    }
    return sb.toString();
  }

}
