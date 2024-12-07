package cs3500.threetrios.view;

import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.ReadOnlyTrioModel;

/**
 * Interface for the Three Trios game GUI view.
 */
public interface TriosViewGUI {

  /**
   * Adds a click listener to handle mouse events.
   */
  void addPlayerListener(PlayerAction listener);

  /**
   * Refreshes the updated view.
   */
  void refresh();

  /**
   * Makes the view visible.
   */
  void makeVisible();

  /**
   * Sets the model for the view and makes sure we only use ReadOnly vers for the model.
   */
  void setModel(ReadOnlyTrioModel<PlayCard> model);

  void setHeader(String title);

  HintDecorator getGridDecorator();
}