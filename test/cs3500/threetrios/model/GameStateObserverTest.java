package cs3500.threetrios.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import cs3500.threetrios.model.computer.Move;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * A test to check the Model's state consistency.
 */
public class GameStateObserverTest {

  private ThreeTrioModel model;
  private TestGameStateObserver testObserver;
  private IPlayer<PlayCard> redPlayer;
  private IPlayer<PlayCard> bluePlayer;

  private class TestGameStateObserver implements GameStateObserver<PlayCard> {

    private GameState lastOldState;
    private GameState lastNewState;
    private Move lastMove;
    private Map<PlayerColor, Integer> lastScores;
    private int stateChangeCount = 0;
    private int moveCount = 0;
    private int scoreChangeCount = 0;

    @Override
    public void onGameStateChanged(GameState oldState, GameState newState) {
      lastOldState = oldState;
      lastNewState = newState;
      stateChangeCount++;
    }

    @Override
    public void onMoveExecuted(Move move, IPlayer<PlayCard> player) {
      lastMove = move;
      moveCount++;
    }

    @Override
    public void onScoreChanged(Map<PlayerColor, Integer> scores) {
      lastScores = new HashMap<>(scores);
      scoreChangeCount++;
    }

    /**
     * Resets the history.
     */
    public void reset() {
      stateChangeCount = 0;
      moveCount = 0;
      scoreChangeCount = 0;
      lastOldState = null;
      lastNewState = null;
      lastMove = null;
      lastScores = null;
    }
  }

  @Before
  public void setUp() {
    model = new ThreeTrioModel(42);
    testObserver = new TestGameStateObserver();
    redPlayer = new HumanPlayer<>(model);
    bluePlayer = new HumanPlayer<>(model);
    model.addStateObserver(testObserver);
  }

  @Test
  public void testMoveNotif() {
    model.initializeGame("3_3Grid", "BasicDeck",
        false, redPlayer, bluePlayer);
    testObserver.reset();

    model.placeCard(0, 0, 0);
    assertTrue("should receive move notification", testObserver.moveCount > 0);
    assertNotNull("should have move details", testObserver.lastMove);
  }

  @Test
  public void testGameOverNotif() {
    model.initializeGame("3_3Grid", "BasicDeck",
        false, redPlayer, bluePlayer);
    testObserver.reset();

    playAllCards();
    Assert.assertTrue(model.isGameOver());

  }

  private void playAllCards() {
    int x = 0;
    int y = 0;

    boolean stillPlaying = true;
    while (stillPlaying) {
      IPlayer<PlayCard> currentPlayer = model.getTurn();
      if (currentPlayer.getHand().isEmpty()) {
        break;
      }

      boolean foundSpot = false;
      while (!foundSpot && x < model.getGridWidth()) {
        while (!foundSpot && y < model.getGridHeight()) {
          try {
            if (!model.getTile(x, y).isHole() &&
                model.getTile(x, y).getSpace() == null) {
              model.placeCard(0, y, x);
              foundSpot = true;
            }
          } catch (IllegalArgumentException | IllegalStateException e) {
            // pass
          }
          if (!foundSpot) {
            y++;
          }
        }
        if (!foundSpot) {
          x++;
          y = 0;
        }
      }
      if (!foundSpot) {
        stillPlaying = false;
      }
    }
  }

  @Test
  public void testScoreTracking() {
    model.initializeGame("3_3Grid", "BasicDeck",
        false, redPlayer, bluePlayer);
    testObserver.reset();

    model.placeCard(0, 0, 0);
    assertTrue("should receive score update", testObserver.scoreChangeCount > 0);
    assertNotNull("should have score details", testObserver.lastScores);
    assertTrue("should track both players",
        testObserver.lastScores.containsKey(PlayerColor.RED) &&
        testObserver.lastScores.containsKey(PlayerColor.BLUE));
  }

  @Test
  public void testStateTransitions() {
    model.initializeGame("3_3Grid", "BasicDeck",
        false, redPlayer, bluePlayer);
    testObserver.reset();

    GameState initialState = testObserver.lastNewState;
    model.placeCard(0, 0, 0);

    assertNotEquals("state should change after move", initialState,
        testObserver.lastNewState);
    assertTrue("should go through multiple states",
        testObserver.stateChangeCount > 1);
  }

  @Test
  public void testMultipleObservers() {
    TestGameStateObserver secondObserver = new TestGameStateObserver();
    model.addStateObserver(secondObserver);

    model.initializeGame("3_3Grid", "BasicDeck",
        false, redPlayer, bluePlayer);
    model.placeCard(0, 0, 0);

    assertEquals("both observers should receive same notifications",
        testObserver.stateChangeCount, secondObserver.stateChangeCount);
    assertEquals("both observers should see same final state",
        testObserver.lastNewState, secondObserver.lastNewState);
  }
}