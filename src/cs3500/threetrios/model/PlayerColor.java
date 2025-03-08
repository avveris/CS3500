package cs3500.threetrios.model;

/**
 * ENUM for our player colors, an enum to prevent mutation and
 * will hold our player name as well.
 */
public enum PlayerColor {
  RED("RED"),
  BLUE("BLUE"),
  NONE("NONE");

  private final String name;

  PlayerColor(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }
}
