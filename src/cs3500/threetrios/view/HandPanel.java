package cs3500.threetrios.view;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

/**
 * Class to build visual representations of each player's hands, which are held in panels on either
 * side of the grid panel.
 */
class HandPanel extends JPanel implements MouseListener {

  private ReadOnlyTrioModel<PlayCard> model;
  private final PlayerColor color;
  private Integer selectedCard;
  private static final int CARD_WIDTH = 120;
  private static final int CARD_HEIGHT = 160;
  private static final int TOP_MARGIN = 10;

  private final List<PlayerAction> actionListeners;

  public HandPanel(PlayerColor color) {
    this.color = color;
    this.selectedCard = null;
    actionListeners = new ArrayList<>();
    setBackground(Color.WHITE);
    addMouseListener(this);
  }

  /**
   * Handles the mouse clicks for the hand panel's cards.
   *
   * @param e the event to be processed
   */
  @Override
  public void mouseClicked(MouseEvent e) {

    int cardIndex = (e.getY() - TOP_MARGIN) / CARD_HEIGHT;
    if (cardIndex < 0) {
      return;
    }
    if (cardIndex >= model.getTurn().getHand().size()) {
      return;
    }

    if (selectedCard != null) {
      if (selectedCard == cardIndex) {
        selectedCard = null;
      } else {
        selectedCard = cardIndex;
      }
    } else {
      selectedCard = cardIndex;
    }

    if (selectedCard != null) {
      System.out.println("clicked card : " + cardIndex + " from " + color + " player's hand");
      for (PlayerAction action : actionListeners) {
        action.handleCardClick(cardIndex, color);
      }
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // pass
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // pass
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // pass
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // pass
  }

  public void setModel(ReadOnlyTrioModel<PlayCard> model) {
    this.model = model;
    repaint();
  }

  public void addPlayerListener(PlayerAction listener) {
    this.actionListeners.add(listener);
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (model == null) {
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    List<PlayCard> hand;
    if (color == PlayerColor.RED) {
      hand = model.getRedPlayer().getHand();
    } else {
      hand = model.getBluePlayer().getHand();
    }

    for (int i = 0; i < hand.size(); i++) {
      drawCard(g2d, hand.get(i), i);
    }
  }

  private void drawCard(Graphics2D g2d, PlayCard card, int index) {
    int x = (getWidth() - CARD_WIDTH) / 2;
    int y = index * CARD_HEIGHT + TOP_MARGIN;

    cardHighlight(g2d, x, y, index);
    if (selectedCard == null || selectedCard != index) {
      fillCard(g2d, x, y);
    }
    drawCardValues(g2d, card, x, y);
  }


  private void cardHighlight(Graphics2D g2d, int x, int y, int index) {
    if (selectedCard == null) {
      return;
    }
    Graphics2D g = (Graphics2D) g2d.create();
    if (selectedCard == index) {
      g.setColor(Color.GRAY);
      g.setStroke(new BasicStroke(3));
      g.drawRect(x - 3, y - 3, CARD_WIDTH + 6, CARD_HEIGHT + 6);
      fillCard(g, x, y);
    }
  }

  private void fillCard(Graphics2D g2d, int x, int y) {
    if (color == PlayerColor.RED) {
      g2d.setColor(Color.RED);
    } else {
      g2d.setColor(Color.BLUE);
    }
    g2d.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
  }

  private void drawCardValues(Graphics2D g2d, PlayCard card, int x, int y) {
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, (CARD_HEIGHT / 5)));
    FontMetrics metrics = g2d.getFontMetrics();

    drawCompassValue(g2d, card.getValue(Compass.NORTH).toString(),
        x + CARD_WIDTH / 2, y + 30, metrics, true);
    drawCompassValue(g2d, card.getValue(Compass.SOUTH).toString(),
        x + CARD_WIDTH / 2, y + CARD_HEIGHT - 20, metrics, true);
    drawCompassValue(g2d, card.getValue(Compass.WEST).toString(),
        x + 20, y + CARD_HEIGHT / 2, metrics, true);
    drawCompassValue(g2d, card.getValue(Compass.EAST).toString(),
        x + CARD_WIDTH - 20, y + CARD_HEIGHT / 2, metrics, true);
  }

  private void drawCompassValue(Graphics2D g2d, String value, int x, int y,
      FontMetrics metrics, boolean center) {
    if (center) {
      x -= metrics.stringWidth(value) / 2;
    }
    g2d.drawString(value, x, y);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(CARD_WIDTH + 40, CARD_HEIGHT * 5 + (TOP_MARGIN * 2));
  }
}
