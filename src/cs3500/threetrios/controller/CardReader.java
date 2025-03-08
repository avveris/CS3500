package cs3500.threetrios.controller;

import static cs3500.threetrios.model.Compass.EAST;
import static cs3500.threetrios.model.Compass.NORTH;
import static cs3500.threetrios.model.Compass.SOUTH;
import static cs3500.threetrios.model.Compass.WEST;
import static cs3500.threetrios.model.Value.A;
import static cs3500.threetrios.model.Value.EIGHT;
import static cs3500.threetrios.model.Value.FIVE;
import static cs3500.threetrios.model.Value.FOUR;
import static cs3500.threetrios.model.Value.NINE;
import static cs3500.threetrios.model.Value.ONE;
import static cs3500.threetrios.model.Value.SEVEN;
import static cs3500.threetrios.model.Value.SIX;
import static cs3500.threetrios.model.Value.THREE;
import static cs3500.threetrios.model.Value.TWO;

import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.Value;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implementation of DeckReader that reads a deck configurations for Three Trios game.
 */
public class CardReader implements DeckReader<PlayCard> {

  private static final Compass[] orderOfRead = {NORTH, SOUTH, EAST, WEST};

  @Override
  public List<PlayCard> read(File file) {
    if (file == null) {
      throw new IllegalArgumentException("File is null");
    }

    Scanner scanner = null;
    List<PlayCard> cards = new ArrayList<>();
    try {
      scanner = new Scanner(new FileReader(file));
      //Every line must have the following STRING String(1-A) ... ... String(1-A)
      //Order NORTH SOUTH EAST WEST

      while (scanner.hasNextLine()) {
        String name;
        Map<Compass, Value> values = new HashMap<>();
        name = scanner.next();
        for (int compassPos = 0; compassPos < orderOfRead.length; compassPos++) {
          if (!scanner.hasNext()) {
            throw new IllegalArgumentException(
                "Not enough values to build a card for card name:" + name);
          }
          String value = scanner.next();
          values.put(orderOfRead[compassPos], validateAndTranslateInput(value));
        }
        cards.add(new PlayCard(name, values));
      }

    } catch (IOException e) {
      throw new IllegalArgumentException("Error reading file: " + e.getMessage());
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }
    return cards;
  }

  //Takes input and tries to fit it into the Value's defined range, throws if not a valid token.
  private Value validateAndTranslateInput(String input) {
    switch (input) {
      case "1":
        return ONE;
      case "2":
        return TWO;
      case "3":
        return THREE;
      case "4":
        return FOUR;
      case "5":
        return FIVE;
      case "6":
        return SIX;
      case "7":
        return SEVEN;
      case "8":
        return EIGHT;
      case "9":
        return NINE;
      case "A":
        return A;
      default:
        throw new IllegalArgumentException("Invalid input: " + input);
    }
  }
}
