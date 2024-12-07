package cs3500.threetrios.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.threetrios.controller.ControllerImpl;
import cs3500.threetrios.model.computer.CornerStrategy;
import cs3500.threetrios.model.computer.InfallableStrategy;
import cs3500.threetrios.model.computer.Move;
import cs3500.threetrios.model.computer.SingleMove;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the MachinePlayer implementation.
 */
public class MachinePlayerTest {

  private ThreeTrioModel model;
  private MachinePlayer<PlayCard> machinePlayer;
  private InfallableStrategy<PlayCard> strategy;
  private IPlayer<PlayCard> opponent;

  @Before
  public void setUp() {
    model = new ThreeTrioModel(42);
    strategy = new SingleMove<>(new CornerStrategy<PlayCard>());
    machinePlayer = new MachinePlayer<>(model, strategy);
    opponent = new HumanPlayer<>(model);
  }

  @Test
  public void testConstructorNullStrat() {
    try {
      new MachinePlayer<>(model, null);
      fail("shouldnt allow construction with null strategy");
    } catch (IllegalArgumentException e) {
      assertEquals("Strategy cannot be null", e.getMessage());
    }
  }

  @Test
  public void testConstructorNullModel() {
    try {
      new MachinePlayer<>(null, strategy);
      fail("shouldnt allow construction with null model");
    } catch (IllegalArgumentException e) {
      assertEquals("model cannot be null", e.getMessage());
    }
  }

  @Test
  public void testInitialState() {
    assertNull("init color should be null", machinePlayer.getColor());
    assertTrue("init hand should be empty", machinePlayer.getHand().isEmpty());
  }

  @Test
  public void testColorAssignment() {
    machinePlayer.setColor(PlayerColor.RED);
    assertEquals("color should be assigned", PlayerColor.RED, machinePlayer.getColor());
  }

  @Test
  public void testHandAssignment() {
    model.initializeGame("3_3Grid", "BasicDeck", false,
        machinePlayer, opponent);
    assertFalse("hand should be populated", machinePlayer.getHand().isEmpty());
    assertTrue("cards should have player's color",
        machinePlayer.getHand().stream()
            .allMatch(card -> card.getColor() == machinePlayer.getColor()));
  }

  @Test
  public void testStrategyExecution() {
    model.initializeGame("3_3Grid", "BasicDeck", false,
        machinePlayer, opponent);
    int initialHandSize = machinePlayer.getHand().size();
    ControllerImpl<PlayCard> controller = new ControllerImpl<>(model, null, machinePlayer);
    machinePlayer.callMove();
    Assert.assertNotEquals(initialHandSize, machinePlayer.getHand().size());
  }

  @Test
  public void testConsistentStrategy() {
    model.initializeGame("3_3Grid", "BasicDeck", false,
        machinePlayer, opponent);
    Move firstMove = strategy.chooseMove(model, machinePlayer);
    Move secondMove = strategy.chooseMove(model, machinePlayer);

    assertEquals("strat should be consistent",
        firstMove.getHandIndex(), secondMove.getHandIndex());
    assertEquals("strat should be consistent",
        firstMove.getPosX(), secondMove.getPosX());
  }

  @Test
  public void testHandManagement() {
    model.initializeGame("3_3Grid", "BasicDeck",
        false, machinePlayer, opponent);
    int initialHandSize = machinePlayer.getHand().size();

    PlayCard card = machinePlayer.takeCard(0);

    assertNotNull("should return valid card", card);
    assertEquals("hand size should decrease",
        initialHandSize - 1, machinePlayer.getHand().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardIndex() {
    model.initializeGame("3_3Grid.config", "BasicDeck.config",
        false, machinePlayer, opponent);
    machinePlayer.takeCard(-1);
  }

  @Test
  public void testStratValidMoves() {
    model.initializeGame("3_3Grid", "BasicDeck",
        false, machinePlayer, opponent);
    Move move = strategy.chooseMove(model, machinePlayer);
    assertFalse("move should be within grid bounds",
        move.getPosX() < 0 || move.getPosX() >= model.getGridWidth() ||
        move.getPosY() < 0 || move.getPosY() >= model.getGridHeight());
    assertFalse("shouldnt play on occupied space",
        model.getTile(move.getPosX(), move.getPosY()).getSpace() != null);
  }

  @Test
  public void testMachinePlayerTurn() {
    model.initializeGame("3_3Grid", "BasicDeck", false,
        machinePlayer, opponent);
    assertTrue("should be machine's turn", model.getTurn() == machinePlayer);

    machinePlayer.callMove();
  }
}