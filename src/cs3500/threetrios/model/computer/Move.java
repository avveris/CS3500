package cs3500.threetrios.model.computer;

/**
 * Data class that holds a strategies play position and card index.
 */
public class Move {

  private final int handIndex;
  private final int posX;
  private final int posY;
  private final int score;

  /**
   * Constructs a Move with a index of the hand in mind and position on the game space.
   *
   * @param handIndex the card in a given players hand.
   * @param posX      the position X.
   * @param posY      the position Y.
   */
  public Move(int handIndex, int posX, int posY, int score) {
    this.handIndex = handIndex;
    this.posX = posX;
    this.posY = posY;
    this.score = score;
  }

  /**
   * Gives the HandIndex.
   *
   * @return the handIndex
   */
  public int getHandIndex() {
    return handIndex;
  }

  /**
   * Gives the x pos.
   *
   * @return posX.
   */
  public int getPosX() {
    return posX;
  }

  /**
   * Gives the y pos.
   *
   * @return posY.
   */
  public int getPosY() {
    return posY;
  }

  public int getScore() {
    return score;
  }
}
