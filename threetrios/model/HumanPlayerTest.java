package cs3500.threetrios.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.model.computer.Move;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for HumanPlayer implementation.
 */
public class HumanPlayerTest {

  private HumanPlayer<PlayCard> humanPlayer;
  private List<PlayCard> testHand;

  private class MockModel implements ReadOnlyTrioModel<PlayCard> {

    @Override
    public TrioMap<PlayCard> getGrid() {
      return null;
    }

    @Override
    public Cell<PlayCard> getTile(int x, int y) {
      return null;
    }

    @Override
    public int getGridHeight() {
      return 3;
    }

    @Override
    public int getGridWidth() {
      return 3;
    }

    @Override
    public List<PlayCard> getPlayerHand(IPlayer<PlayCard> player) {
      return new ArrayList<>();
    }

    @Override
    public IPlayer<PlayCard> getTurn() {
      return humanPlayer;
    }

    @Override
    public boolean isGameOver() {
      return false;
    }

    @Override
    public IPlayer<PlayCard> getWinner() {
      return null;
    }

    @Override
    public IPlayer<PlayCard> getRedPlayer() {
      return humanPlayer.getColor() == PlayerColor.RED ? humanPlayer : null;
    }

    @Override
    public IPlayer<PlayCard> getBluePlayer() {
      return humanPlayer.getColor() == PlayerColor.BLUE ? humanPlayer : null;
    }

    @Override
    public int getScore(IPlayer<PlayCard> player) {
      return 0;
    }

    @Override
    public int getFlipTotal(IPlayer<PlayCard> player, PlayCard card, int x, int y) {
      return 0;
    }
  }

  @Before
  public void setUp() {
    ReadOnlyTrioModel<PlayCard> mockModel = new MockModel();
    humanPlayer = new HumanPlayer<>(mockModel);
    testHand = createTestHand();
  }

  private List<PlayCard> createTestHand() {
    List<PlayCard> hand = new ArrayList<>();
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.ONE);
    values.put(Compass.SOUTH, Value.TWO);
    values.put(Compass.EAST, Value.THREE);
    values.put(Compass.WEST, Value.FOUR);
    hand.add(new PlayCard("test card 1", values));
    return hand;
  }

  @Test
  public void testInitialState() {
    assertNull("init color should be null", humanPlayer.getColor());
    assertTrue("init hand should be empty", humanPlayer.getHand().isEmpty());
  }

  @Test
  public void testSetColor() {
    humanPlayer.setColor(PlayerColor.RED);
    assertEquals(PlayerColor.RED, humanPlayer.getColor());
  }

  @Test
  public void testSetColorTwice() {
    try {
      humanPlayer.setColor(PlayerColor.RED);
      humanPlayer.setColor(PlayerColor.BLUE);
      fail("shoudlnt allow setting color twice");
    } catch (IllegalStateException e) {
      assertEquals("Color cannot be changed twice", e.getMessage());
    }
  }

  @Test
  public void testSetNullColor() {
    try {
      humanPlayer.setColor(null);
      fail("shouldnt allow null color");
    } catch (IllegalArgumentException e) {
      assertEquals("color cannot be null", e.getMessage());
    }
  }

  @Test
  public void testColorHand() {
    humanPlayer.setColor(PlayerColor.RED);
    humanPlayer.colorHand(testHand);
    assertEquals(testHand.size(), humanPlayer.getHand().size());
    assertEquals(PlayerColor.RED, humanPlayer.getHand().get(0).getColor());
  }

  @Test
  public void testColorHandBeforeColor() {
    try {
      humanPlayer.colorHand(testHand);
      fail("shoukldnt allow coloring hand before setting color");
    } catch (IllegalStateException e) {
      assertEquals("Color has not been assigned.", e.getMessage());
    }
  }

  @Test
  public void testColorHandTwice() {
    try {
      humanPlayer.setColor(PlayerColor.RED);
      humanPlayer.colorHand(testHand);
      humanPlayer.colorHand(testHand);
      fail("shouldnt allow coloring hand twice");
    } catch (IllegalStateException e) {
      assertEquals("Player already has a hand", e.getMessage());
    }
  }

  @Test
  public void testTakeCard() {
    humanPlayer.setColor(PlayerColor.RED);
    humanPlayer.colorHand(testHand);
    PlayCard card = humanPlayer.takeCard(0);
    assertNotNull("card shouldnt  be null", card);
    assertEquals("hand should be empty after taking only card",
        0, humanPlayer.getHand().size());
  }

  @Test
  public void testTakeCardInvalidIndex() {
    try {
      humanPlayer.setColor(PlayerColor.RED);
      humanPlayer.colorHand(testHand);
      humanPlayer.takeCard(-1);
      fail("shouldnt allow taking card with invalid index");
    } catch (IllegalArgumentException e) {
      assertEquals("hand index is invalid-1", e.getMessage());
    }
  }

  @Test
  public void testTakeCardFromEmptyHand() {
    try {
      humanPlayer.setColor(PlayerColor.RED);
      humanPlayer.colorHand(testHand);
      humanPlayer.takeCard(0);
      humanPlayer.takeCard(0);
      fail("Should not allow taking card from empty hand");
    } catch (IllegalArgumentException e) {
      assertEquals("hand index is invalid0", e.getMessage());
    }
  }

  @Test
  public void testHandImmutability() {
    humanPlayer.setColor(PlayerColor.RED);
    humanPlayer.colorHand(testHand);
    List<PlayCard> hand = humanPlayer.getHand();
    try {
      hand.add(testHand.get(0));
      fail("Hand should be immutable");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  //should do nothing with a human player
  @Test
  public void testCallMove() {
    TestPlayerAction listener = new TestPlayerAction();
    humanPlayer.addListener(listener);
    humanPlayer.callMove();
    assertFalse("Human player should not trigger moves", listener.moveTriggered);
  }

  private class TestPlayerAction implements PlayerAction {

    boolean moveTriggered = false;

    @Override
    public void handleCardClick(int index, PlayerColor color) {
      //Empty for testing
    }

    @Override
    public void handleCellClick(int row, int col) {
      //Empty for testing
    }

    @Override
    public void handleMove(Move move) {
      moveTriggered = true;
    }
  }
}
