package cs3500.threetrios.model;

import cs3500.threetrios.controller.PlayerAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract class that defines the general methods all players will need access to.
 *
 * @param <C> type of Card.
 */
public abstract class AbstractPlayer<C extends Card<C>> implements IPlayer<C> {

  protected List<C> hand;
  protected PlayerColor color;
  protected List<PlayerAction> listeners;

  protected final ReadOnlyTrioModel<C> model;

  /**
   * Constructs an AbstractPlayer with a given model.
   * @param model the given model.
   * @throws IllegalArgumentException if model is null.
   */
  public AbstractPlayer(ReadOnlyTrioModel<C> model) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;
    listeners = new ArrayList<>();
    hand = new ArrayList<>();
  }

  @Override
  public void addListener(PlayerAction listener) {
    listeners.add(listener);
  }

  @Override
  public void colorHand(List<C> hand) {
    if (color == null) {
      throw new IllegalStateException("Color has not been assigned.");
    }

    if (hand == null) {
      throw new IllegalArgumentException("Given list must not be null");
    }
    if (!this.hand.isEmpty()) {
      throw new IllegalStateException("Player already has a hand");
    }
    for (C card : hand) {
      if (card == null) {
        throw new IllegalArgumentException("hand can't have null card(s)");
      }
      card.setColor(color);
      this.hand.add(card);
    }
  }

  @Override
  public C takeCard(int index) {
    if (index < 0 || index >= hand.size()) {
      throw new IllegalArgumentException("hand index is invalid" + index);
    }
    C card = hand.get(index);
    hand.remove(index);

    return card;
  }

  @Override
  public List<C> getHand() {
    return Collections.unmodifiableList(hand);
  }

  @Override
  public void setColor(PlayerColor color) {
    if (this.color != null) {
      throw new IllegalStateException("Color cannot be changed twice");
    }
    if (color == null) {
      throw new IllegalArgumentException("color cannot be null");
    }
    this.color = color;
  }

  @Override
  public PlayerColor getColor() {
    return color;
  }


}
