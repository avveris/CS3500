package cs3500.threetrios.model;

import cs3500.threetrios.model.computer.Move;
import java.util.HashMap;
import java.util.Map;

/**
 * A MoveRecord is the dataclass that holds the score, state, and move at a time in the game.
 */
public class MoveRecord {

  private final Move move;
  private final GameState state;
  private final Map<PlayerColor, Integer> scores;

  /**
   * Constructs a record.
   *
   * @param move   the move.
   * @param state  the state.
   * @param scores the current scores.
   */
  public MoveRecord(Move move, GameState state, Map<PlayerColor, Integer> scores) {
    this.move = move;
    this.state = state;
    this.scores = new HashMap<>(scores);
  }

  /**
   * Yields the Move.
   *
   * @return a Move.
   */
  public Move getMove() {
    return move;
  }

  /**
   * Yields the state.
   *
   * @return the gameState
   */
  public GameState getState() {
    return state;
  }

  /**
   * The scores at that time in the game.
   *
   * @return the scores.
   */
  public Map<PlayerColor, Integer> getScores() {
    return new HashMap<>(scores);
  }
}