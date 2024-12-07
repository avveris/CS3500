package cs3500.threetrios.model;

import cs3500.threetrios.model.computer.Move;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * A class to keep track of the moves made by a model.
 *
 * @param <C> type of card.
 */
public class MoveHistory<C extends Card<C>> {

  private final Stack<Move> moves;
  private final Stack<GameState> states;
  private final Stack<Map<PlayerColor, Integer>> scores;
  private final List<GameStateObserver<C>> observers;
  private final int maxHistorySize;

  /**
   * Constructs a MoveHistory with maxSize it will store.
   *
   * @param maxHistorySize a number that defines the max history stored.
   */
  public MoveHistory(int maxHistorySize) {
    this.maxHistorySize = maxHistorySize;
    this.moves = new Stack<>();
    this.states = new Stack<>();
    this.scores = new Stack<>();
    this.observers = new ArrayList<>();
  }

  /**
   * Records a move onto the History. If the history is full it will remove the first item added.
   *
   * @param move          the move made.
   * @param state         the state of the game.
   * @param currentScores the current score.
   */
  public void recordMove(Move move, GameState state, Map<PlayerColor, Integer> currentScores) {
    if (moves.size() >= maxHistorySize) {
      moves.remove(0);
      states.remove(0);
      scores.remove(0);
    }

    moves.push(move);
    states.push(state);
    scores.push(new HashMap<>(currentScores));

    notifyObservers(move, state, currentScores);
  }

  /**
   * Gives the Record of the previous turn.
   *
   * @return a MoveRecord of the turn before the current.
   */
  public MoveRecord undoLastMove() {
    if (moves.isEmpty()) {
      return null;
    }

    Move lastMove = moves.pop();
    GameState previousState = states.pop();
    Map<PlayerColor, Integer> previousScores = scores.pop();

    return new MoveRecord(lastMove, previousState, previousScores);
  }

  /**
   * Adds a listener to the class.
   *
   * @param observer the listener.
   */
  public void addObserver(GameStateObserver<C> observer) {
    observers.add(observer);
  }

  /**
   * Notifies all listeners.
   *
   * @param move          the given move.
   * @param state         the given state.
   * @param currentScores the current score.
   */
  private void notifyObservers(Move move, GameState state,
      Map<PlayerColor, Integer> currentScores) {
    GameState previousState = states.size() > 1 ? states.get(states.size() - 2) : null;

    for (GameStateObserver<C> observer : observers) {
      if (previousState != null) {
        observer.onGameStateChanged(previousState, state);
      }
      observer.onScoreChanged(currentScores);
    }
  }

  /**
   * Gives a list of all the moves made.
   *
   * @returnn a list of the history.
   */
  public List<Move> getMoveHistory() {
    return new ArrayList<>(moves);
  }
}