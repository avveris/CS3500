package cs3500.threetrios;

import cs3500.threetrios.controller.ControllerImpl;
import cs3500.threetrios.model.HumanPlayer;
import cs3500.threetrios.model.IPlayer;
import cs3500.threetrios.model.MachinePlayer;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.ThreeTrioModel;
import cs3500.threetrios.model.computer.CornerStrategy;
import cs3500.threetrios.model.computer.FlipMaxStrategy;
import cs3500.threetrios.model.computer.LeastFlipableStrategy;
import cs3500.threetrios.model.computer.SingleMove;
import cs3500.threetrios.view.ThreeTriosGUI;
import javax.swing.SwingUtilities;

/**
 * A class for running the Three Trios game.
 */
public final class ThreeTrios {

  /**
   * Runs a game of three trios.
   *
   * @param args takes in 4 arguments defining, map, deck, redPlayer, bluePlayer
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        String map = args[0];
        String deck = args[1];
        ThreeTrioModel model = new ThreeTrioModel();

        IPlayer<PlayCard> redPlayer = playerBuilder(args[2], model);
        IPlayer<PlayCard> bluePlayer = playerBuilder(args[3], model);

        ThreeTriosGUI redView = new ThreeTriosGUI(model);
        ThreeTriosGUI blueView = new ThreeTriosGUI(model);

        ControllerImpl<PlayCard> redController =
            new ControllerImpl<>(model, redView, redPlayer);
        ControllerImpl<PlayCard> blueController =
            new ControllerImpl<>(model, blueView, bluePlayer);

        model.initializeGame(map, deck,
            true, redPlayer, bluePlayer);
        redView.makeVisible();
        blueView.makeVisible();

      } catch (Exception e) {
        System.err.println("Error starting game: " + e.getMessage());
        System.exit(1);
      }
    });
  }

  private static IPlayer<PlayCard> playerBuilder(String arg, ThreeTrioModel model) {
    switch (arg) {
      case "human":
        return new HumanPlayer<>(model);
      case "strategy1":
        return new MachinePlayer<>(model, new SingleMove<>(new FlipMaxStrategy<PlayCard>()));
      case "strategy2":
        return new MachinePlayer<>(model, new SingleMove<>(new CornerStrategy<PlayCard>()));
      case "strategy3":
        return new MachinePlayer<>(model, new SingleMove<>(new LeastFlipableStrategy<PlayCard>()));
      default:
        throw new IllegalArgumentException("Unknown strategy: " + arg);
    }
  }

}
