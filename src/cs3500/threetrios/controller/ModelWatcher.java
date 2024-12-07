package cs3500.threetrios.controller;

import cs3500.threetrios.model.Card;

/**
 * An observer for a controller's given model to watch and broadcast certain calls from the model.
 */
public interface ModelWatcher<C extends Card<C>> {

  /**
   * Listens for when a change in turn happens in the model. If the change matches the given Player
   * than we allow that Player to initiate their move process.
   */
  void signalTurn();

  /**
   * Listens for when the model's game is over and calls a winner.
   */
  void callWinner();
}
