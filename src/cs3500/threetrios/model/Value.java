package cs3500.threetrios.model;

/**
 * Represents all the possible value options a card can hold in Three's Trio.
 */
public enum Value {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  A(10);

  //Represent the int equivalent of the value
  private final int value;
  //Represents the String appearance of the value
  private final String character;

  Value(int value) {
    this.value = value;
    if (value == 10) {
      character = "A";
    } else {
      character = String.valueOf(value);
    }
  }

  /**
   * Fetches the int equivalent of the enum.
   * @return the int held by the class
   */
  public int toInteger() {
    return value;
  }

  @Override
  public String toString() {
    return character;
  }
}
