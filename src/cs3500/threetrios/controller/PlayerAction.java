package cs3500.threetrios.controller;

import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.computer.Move;

/**
 * Defines the type of action a player can make in this game either human or non-human.
 */
public interface PlayerAction {

  /**
   * Handles the game behavior when a cell on the grid is clicked.
   *
   * @param row the row index of the clicked cell
   * @param col the column index of the clicked cell
   */
  void handleCellClick(int row, int col);

  /**
   * Handles the game behavior  when a card in a player's hand is clicked.
   *
   * @param index the index of the clicked card in the player's hand
   * @param color the color of the player whose hand was clicked
   */
  void handleCardClick(int index, PlayerColor color);

  /**
   * Handles a non-human Player action.
   *
   * @param move the strategies desired card and x,y position on the grid.
   */
  void handleMove(Move move);
}
