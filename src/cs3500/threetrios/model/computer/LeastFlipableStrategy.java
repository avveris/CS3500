package cs3500.threetrios.model.computer;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import cs3500.threetrios.model.TrioMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An implementation of Strategy where its bias is to play Cards that are unlikely to be flipped.
 * Score is in Move is determined by the number of cards the opponent has multiplied by four and
 * then subtracted by each time it could get flipped.
 *
 * @param <C> Type of Card.
 */
public class LeastFlipableStrategy<C extends Card<C>> implements Strategy<C> {

  @Override
  public List<Move> chooseMove(ReadOnlyTrioModel<C> model, IPlayer<C> player) {
    List<C> playCards = model.getPlayerHand(player);
    IPlayer<C> oppositePlayer =
        player == model.getRedPlayer() ? model.getBluePlayer() : model.getRedPlayer();
    List<C> opponentCards = model.getPlayerHand(oppositePlayer);

    List<Move> leastFlipableMoves = new ArrayList<>();

    int highestScore = -1;
    for (int cardIndex = 0; cardIndex < playCards.size(); cardIndex++) {
      List<Move> potentialMoves = scoreHandPositions(model, cardIndex, player, oppositePlayer);
      if (potentialMoves.isEmpty()) {
        continue;
      }
      if (potentialMoves.get(0).getScore() > highestScore) {
        leastFlipableMoves.clear();
        highestScore = potentialMoves.get(0).getScore();
        leastFlipableMoves.addAll(potentialMoves);
      } else if (potentialMoves.get(0).getScore() == highestScore) {
        leastFlipableMoves.addAll(potentialMoves);
      }
    }
    return leastFlipableMoves;
  }

  private List<Move> scoreHandPositions(ReadOnlyTrioModel<C> model, int cardIndex,
      IPlayer<C> player,
      IPlayer<C> opponent) {
    List<Move> bestSpotsForCard = new ArrayList<>();
    int highestScore = -1;
    for (int row = 0; row < model.getGridHeight(); row++) {
      for (int col = 0; col < model.getGridWidth(); col++) {
        if (model.getTile(col, row).getSpace() != null) {
          continue;
        }
        //Create a hypothetical board with this card placed here.
        TrioMap<C> hypoBoard = model.getGrid();
        int potentialScore = 4 * model.getPlayerHand(opponent).size();

        C card = model.getPlayerHand(player).get(cardIndex);
        hypoBoard.getTile(row, col).playToTile(card);
        Map<Compass, Cell<C>> neighbors = hypoBoard.getAdjacentTiles(hypoBoard.getTile(row, col));
        Compass[] dir = Compass.values();
        for (Compass c : dir) {
          if (neighbors.get(c) == null || neighbors.get(c).getSpace() != null) {
            continue;
          }
          for (int oppCard = 0; oppCard < model.getPlayerHand(opponent).size(); oppCard++) {
            C compareCard = model.getPlayerHand(opponent).get(oppCard);
            if (card.getValue(c).toInteger() < compareCard.getValue(c.flip()).toInteger()) {
              potentialScore--;
            }
          }
        }
        if (potentialScore > highestScore) {
          bestSpotsForCard.clear();
          highestScore = potentialScore;
          bestSpotsForCard.add(new Move(cardIndex, col, row, potentialScore));
        } else if (potentialScore == highestScore) {
          bestSpotsForCard.add(new Move(cardIndex, col, row, potentialScore));
        }
      }
    }
    return bestSpotsForCard;
  }
}
