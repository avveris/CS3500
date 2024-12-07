package cs3500.threetrios.view;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.model.Compass;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import cs3500.threetrios.model.TrioMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


/**
 * Panel for displaying the game grid. The center panel holds the board where cards will be played.
 * The grid can be interacted with, by clicking a card and placing it at a cell on the grid. The
 * coordinate system of the grid starts at (0,0) in the upper left hand corner, X increases
 * downwards and Y increases to the right.
 */
public class GridPanel extends JPanel implements MouseListener {

  private ReadOnlyTrioModel<PlayCard> model;
  private static final int CELL_SIZE = 100;
  private static final Color TILE_COLOR = Color.YELLOW;
  private static final Color HOLE_COLOR = Color.LIGHT_GRAY;

  private final List<PlayerAction> actionListeners;

  /**
   * Builds a new grid panel for the game board.
   */
  public GridPanel() {
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createLineBorder(Color.BLACK));
    addMouseListener(this);
    actionListeners = new ArrayList<>();
  }

  /**
   * Gets the x-coordinate where the grid should start being drawn.
   */
  private int getStartX(int gridWidth) {
    return (getWidth() - (gridWidth * CELL_SIZE)) / 2;
  }

  /**
   * Gets the y-coordinate where the grid should start being drawn.
   */
  private int getStartY(int gridHeight) {
    return (getHeight() - (gridHeight * CELL_SIZE)) / 2;
  }

  private void gridClick(MouseEvent e) {
    if (model == null) {
      return;
    }

    TrioMap<PlayCard> grid = model.getGrid();
    int startX = getStartX(grid.getWidth());
    int startY = getStartY(grid.getHeight());

    int gridX = (e.getX() - startX) / CELL_SIZE;
    int gridY = (e.getY() - startY) / CELL_SIZE;

    for (PlayerAction action : actionListeners) {
      action.handleCellClick(gridY, gridX);
    }

  }

  private boolean isValidMove(int gridX, int gridY, int width, int height) {
    return gridX >= 0 && gridX < width && gridY >= 0 && gridY < height && !model.getGrid()
        .getTile(gridY, gridX).isHole() && !model.getGrid().getTile(gridY, gridX).hasCard();
  }

  public void setModel(ReadOnlyTrioModel<PlayCard> model) {
    this.model = model;
    repaint();
  }

  public void addPlayerListener(PlayerAction action) {
    actionListeners.add(action);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (model == null) {
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    TrioMap<PlayCard> grid = model.getGrid();
    int startX = getStartX(grid.getWidth());
    int startY = getStartY(grid.getHeight());

    drawGrid(g2d, grid, startX, startY);
  }

  private void drawGrid(Graphics2D g2d, TrioMap<PlayCard> grid, int startX, int startY) {
    for (int row = 0; row < grid.getHeight(); row++) {
      for (int col = 0; col < grid.getWidth(); col++) {
        int x = startX + (col * CELL_SIZE);
        int y = startY + (row * CELL_SIZE);
        drawCell(g2d, grid, row, col, x, y);
      }
    }
  }

  private void drawCell(Graphics2D g2d, TrioMap<PlayCard> grid, int row, int col, int x, int y) {
    if (grid.getTile(row, col).isHole()) {
      g2d.setColor(HOLE_COLOR);
      g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    } else if (grid.getTile(row, col).hasCard()) {
      drawCard(g2d, grid.getTile(row, col).getSpace(), x, y);
    } else {
      g2d.setColor(TILE_COLOR);
      g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
  }

  private void drawCardValues(Graphics2D g2d, PlayCard card, int x, int y) {
    String north = card.getValue(Compass.NORTH).toString();
    String south = card.getValue(Compass.SOUTH).toString();
    String west = card.getValue(Compass.WEST).toString();
    String east = card.getValue(Compass.EAST).toString();

    g2d.drawString(north, x + CELL_SIZE / 2 - 5, y + 20);
    g2d.drawString(south, x + CELL_SIZE / 2 - 5, y + CELL_SIZE - 10);
    g2d.drawString(west, x + 10, y + CELL_SIZE / 2);
    g2d.drawString(east, x + CELL_SIZE - 20, y + CELL_SIZE / 2);
  }

  private void drawCard(Graphics2D g2d, PlayCard card, int x, int y) {
    g2d.setColor(card.getColor() == PlayerColor.RED ? Color.RED : Color.BLUE);
    g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 16));

    drawCardValues(g2d, card, x, y);
  }

  @Override
  public Dimension getPreferredSize() {
    if (model == null) {
      return new Dimension(300, 300);
    }
    TrioMap<PlayCard> grid = model.getGrid();
    int width = grid.getWidth() * CELL_SIZE;
    int height = grid.getHeight() * CELL_SIZE;
    return new Dimension(width, height);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (model == null) {
      return;
    }

    TrioMap<PlayCard> grid = model.getGrid();
    int startX = getStartX(grid.getWidth());
    int startY = getStartY(grid.getHeight());

    if (e.getX() < startX || e.getX() > startX + (grid.getWidth() * CELL_SIZE) || e.getY() < startY
        || e.getY() > startY + (grid.getHeight() * CELL_SIZE)) {
      return;
    }

    int gridX = (e.getX() - startX) / CELL_SIZE;
    int gridY = (e.getY() - startY) / CELL_SIZE;

    if (gridX >= 0 && gridX < grid.getWidth() && gridY >= 0 && gridY < grid.getHeight()) {
      System.out.println("clicked grid at coordinates: (" + gridY + ", " + gridX + ")");
      gridClick(e);
    }
  }

  // Empty mouse listener methods
  @Override
  public void mousePressed(MouseEvent e) {
    //Must be here for interface.
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    //Must be here for interface.
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    //Must be here for interface.
  }

  @Override
  public void mouseExited(MouseEvent e) {
    //Must be here for interface.
  }
}