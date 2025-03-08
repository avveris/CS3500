package cs3500.threetrios.controller;

import static javax.swing.JOptionPane.showMessageDialog;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.MachinePlayer;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.TrioModel;
import cs3500.threetrios.model.computer.Move;
import cs3500.threetrios.view.TriosViewGUI;
import java.awt.Component;

/**
 * Controller implementation for the Three Trios game.
 */
public class ControllerImpl<C extends Card<C>> implements PlayerAction, ModelWatcher<C> {

  private final TrioModel<C> model;
  private final TriosViewGUI view;
  private final IPlayer<C> player;
  private Integer selectedCard;
  private PlayerColor selectedColor;

  /**
   * Constructs a controller.
   *
   * @param model  a model.
   * @param view   a view.
   * @param player type of player.
   */
  public ControllerImpl(TrioModel<C> model, TriosViewGUI view, IPlayer<C> player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null");
    }
    this.model = model;
    this.view = view;
    this.player = player;
    this.selectedCard = null;
    this.selectedColor = null;

    if (view != null) {
      view.addPlayerListener(this);
    }
    player.addListener(this);
    model.addListener(this);
  }

  @Override
  public void handleCardClick(int index, PlayerColor color) {
    if (view == null) {
      return;
    }
    if (model.getTurn() != player) {
      showMessageDialog((Component) view, "Not " + player.getColor() + " turn.");
      return;
    }
    if (color != player.getColor()) {
      showMessageDialog((Component) view, "Not " + player.getColor() + "'s hand.");
      return;
    }
    if (model.getTurn().getColor() == color) {
      selectedCard = index;
      selectedColor = color;
      view.refresh();
    }
  }

  @Override
  public void handleMove(Move move) {
    try {
      model.placeCard(move.getHandIndex(), move.getPosY(), move.getPosX());
      if (view != null) {
        view.refresh();
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      if (view != null) {
        showMessageDialog((Component) view, "Invalid move: " + e.getMessage());
      }
    }
  }

  @Override
  public void handleCellClick(int row, int col) {
    if (view == null) {
      return;
    }
    if (model.getTurn() != player) {
      showMessageDialog((Component) view, "Not " + player.getColor() + " turn.");
      return;
    }
    if (selectedCard == null) {
      showMessageDialog((Component) view,
          "Player " + player.getColor() + ": Please select a card from hand first.");
      return;
    }
    if (model.getTile(col, row).isHole() || model.getTile(col, row).getSpace() != null) {
      showMessageDialog((Component) view,
          "Player " + player.getColor() + ": Please place on a valid tile.");
      return;
    }
    try {
      model.placeCard(selectedCard, row, col);
      selectedCard = null;
      selectedColor = null;
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      showMessageDialog((Component) view, "Invalid move: " + e.getMessage());
    }
  }

  @Override
  public void signalTurn() {
    if (model.getTurn() == player) {
      if (view != null) {
        view.setHeader("Player " + player.getColor() + ": Playing");
      }
      if (player instanceof MachinePlayer) {
        player.callMove();
      }
    } else if (view != null) {
      view.setHeader("Player " + player.getColor() + ": Waiting");
    }
    if (view != null) {
      view.refresh();
    }
  }

  @Override
  public void callWinner() {
    if (view == null) {
      return;
    }
    IPlayer<C> winner = model.getWinner();
    showMessageDialog((Component) view,
        "Game Over! " + winner.getColor() + " wins with score: " + model.getScore(winner));
  }
}
