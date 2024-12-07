package cs3500.threetrios.model.computer;

import java.util.Comparator;

/**
 * Sorts lists of Moves by the documentation's ruleset. First try to sort by who's the uppermost
 * position If they are equal sort by leftmost position. If they are equal and only then sort by
 * position in the hand, where closer to index 0 takes precedent.
 */
public class TieBreaker implements Comparator<Move> {

  @Override
  public int compare(Move o1, Move o2) {

    if (o1.getPosY() != o2.getPosY()) {
      return Integer.compare(o1.getPosY(), o2.getPosY());
    }
    if (o1.getPosX() != o2.getPosX()) {
      return Integer.compare(o1.getPosX(), o2.getPosX());
    }

    return Integer.compare(o1.getHandIndex(), o2.getHandIndex());
  }


}
