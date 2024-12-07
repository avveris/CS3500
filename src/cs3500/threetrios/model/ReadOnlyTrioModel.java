package cs3500.threetrios.model;

import java.util.List;

/**
 * Interface for the read-only version of the Three Trios game model.
 */
public interface ReadOnlyTrioModel<C extends Card<C>> {

  /**
   * Retrieves the current game space of the game defined in the Grid object.
   *
   * @return the model's grid.
   * @throws IllegalStateException if game has not started.
   */
  TrioMap<C> getGrid();

  /**
   * Checks if the game is over.
   *
   * @return true if the game is over false otherwise.
   * @throws IllegalStateException if game has not started.
   * @throws IllegalStateException if it is not the end of a turn.
   */
  boolean isGameOver();

  /**
   * Fetches the Player that won the game.
   *
   * @return a Player from the model that had the most colored tiles.
   * @throws IllegalStateException if method is called before the game is over or hasn't been
   *                               started.
   */
  IPlayer<C> getWinner();

  /**
   * Fetches the current Player whose turn it is.
   *
   * @return a Player from the model who is moving.
   * @throws IllegalStateException if game has not been started
   */
  IPlayer<C> getTurn();

  /**
   * Gets the red player.
   *
   * @return the red player
   * @throws IllegalStateException if game has not been started
   */
  IPlayer<C> getRedPlayer();

  /**
   * Gets the blue player.
   *
   * @return the blue player
   * @throws IllegalStateException if game has not been started
   */
  IPlayer<C> getBluePlayer();


  /**
   * Given a player return the total colored cards on their color.
   *
   * @param player the given player
   * @return the number of their cards on the board.
   */
  int getScore(IPlayer<C> player);

  /**
   * Gives the total amount of cards flipped by placing a card at a given position.
   *
   * @param player the player the card is being played to.
   * @param card   the card that would be flipped.
   * @param x      the position on the board.
   * @param y      the position on the board.
   * @return the total number of cards flipped.
   */
  int getFlipTotal(IPlayer<C> player, C card, int x, int y);

  /**
   * Retrieves the Players Hand from a given player.
   *
   * @param player the player's hand being viewed.
   * @return a List of cards.
   * @throws IllegalArgumentException if player is null
   */
  List<C> getPlayerHand(IPlayer<C> player);

  /**
   * Get a tile from the model.
   *
   * @param x the x position on grid.
   * @param y the y position on grid.
   * @return the tile at the given position.
   */
  Cell<C> getTile(int x, int y);

  /**
   * Grabs the Map's height.
   *
   * @return the height of the game board.
   */
  int getGridHeight();

  /**
   * Grabs the Map's width.
   *
   * @return the width of the game board.
   */
  int getGridWidth();
}