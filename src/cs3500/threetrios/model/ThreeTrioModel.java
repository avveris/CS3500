package cs3500.threetrios.model;

import static cs3500.threetrios.model.GameState.BATTLE_STEP;
import static cs3500.threetrios.model.GameState.END_TURN;
import static cs3500.threetrios.model.GameState.FINISHED;
import static cs3500.threetrios.model.GameState.NOT_STARTED;
import static cs3500.threetrios.model.GameState.PLACING_STEP;

import cs3500.threetrios.controller.CardReader;
import cs3500.threetrios.controller.ModelWatcher;
import cs3500.threetrios.controller.TriosReaderImpl;
import cs3500.threetrios.model.computer.Move;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This is the model class for our threeTriosModel and holds the majority of our methods for the
 * model of the game.  Also where we implement the previously established classes to work with one
 * another to create game logic. Uses a 0 index system, where the top left cell of the grid is 0,0.
 * This means our grid's X coordinate increases down and Y increases right.
 */
public class ThreeTrioModel implements TrioModel<PlayCard> {

  //Random seed
  private final Random rand;
  private TrioMap<PlayCard> grid;
  private GameState state;
  private IPlayer<PlayCard> redPlayer;
  private IPlayer<PlayCard> bluePlayer;
  private IPlayer<PlayCard> currentTurn;
  private final List<ModelWatcher<PlayCard>> listeners;
  private final MoveHistory<PlayCard> moveHistory;
  private final List<GameStateObserver<PlayCard>> stateObservers;
  private final Map<PlayerColor, Integer> currentScores;

  /**
   * Constructs a Model with a seed to help test randomness.
   *
   * @param seed the defined seed to construct rand.
   */
  public ThreeTrioModel(long seed) {
    rand = new Random(seed);
    state = NOT_STARTED;
    listeners = new ArrayList<>();
    moveHistory = new MoveHistory<>(50); // Store last 50 moves
    stateObservers = new ArrayList<>();
    currentScores = new HashMap<>();
  }

  /**
   * Constructs the model.
   */
  public ThreeTrioModel() {
    state = NOT_STARTED;
    rand = new Random();
    listeners = new ArrayList<>();
    moveHistory = new MoveHistory<>(50);
    stateObservers = new ArrayList<>();
    currentScores = new HashMap<>();
  }

  /**
   * Adds observers for model testing.
   *
   * @param observer a GameStateListener.
   */
  public void addStateObserver(GameStateObserver<PlayCard> observer) {
    stateObservers.add(observer);
    moveHistory.addObserver(observer);
  }

  @Override
  public void initializeGame(String mapName, String deckName, boolean shuffle,
      IPlayer<PlayCard> redPlayer, IPlayer<PlayCard> bluePlayer) {
    if (state != NOT_STARTED) {
      throw new IllegalStateException("Game has already been started cannot be called again.");
    }
    if (mapName == null || deckName == null || redPlayer == null || bluePlayer == null) {
      throw new IllegalArgumentException("Arguments cannot be null.");
    }
    String gridPath = "docs" + File.separator + mapName + ".config";
    File gridFile = new File(gridPath);
    grid = new Grid<PlayCard>(new TriosReaderImpl().read(gridFile));

    String deckPath = "docs" + File.separator + deckName + ".config";
    File deckFile = new File(deckPath);
    List<PlayCard> deck = new CardReader().read(deckFile);
    //Playable Cells must be odd let it be N
    int playableTiles = grid.getNumberOfPlayableTiles();
    if (playableTiles % 2 == 0) {
      throw new IllegalArgumentException("Grid has an even amount of playable tiles.");
    }

    if (deck.size() < playableTiles + 1) {
      throw new IllegalArgumentException("Deck does not have enough cards to play a valid game");
    }
    if (shuffle) {
      Collections.shuffle(deck, rand);
    }
    this.redPlayer = redPlayer;
    this.redPlayer.setColor(PlayerColor.RED);
    this.bluePlayer = bluePlayer;
    this.bluePlayer.setColor(PlayerColor.BLUE);
    int deckSize = (playableTiles + 1) / 2;
    List<PlayCard> redDeck = deck.subList(0, deckSize);
    List<PlayCard> blueDeck = deck.subList(deckSize, deckSize + deckSize);
    redPlayer.colorHand(redDeck);
    bluePlayer.colorHand(blueDeck);
    currentTurn = redPlayer;
    state = PLACING_STEP;
    callTurnListeners();
  }

  @Override
  public void addListener(ModelWatcher<PlayCard> observer) {
    listeners.add(observer);
  }

  private void callTurnListeners() {
    for (ModelWatcher<PlayCard> observer : listeners) {
      observer.signalTurn();
    }
  }

  @Override
  public void placeCard(int handIndex, int posY, int posX) {
    if (state != PLACING_STEP) {
      throw new IllegalStateException(
          "Game should not be trying to place its state is at: " + state);
    }
    PlayCard card = currentTurn.takeCard(handIndex);
    grid.getTile(posY, posX).playToTile(card);

    Move move = new Move(handIndex, posX, posY, getScore(currentTurn));
    updateScores();
    moveHistory.recordMove(move, state, new HashMap<>(currentScores));

    for (GameStateObserver<PlayCard> observer : stateObservers) {
      observer.onMoveExecuted(move, currentTurn);
    }

    GameState oldState = state;
    state = BATTLE_STEP;
    notifyStateChanged(oldState, state);
    battleStep(grid.getTile(posY, posX));
  }

  private void battleStep(Cell<PlayCard> tile) {
    if (state != BATTLE_STEP) {
      throw new IllegalStateException("Game should not be battling it is at: " + state);
    }

    grid.flipTiles(tile, currentTurn.getColor());
    updateScores();

    GameState oldState = state;
    state = END_TURN;
    notifyStateChanged(oldState, state);
    changeTurn();
  }

  private void updateScores() {
    currentScores.put(PlayerColor.RED, getScore(redPlayer));
    currentScores.put(PlayerColor.BLUE, getScore(bluePlayer));

    for (GameStateObserver<PlayCard> observer : stateObservers) {
      observer.onScoreChanged(new HashMap<>(currentScores));
    }
  }

  private void notifyStateChanged(GameState oldState, GameState newState) {
    for (GameStateObserver<PlayCard> observer : stateObservers) {
      observer.onGameStateChanged(oldState, newState);
    }
  }


  private void changeTurn() {
    System.out.println("Changing turn. Current turn: " + currentTurn.getColor());
    if (state != END_TURN) {
      throw new IllegalStateException(
          "Game should not be trying to change turns it is at: " + state);
    }

    if (!isGameOver()) {
      if (currentTurn == redPlayer) {
        currentTurn = bluePlayer;
      } else {
        currentTurn = redPlayer;
      }
      System.out.println("New turn: " + currentTurn.getColor());
      state = PLACING_STEP;
      callTurnListeners();
    } else {
      callTurnListeners();
      for (ModelWatcher<PlayCard> observer : listeners) {
        observer.callWinner();
      }
    }
  }

  @Override
  public TrioMap<PlayCard> getGrid() {
    if (state == NOT_STARTED) {
      throw new IllegalStateException("Game Has Not Started");
    }
    return grid.cloneGrid();
  }

  @Override
  public boolean isGameOver() {
    if (state == NOT_STARTED) {
      throw new IllegalStateException("Game Has Not Started");
    }
    if (state == FINISHED) {
      return true;
    }
    if (state != END_TURN) {
      throw new IllegalStateException("Cannot Check if Game is Over during a turn's step.");
    }

    if (grid.isFull()) {
      state = FINISHED;
      return true;
    }

    return false;
  }

  @Override
  public IPlayer<PlayCard> getWinner() {
    if (state == NOT_STARTED) {
      throw new IllegalStateException("Game Has Not Started");
    }
    if (state != FINISHED) {
      throw new IllegalStateException("cant check for winner while game is ongoing.");
    }
    if (grid.getColorCount(PlayerColor.RED) > grid.getColorCount(PlayerColor.BLUE)) {
      return redPlayer;
    }
    return bluePlayer;
  }

  @Override
  public IPlayer<PlayCard> getTurn() {
    if (state == NOT_STARTED) {
      throw new IllegalStateException("Cannot get turn before game is initialized");
    }
    return currentTurn;
  }

  @Override
  public IPlayer<PlayCard> getRedPlayer() {
    if (state == NOT_STARTED) {
      throw new IllegalStateException("cant get red player before game is started");
    }
    return redPlayer;
  }

  @Override
  public IPlayer<PlayCard> getBluePlayer() {
    if (state == NOT_STARTED) {
      throw new IllegalStateException("cant get blue player before game is started");
    }
    return bluePlayer;
  }

  @Override
  public int getScore(IPlayer<PlayCard> player) {
    return grid.getColorCount(player.getColor());
  }

  @Override
  public int getFlipTotal(IPlayer<PlayCard> player, PlayCard card, int x, int y) {
    if (getTile(x, y).isHole() || getTile(x, y).getSpace() != null) {
      //Return non-playable tiles as -1
      return -1;
    }
    int getCurrentTotal = getScore(player) - 1;
    TrioMap<PlayCard> hypoGrid = getGrid();
    hypoGrid.getTile(y, x).playToTile(card);
    hypoGrid.flipTiles(hypoGrid.getTile(y, x), player.getColor());
    return hypoGrid.getColorCount(player.getColor()) - getCurrentTotal;
  }

  @Override
  public List<PlayCard> getPlayerHand(IPlayer<PlayCard> player) {
    return player.getHand();
  }

  @Override
  public Cell<PlayCard> getTile(int x, int y) {
    return grid.getTile(y, x);
  }

  @Override
  public int getGridHeight() {
    return grid.getHeight();
  }

  @Override
  public int getGridWidth() {
    return grid.getWidth();
  }
}