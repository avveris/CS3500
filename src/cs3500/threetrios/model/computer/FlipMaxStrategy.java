package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import java.util.ArrayList;
import java.util.List;


/**
 * A strategy where you play to the tile that will flip the most amount of cards.
 *
 * @param <C> the type of Card being played.
 */
public class FlipMaxStrategy<C extends Card<C>> implements Strategy<C> {

  @Override
  public List<Move> chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player) {
    List<C> hand = model.getPlayerHand(player);
    List<Move> bestMoves = new ArrayList<>();
    int highestCardIndex = 0;
    int currentHighestFlip = -1;
    for (int cardInHand = 0; cardInHand < hand.size(); cardInHand++) {
      List<Move> bestPosForCard = cardsBestPositions(hand.get(cardInHand), model, player,
          cardInHand);
      if (bestPosForCard.isEmpty()) {
        continue;
      }
      if (bestPosForCard.get(0).getScore() > currentHighestFlip) {
        currentHighestFlip = bestPosForCard.get(0).getScore();
        bestMoves.clear();
        bestMoves.addAll(bestPosForCard);

      } else if (bestPosForCard.get(0).getScore() == currentHighestFlip) {
        bestMoves.addAll(bestPosForCard);
      }
    }

    return bestMoves;
  }

  private List<Move> cardsBestPositions(C card, ReadOnlyTrioModel<C> model, IPlayer<C> player,
      int handIndex) {
    int highestScore = -1;
    List<Move> currentBestPositions = new ArrayList<>();
    for (int row = 0; row < model.getGridHeight(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        Cell<C> tile = model.getTile(row, col);
        if (tile.getSpace() != null || tile.isHole()) {
          continue;
        }
        int currentFlip = model.getFlipTotal(player, card, col, row);
        if (currentFlip > highestScore) {
          currentBestPositions.clear();
          currentBestPositions.add(new Move(handIndex, col, row, currentFlip));
          highestScore = currentFlip;
        } else if (currentFlip == highestScore) {
          currentBestPositions.add(new Move(handIndex, col, row, currentFlip));
        }
      }
    }
    return currentBestPositions;
  }

}
