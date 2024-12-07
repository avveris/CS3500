package cs3500.threetrios.model;

import cs3500.threetrios.model.computer.Move;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Logs game events to both console and file for debugging and analysis.
 */
public class GameLogger<C extends Card<C>> implements GameStateObserver<C> {
  private final PrintWriter fileWriter;
  private final DateTimeFormatter formatter;

  public GameLogger(String logFileName) throws IOException {
    fileWriter = new PrintWriter(new FileWriter(logFileName, true));
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  }

  @Override
  public void onGameStateChanged(GameState oldState, GameState newState) {
    String message = String.format("[%s] Game state changed: %s -> %s",
            LocalDateTime.now().format(formatter), oldState, newState);
    log(message);
  }

  @Override
  public void onMoveExecuted(Move move, IPlayer<C> player) {
    String message = String.format("[%s] Player %s made move: Card %d to position (%d,%d)",
            LocalDateTime.now().format(formatter),
            player.getColor(),
            move.getHandIndex(),
            move.getPosX(),
            move.getPosY());
    log(message);
  }

  @Override
  public void onScoreChanged(Map<PlayerColor, Integer> scores) {
    StringBuilder message = new StringBuilder();
    message.append(String.format("[%s] Scores updated: ", LocalDateTime.now().format(formatter)));
    for (Map.Entry<PlayerColor, Integer> entry : scores.entrySet()) {
      message.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
    }
    log(message.toString());
  }

  private void log(String message) {
    System.out.println(message);
    fileWriter.println(message);
    fileWriter.flush();
  }

  public void close() {
    fileWriter.close();
  }
}