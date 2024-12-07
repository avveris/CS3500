package cs3500.threetrios.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import cs3500.threetrios.model.TrioMap;

/**
 * A decorator that adds hint functionality to GridPanel while preserving mouse interaction.
 */
public class HintDecorator extends JPanel implements MouseListener {
  private final GridPanel decoratedPanel;
  private boolean hintsEnabled;
  private ReadOnlyTrioModel<PlayCard> model;
  private PlayCard selectedCard;
  private final PlayerColor playerColor;
  private static final Font HINT_FONT = new Font("Arial", Font.BOLD, 24);

  public HintDecorator(GridPanel panel, PlayerColor color) {
    this.decoratedPanel = panel;
    this.playerColor = color;
    this.hintsEnabled = false;
    setOpaque(false);
    setLayout(new BorderLayout());
    add(decoratedPanel, BorderLayout.CENTER);
    addMouseListener(this);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (shouldShowHints()) {
      Graphics2D g2d = (Graphics2D) g.create();
      paintHints(g2d);
      g2d.dispose();
    }
  }

  private boolean shouldShowHints() {
    return hintsEnabled && model != null && selectedCard != null
            && model.getTurn().getColor() == playerColor;
  }

  private void paintHints(Graphics2D g2d) {
    TrioMap<PlayCard> grid = model.getGrid();
    int cellSize = 100;
    int startX = (getWidth() - (grid.getWidth() * cellSize)) / 2;
    int startY = (getHeight() - (grid.getHeight() * cellSize)) / 2;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setFont(HINT_FONT);

    for (int row = 0; row < grid.getHeight(); row++) {
      for (int col = 0; col < grid.getWidth(); col++) {
        if (!grid.getTile(row, col).isHole() && !grid.getTile(row, col).hasCard()) {
          int flipCount = model.getFlipTotal(model.getTurn(), selectedCard, col, row);
          if (flipCount >= 0) {
            paintCellHint(g2d, row, col, startX, startY, cellSize, flipCount);
          }
        }
      }
    }
  }

  private void paintCellHint(Graphics2D g2d, int row, int col, int startX, int startY,
                             int cellSize, int flipCount) {
    int x = startX + (col * cellSize);
    int y = startY + (row * cellSize);

    g2d.setColor(new Color(144, 238, 144, 180));
    g2d.fillRect(x, y, cellSize, cellSize);

    g2d.setColor(Color.BLACK);
    String hint = String.valueOf(flipCount);
    FontMetrics metrics = g2d.getFontMetrics();
    int textX = x + (cellSize - metrics.stringWidth(hint)) / 2;
    int textY = y + ((cellSize + metrics.getHeight()) / 2);
    g2d.drawString(hint, textX, textY);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    MouseEvent convertedEvent = SwingUtilities.convertMouseEvent(this, e, decoratedPanel);
    decoratedPanel.mouseClicked(convertedEvent);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    MouseEvent convertedEvent = SwingUtilities.convertMouseEvent(this, e, decoratedPanel);
    decoratedPanel.mousePressed(convertedEvent);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    MouseEvent convertedEvent = SwingUtilities.convertMouseEvent(this, e, decoratedPanel);
    decoratedPanel.mouseReleased(convertedEvent);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    MouseEvent convertedEvent = SwingUtilities.convertMouseEvent(this, e, decoratedPanel);
    decoratedPanel.mouseEntered(convertedEvent);
  }

  @Override
  public void mouseExited(MouseEvent e) {
    MouseEvent convertedEvent = SwingUtilities.convertMouseEvent(this, e, decoratedPanel);
    decoratedPanel.mouseExited(convertedEvent);
  }

  public void setSelectedCard(PlayCard card) {
    this.selectedCard = card;
    repaint();
  }

  public void setModel(ReadOnlyTrioModel<PlayCard> model) {
    this.model = model;
    decoratedPanel.setModel(model);
  }

  public void addPlayerListener(PlayerAction listener) {
    decoratedPanel.addPlayerListener(listener);
  }

  public void setHintsEnabled(boolean enabled) {
    this.hintsEnabled = enabled;
    repaint();
  }
}