package cs3500.threetrios.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.Value;
import java.io.File;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Suite of Tests for CardReader.
 */
public class CardReaderTest {

  @Test
  public void testNullFile() {
    CardReader reader = new CardReader();
    try {
      reader.read(null);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("File is null", e.getMessage());
    }
  }

  @Test
  public void testMissingFile() {
    CardReader reader = new CardReader();
    File nonexistentFile = new File("docs/NonexistentDeck.config");

    try {
      reader.read(nonexistentFile);
      fail("should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().startsWith("Error reading file:"));
    }
  }

  @Test
  public void testInvalidValue() {
    CardReader reader = new CardReader();
    File invalidFile = new File("docs/InvalidValueDeck.config");

    try {
      reader.read(invalidFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid input: B", e.getMessage());
    }
  }

  @Test
  public void testInvalidFormat() {
    File fileWithBadFormat = new File("docs/MissingValueDeck.config");
    try {
      new CardReader().read(fileWithBadFormat);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid input: Chloris", e.getMessage());
    }
  }


  @Test
  public void testBasicCard() {
    CardReader reader = new CardReader();
    File validFile = new File("docs/BasicDeck.config");
    List<PlayCard> cards = reader.read(validFile);

    PlayCard firstCard = cards.get(0);
    assertEquals("Abilene", firstCard.getName());
    Assert.assertEquals(Value.ONE, firstCard.getValue(Compass.NORTH));
    assertEquals(Value.A, firstCard.getValue(Compass.SOUTH));
    assertEquals(Value.TWO, firstCard.getValue(Compass.EAST));
    assertEquals(Value.NINE, firstCard.getValue(Compass.WEST));
  }

  @Test
  public void testDeckSize() {
    CardReader reader = new CardReader();
    File validFile = new File("docs/BasicDeck.config");
    List<PlayCard> cards = reader.read(validFile);
    assertEquals(10, cards.size());
  }

  @Test
  public void testAValueCard() {
    CardReader reader = new CardReader();
    File validFile = new File("docs/BasicDeck.config");
    List<PlayCard> cards = reader.read(validFile);

    PlayCard chlorisCard = cards.stream()
        .filter(card -> card.getName().equals("Chloris"))
        .findFirst()
        .orElse(null);

    assertNotNull("Chloris card should exist in deck", chlorisCard);
    assertEquals(Value.A, chlorisCard.getValue(Compass.NORTH));
    assertEquals(Value.A, chlorisCard.getValue(Compass.SOUTH));
    assertEquals(Value.A, chlorisCard.getValue(Compass.EAST));
    assertEquals(Value.A, chlorisCard.getValue(Compass.WEST));
  }

  @Test
  public void testCardsStartWithoutColor() {
    CardReader reader = new CardReader();
    File validFile = new File("docs/BasicDeck.config");
    List<PlayCard> cards = reader.read(validFile);

    cards.forEach(card ->
        Assert.assertEquals(PlayerColor.NONE, card.getColor()));
    Assert.assertEquals(cards.get(0).getColor(), PlayerColor.NONE);
  }

  @Test
  public void testCardName() {
    CardReader reader = new CardReader();
    File validFile = new File("docs/BasicDeck.config");
    List<PlayCard> cards = reader.read(validFile);

    assertTrue(cards.stream().anyMatch(card -> card.getName().equals("Abilene")));
    assertTrue(cards.stream().anyMatch(card -> card.getName().equals("Daneiris")));
    assertTrue(cards.stream().anyMatch(card -> card.getName().equals("Txilar")));
  }
}
