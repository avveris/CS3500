package cs3500.threetrios.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An implementation of Card that is a mostly read only class.
 */
public class PlayCard implements Card<PlayCard> {

  //READ ONLY FIELDS
  private final String name;
  private final Map<Compass, Value> compassValues;
  //MUTABLE FIELDS
  private PlayerColor color;

  /**
   * Constructs a card with its unique identifier and values linked with direction.
   * @param name a string with a unique identifier.
   * @param compassMap a Map of Values and Directions
   * @throws IllegalArgumentException if compassMap is null or the elements aren't full.
   * @throws IllegalArgumentException if name is null.
   */
  public PlayCard(String name, Map<Compass, Value> compassMap) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    if (compassMap == null) {
      throw new IllegalArgumentException("Map cannot be null");
    }
    this.name = name;
    color = PlayerColor.NONE;
    this.compassValues = new HashMap<Compass, Value>();
    for (Compass c : Compass.values()) {
      if (!compassMap.containsKey(c)) {
        throw new IllegalArgumentException("Compass " + c + " does not exist for this card.");
      }
      if (compassMap.get(c) == null) {
        throw new IllegalArgumentException(
                "Compass " + c + " holds a NULL value." + compassValues.get(c));
      }
      this.compassValues.put(c, compassMap.get(c));
    }
  }


  @Override
  public String getName() {
    return name;
  }

  @Override
  public PlayerColor getColor() {
    return color;
  }

  @Override
  public void setColor(PlayerColor color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    } else if (color == PlayerColor.NONE) {
      throw new IllegalArgumentException("Color cannot be set to None");
    } else if (color == this.color) {
      throw new IllegalArgumentException("Color already set to " + color);
    }
    this.color = color;
  }

  @Override
  public Value getValue(Compass dir) {
    if (dir == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    return compassValues.get(dir);
  }

  /**
   * Equals method to compare actual objects instead of instances of.
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayCard)) {
      return false;
    }
    PlayCard that = (PlayCard) o;
    return Objects.equals(name, that.name)
            && Objects.equals(compassValues, that.compassValues)
            && color == that.color;
  }

  public int hashCode() {
    return Objects.hash(name, compassValues, color);
  }

  @Override
  public String toString() {
    return String.format("%s: %s %s %s %s",
            name,
            compassValues.get(Compass.NORTH),
            compassValues.get(Compass.SOUTH),
            compassValues.get(Compass.EAST),
            compassValues.get(Compass.WEST));
  }

  @Override
  public PlayCard clone() {
    PlayCard clone = new PlayCard(name, compassValues);
    clone.color = this.color;
    return clone;
  }

}
