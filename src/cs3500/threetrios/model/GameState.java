package cs3500.threetrios.model;

/**
 * This represends our GameState for each stage of the ThreeTrios Game.  These are stored as
 * an enum for consistancy sake, and to prevent mutation.
 */
public enum GameState {
  NOT_STARTED,
  PLACING_STEP,
  BATTLE_STEP,
  END_TURN,
  FINISHED
}
