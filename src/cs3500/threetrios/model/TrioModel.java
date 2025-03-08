package cs3500.threetrios.model;


import cs3500.threetrios.controller.ModelWatcher;

/**
 * This is the interface for our Model, starts the game and also has the placeCard method for the
 * grid PlayerCard interaction.
 */
public interface TrioModel<C extends Card<C>> extends ReadOnlyTrioModel<C> {

  /**
   * Places a card from a Player's hand onto the grid. Removing the card from their hand and placing
   * it into a playable grid. It has the additional effect of starting the battle step on the gri,
   * flipping cards per the rules of Three Trio.
   *
   * @param handIndex the card position in a Player's hand.
   * @param posY      the X position on the grid
   * @param posX      the Y position on the grid
   * @throws IllegalStateException    if the game state is not at Placing Step yet.
   * @throws IllegalArgumentException if positions are out of bounds of the grid.
   * @throws IllegalArgumentException if hand position is out of the hand.
   */
  void placeCard(int handIndex, int posY, int posX);

  /**
   * Starts a game of Three Trios.
   *
   * @param mapName  file name for map to be loaded.
   * @param deckName file name for deck to be loaded,
   * @param shuffle  if the deck should be shuffled or not.
   * @throws IllegalStateException    if game has already started.
   * @throws IllegalArgumentException if model is given a null deck or null map.
   * @throws IllegalArgumentException if map has an even number of playableTiles.
   * @throws IllegalArgumentException if deck does not have enough cards to start a game.
   */
  void initializeGame(String mapName, String deckName, boolean shuffle, IPlayer<C> redPlayer,
      IPlayer<C> bluePlayer);

  /**
   * Adds a listener for events in the model.
   *
   * @param observer the type of observer.
   */
  void addListener(ModelWatcher<C> observer);


}
