package cs3500.threetrios.model;

import static org.junit.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for our PlayCard class, which holds the implementaion of the card
 * interface.  This mostly is testing the constructors and making sure it has valid
 * parameters, but also testsfor immutability.
 */
public class PlayCardTest {

  private Card card;
  private Map<Compass, Value> validValues;

  @Before
  public void setup() {
    validValues = new HashMap<>();
    validValues.put(Compass.NORTH, Value.ONE);
    validValues.put(Compass.SOUTH, Value.TWO);
    validValues.put(Compass.EAST, Value.FOUR);
    validValues.put(Compass.WEST, Value.THREE);
    card = new PlayCard("testCard", validValues);
  }

  // tests invalid construction in different cases, like nulls
  @Test
  public void testBadConstruction() {
    assertThrows(IllegalArgumentException.class, () -> new PlayCard(null, validValues));
    assertThrows(IllegalArgumentException.class, () -> new PlayCard(
            "testCard", null));

    Map<Compass, Value> invalidValues = new HashMap<>();
    invalidValues.put(Compass.SOUTH, Value.TWO);
    invalidValues.put(Compass.EAST, Value.FOUR);
    invalidValues.put(Compass.WEST, Value.THREE);
    assertThrows(IllegalArgumentException.class, () -> new PlayCard(
            "testCard", invalidValues));
    invalidValues.put(Compass.NORTH, null);
    assertThrows(IllegalArgumentException.class, () -> new PlayCard(
            "testCard", invalidValues));
  }

  // tests getting the value of an invalid card
  @Test
  public void testInvalidGetValue() {
    assertThrows(IllegalArgumentException.class, () -> card.getValue(null));
  }

  // tests for getting the value of a valid card
  @Test
  public void testGetValue() {
    Assert.assertEquals(card.getValue(Compass.NORTH), Value.ONE);
  }

  // test for setting the color of an invalid playercard
  @Test
  public void testTryToColorCardNone() {
    assertThrows(IllegalArgumentException.class, () -> card.setColor(PlayerColor.NONE));
  }

  // tests for getting the value of a null card color
  @Test
  public void testTryToColorCardNull() {
    assertThrows(IllegalArgumentException.class, () -> card.setColor(null));
  }

  // tests for coloring the card the same color
  @Test
  public void testTryToColorCardSameColor() {
    card.setColor(PlayerColor.BLUE);
    Assert.assertEquals(card.getColor(), PlayerColor.BLUE);
    assertThrows(IllegalArgumentException.class, () -> card.setColor(PlayerColor.BLUE));
    card.setColor(PlayerColor.RED);
    Assert.assertEquals(card.getColor(), PlayerColor.RED);
    card.setColor(PlayerColor.BLUE);
    Assert.assertEquals(card.getColor(), PlayerColor.BLUE);
  }

  // tests for immutability with our maps
  @Test
  public void testImmutabilityFromInputMap() {
    Map<Compass, Value> values = new HashMap<>();
    values.put(Compass.NORTH, Value.ONE);
    values.put(Compass.SOUTH, Value.TWO);
    values.put(Compass.EAST, Value.FOUR);
    values.put(Compass.WEST, Value.THREE);
    Card toDeleteValue = new PlayCard("testCard", values);

    Assert.assertEquals(toDeleteValue, card);

    values.replace(Compass.NORTH, Value.SIX);
    Assert.assertEquals(toDeleteValue, card);
    values.remove(Compass.NORTH);
    Assert.assertEquals(toDeleteValue, card);
  }
}
