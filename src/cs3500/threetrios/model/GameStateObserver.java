package cs3500.threetrios.model;

import cs3500.threetrios.model.computer.Move;
import java.util.Map;

/**
 * Observes the current game state of a model.
 *
 * @param <C> type of card.
 */
public interface GameStateObserver<C extends Card<C>> {

  /**
   * Called when the game state changes from one state to another.
   *
   * @param oldState the previous game state
   * @param newState the new game state
   */
  void onGameStateChanged(GameState oldState, GameState newState);

  /**
   * Called when a move is executed by a player.
   *
   * @param move   the move that was executed
   * @param player the player who made the move
   */
  void onMoveExecuted(Move move, IPlayer<C> player);

  /**
   * Called when the game scores change.
   *
   * @param scores map of player colors to their current scores
   */
  void onScoreChanged(Map<PlayerColor, Integer> scores);
}