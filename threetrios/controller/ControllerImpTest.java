package cs3500.threetrios.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.threetrios.model.HumanPlayer;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.view.ThreeTriosGUI;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the controller implementation using a mock model.
 */
public class ControllerImpTest {

  private MockModel model;
  private IPlayer<PlayCard> player;
  private ThreeTriosGUI view;
  private ControllerImpl<PlayCard> controller;

  @Before
  public void setup() {
    model = new MockModel();
    player = new HumanPlayer<>(model);
    player.setColor(PlayerColor.RED);
    view = new ThreeTriosGUI(model);
    controller = new ControllerImpl<>(model, view, player);
    model.initializeGame("3_3Grid.config", "BasicDeck.config", false,
        player, new HumanPlayer<>(model));
  }

  @Test
  public void testNullModel() {
    try {
      new ControllerImpl<>(null, view, player);
      fail("shouldnt allow null model");
    } catch (IllegalArgumentException e) {
      // expected
      assertEquals("model and player cannot be null", e.getMessage());
    }
  }

  @Test
  public void testNullPlayer() {
    try {
      new ControllerImpl<>(model, view, null);
      fail("shouldnt allow null player");
    } catch (IllegalArgumentException e) {
      // expected
      assertEquals("Model and player cannot be null", e.getMessage());
    }
  }

  @Test
  public void testCardClickBeforeGame() {
    MockModel uninitializedModel = new MockModel();
    ControllerImpl<PlayCard> newController =
        new ControllerImpl<>(uninitializedModel, view, player);
    try {
      newController.handleCardClick(0, PlayerColor.RED);
      fail("shouldnt allow card click before game starts");
    } catch (IllegalStateException e) {
      assertEquals("game hasnt started", e.getMessage());
    }
  }

  @Test
  public void testTurnSignal() {
    TestModelWatcher watcher = new TestModelWatcher();
    model.addListener(watcher);
    model.notifyTurn();
    assertTrue("should receive turn signal", watcher.turnSignaled);
  }

  private static class TestModelWatcher implements ModelWatcher<PlayCard> {

    boolean turnSignaled = false;
    boolean winnerCalled = false;

    @Override
    public void signalTurn() {
      turnSignaled = true;
    }

    @Override
    public void callWinner() {
      winnerCalled = true;
    }
  }

  @Test
  public void testWrongTurnBlue() {
    model.setCurrentTurn(PlayerColor.RED);
    controller.handleCardClick(0, PlayerColor.BLUE);
    assertFalse("shouldnt allow wrong color selection", model.wasCardPlaced());
  }

  @Test
  public void testValidTurnRed() {
    model.setCurrentTurn(PlayerColor.RED);
    controller.handleCardClick(0, PlayerColor.RED);
    controller.handleCellClick(0, 0);
    assertTrue("should allow correct player to place card", model.wasCardPlaced());
  }

  // place without selecting card first
  @Test
  public void testInvalidCardPlacement() {
    model.setCurrentTurn(PlayerColor.RED);
    controller.handleCellClick(0, 0);
    assertFalse("shouldnt allow placement without card selection", model.wasCardPlaced());
  }

  @Test
  public void testOutOfTurnAction() {
    model.setCurrentTurn(PlayerColor.BLUE);
    controller.handleCardClick(0, PlayerColor.RED);
    controller.handleCellClick(0, 0);
    assertFalse("shouldnt allow actions out of turn", model.wasCardPlaced());
  }
}
