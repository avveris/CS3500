package cs3500.threetrios.view;

import cs3500.threetrios.controller.PlayerAction;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.PlayerColor;
import cs3500.threetrios.model.ReadOnlyTrioModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is our Graphic User Interface for the Three Trios Game.  This creates a visual
 * representation of the game, with a grid board, 2 panels of hands of cards, and renders the cards
 * themselves.  Users can play the game by clicking the cards and then their desired placement on
 * the grid.
 */
public class ThreeTriosGUI extends JFrame implements TriosViewGUI {
  private final HintDecorator gridDecorator;
  private final GridPanel gridPanel;
  private final HandPanel redHandPanel;
  private final HandPanel blueHandPanel;
  private ReadOnlyTrioModel<PlayCard> model;
  private final PlayerColor playerView;
  private static final int WINDOW_WIDTH = 1000;
  private static final int PANEL_HEIGHT = 1000;
  private static final int SIDE_PANEL_WIDTH = 200;

  public ThreeTriosGUI(ReadOnlyTrioModel<PlayCard> model, PlayerColor playerView) {
    super();
    this.model = model;
    this.playerView = playerView;

    gridPanel = new GridPanel();
    gridDecorator = new HintDecorator(gridPanel, playerView);
    redHandPanel = new HandPanel(PlayerColor.RED);
    blueHandPanel = new HandPanel(PlayerColor.BLUE);

    gridDecorator.setModel(model);
    redHandPanel.setModel(model);
    blueHandPanel.setModel(model);

    JCheckBox hintToggle = new JCheckBox("Show Hints");
    hintToggle.addItemListener(e ->
            gridDecorator.setHintsEnabled(e.getStateChange() == ItemEvent.SELECTED));

    setupLayout(hintToggle);
    setupWindow();
  }

  private void setupLayout(JCheckBox hintToggle) {
    setLayout(new BorderLayout());

    JPanel controlPanel = new JPanel();
    controlPanel.add(hintToggle);
    add(controlPanel, BorderLayout.NORTH);

    JPanel redPanel = new JPanel(new BorderLayout());
    redPanel.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, PANEL_HEIGHT));
    redPanel.add(redHandPanel, BorderLayout.CENTER);
    redPanel.setBorder(BorderFactory.createTitledBorder("red player"));
    add(redPanel, BorderLayout.WEST);

    JPanel bluePanel = new JPanel(new BorderLayout());
    bluePanel.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, PANEL_HEIGHT));
    bluePanel.add(blueHandPanel, BorderLayout.CENTER);
    bluePanel.setBorder(BorderFactory.createTitledBorder("blue player"));
    add(bluePanel, BorderLayout.EAST);

    JPanel boardPanel = new JPanel(new BorderLayout());
    boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    boardPanel.add(gridDecorator, BorderLayout.CENTER);
    add(boardPanel, BorderLayout.CENTER);
  }

  private void setupWindow() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(WINDOW_WIDTH, PANEL_HEIGHT));
    pack();
    setLocationRelativeTo(null);
  }

  @Override
  public void addPlayerListener(PlayerAction listener) {
    gridDecorator.addPlayerListener(listener);
    if (playerView == PlayerColor.RED) {
      redHandPanel.addPlayerListener(listener);
    } else {
      blueHandPanel.addPlayerListener(listener);
    }
  }

  @Override
  public HintDecorator getGridDecorator() {
    return gridDecorator;
  }

  @Override
  public void setHeader(String title) {
    setTitle(title);
  }

  @Override
  public void refresh() {
    gridDecorator.repaint();
    redHandPanel.repaint();
    blueHandPanel.repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void setModel(ReadOnlyTrioModel<PlayCard> model) {
    this.model = model;
    gridDecorator.setModel(model);
    redHandPanel.setModel(model);
    blueHandPanel.setModel(model);
    refresh();
  }
}