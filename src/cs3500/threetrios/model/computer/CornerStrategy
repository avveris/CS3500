package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A strategy that prioritizes playing in corners, falling back to the first available spot when
 * corners are not available.
 */
public class CornerStrategy<C extends Card<C>> implements Strategy<C> {

  private static class Point {

    final int x;
    final int y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  @Override
  public List<Move> chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player) {
    List<Move> moves = new ArrayList<>();

    Point[] corners = {
        new Point(0, 0),
        new Point(0, model.getGridWidth() - 1),
        new Point(model.getGridHeight() - 1, model.getGridWidth() - 1),
        new Point(model.getGridHeight() - 1, 0)
    };

    for (Point p : corners) {
      if (isValidMove(model, p.x, p.y)) {
        moves.add(new Move(0, p.x, p.y, 1));
      }
    }
    return moves;
  }

  private boolean isValidMove(ReadOnlyTrioModel<C> model, int x, int y) {
    Cell<C> tile = model.getTile(x, y);
    return !tile.isHole() && tile.getSpace() == null;
  }
}
