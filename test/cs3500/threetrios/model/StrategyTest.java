package cs3500.threetrios.model;

import cs3500.threetrios.model.computer.CornerStrategy;
import cs3500.threetrios.model.computer.FlipMaxStrategy;
import cs3500.threetrios.model.computer.InfallableStrategy;
import cs3500.threetrios.model.computer.LeastFlipableStrategy;
import cs3500.threetrios.model.computer.Move;
import cs3500.threetrios.model.computer.SingleMove;
import cs3500.threetrios.model.computer.Strategy;
import cs3500.threetrios.model.computer.TwoStategies;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests multiple implementation of strategies.
 */
public class StrategyTest {

  Strategy<PlayCard> bot;
  InfallableStrategy<PlayCard> tieBreaker;
  TrioModel<PlayCard> mock;
  Appendable log;

  @Before
  public void setUp() {
    log = new StringBuilder();
    mock = new MockStrategyTestModel(log);

  }

  @Test
  public void testFlipMaxStrategyChecksAllSpaces() {
    bot = new FlipMaxStrategy<>();

    List<Move> option = bot.chooseMove(mock, mock.getRedPlayer());
    String[] lines = log.toString().split("\n");

    Assert.assertEquals(lines.length, 27);
    String path = "docs" + File.separator + "strategy-transcript.txt";
    File file = new File(path);
    try {
      FileWriter fw = new FileWriter(file);
      fw.write("Simplest Strategy 1: Check for Most Flips\n");
      fw.write(log.toString());
      fw.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Bad File Path");
    }

  }

  @Test
  public void testFlipMaxStrategyPicksHighestFlip() {
    bot = new FlipMaxStrategy<>();

    List<Move> option = bot.chooseMove(mock, mock.getRedPlayer());
    Move move = option.get(0);
    Assert.assertEquals(move.getPosX(), 2);
    Assert.assertEquals(move.getPosY(), 2);
    Assert.assertEquals(move.getHandIndex(), 0);

  }

  @Test
  public void testTieBreakHandMaxFlip() {
    bot = new FlipMaxStrategy<>();
    List<Move> option = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(option.size(), 3);
    Assert.assertEquals(option.get(0).getScore(), 10);
    Assert.assertEquals(option.get(1).getScore(), 10);
    Assert.assertEquals(option.get(2).getScore(), 10);
    Assert.assertEquals(option.get(0).getPosX(), 2);
    Assert.assertEquals(option.get(1).getPosX(), 2);
    Assert.assertEquals(option.get(2).getPosX(), 2);
    Assert.assertEquals(option.get(0).getPosY(), 2);
    Assert.assertEquals(option.get(1).getPosY(), 2);
    Assert.assertEquals(option.get(2).getPosY(), 2);

    tieBreaker = new SingleMove<>(bot);
    Move move = tieBreaker.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(move.getHandIndex(), 0);
    Assert.assertEquals(move.getPosX(), 2);
    Assert.assertEquals(move.getPosY(), 2);
  }

  @Test
  public void testTieBreakPosMaxFlip() {
    mock.placeCard(0, 1, 1);
    bot = new FlipMaxStrategy<>();
    List<Move> option = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(option.size(), 4);
    Assert.assertEquals(option.get(2).getPosX(), 0);
    Assert.assertEquals(option.get(2).getPosY(), 2);

    tieBreaker = new SingleMove<>(bot);
    Move move = tieBreaker.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(move.getPosX(), 0);
    Assert.assertEquals(move.getPosY(), 2);
  }

  @Test
  public void testCornerChecksCorners() {
    bot = new CornerStrategy<>();

    List<Move> option = bot.chooseMove(mock, mock.getRedPlayer());

    String[] lines = log.toString().split("\n");
    Assert.assertEquals(lines.length, 4);
    Assert.assertEquals(lines[0], "Checking tile (0, 0)");

    mock.placeCard(0, 0, 0);
    mock.placeCard(0, 2, 0);
    mock.placeCard(0, 0, 2);

    option = bot.chooseMove(mock, mock.getRedPlayer());
    lines = log.toString().split("\n");
    Assert.assertEquals(lines.length, 8);
  }

  @Test
  public void testTieBreakersCornerStrategy() {
    bot = new CornerStrategy<>();
    InfallableStrategy<PlayCard> tieBreak = new SingleMove<>(bot);

    List<Move> option = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(option.size(), 4);
    Move tieBreakerMove = tieBreak.chooseMove(mock, mock.getRedPlayer());
    //Demonstrate tie
    Assert.assertEquals(option.get(0).getHandIndex(), option.get(1).getHandIndex());
    Assert.assertEquals(option.get(0).getPosX(), 0);
    Assert.assertEquals(option.get(0).getPosY(), 0);
    Assert.assertEquals(option.get(2).getPosX(), 2);
    Assert.assertEquals(option.get(2).getPosY(), 2);

    Assert.assertEquals(tieBreakerMove.getHandIndex(), 0);
    Assert.assertEquals(tieBreakerMove.getPosX(), 0);
    Assert.assertEquals(tieBreakerMove.getPosY(), 0);
  }

  @Test
  public void testNoValidMovesCorners() {
    mock.placeCard(0, 0, 0);
    mock.placeCard(0, 2, 0);
    mock.placeCard(0, 0, 2);
    mock.placeCard(0, 2, 2);
    bot = new CornerStrategy<>();
    tieBreaker = new SingleMove<>(bot);

    List<Move> options = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(options.size(), 0);

    Move move = tieBreaker.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(move.getHandIndex(), 0);
    Assert.assertEquals(move.getPosX(), 1);
    Assert.assertEquals(move.getPosY(), 0);
  }

  @Test
  public void testInvalidBardOnStrategies() {
    mock.placeCard(0, 0, 0);
    mock.placeCard(0, 0, 1);
    mock.placeCard(0, 0, 2);
    mock.placeCard(0, 1, 0);
    mock.placeCard(0, 1, 1);
    mock.placeCard(0, 1, 2);
    mock.placeCard(0, 2, 0);
    mock.placeCard(0, 2, 1);
    mock.placeCard(0, 2, 2);
    bot = new FlipMaxStrategy<>();
    List<Move> option = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(option.size(), 0);
    tieBreaker = new SingleMove<>(new FlipMaxStrategy<PlayCard>());
    Assert.assertThrows(IllegalStateException.class, () -> {
      tieBreaker.chooseMove(mock, mock.getRedPlayer());
    });

    Assert.assertThrows(IllegalStateException.class, () -> {
      new SingleMove<PlayCard>(new CornerStrategy<>()).chooseMove(mock, mock.getRedPlayer());
    });
  }

  //Extra Credit
  @Test
  public void testStrategy3ChecksEveryTile() {
    bot = new LeastFlipableStrategy<>();
    bot.chooseMove(mock, mock.getRedPlayer());
    String[] lines = log.toString().split("\n");
    Assert.assertEquals(lines.length, 27);
  }

  @Test
  public void testBehaviorAgainstStrongCards() {
    bot = new LeastFlipableStrategy<>();
    List<Move> options = bot.chooseMove(mock, mock.getRedPlayer());
    //Goes to Corners
    Assert.assertEquals(options.size(), 12);
    Assert.assertEquals(options.get(0).getHandIndex(), 0);
    Assert.assertEquals(options.get(0).getPosX(), 0);
    Assert.assertEquals(options.get(0).getPosY(), 0);
  }

  @Test
  public void testGoesToSafePocket() {
    bot = new LeastFlipableStrategy<>();
    mock.placeCard(0, 0, 1);
    mock.placeCard(0, 1, 2);
    List<Move> options = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(options.size(), 3);
  }

  @Test
  public void testTieBreaker() {
    bot = new LeastFlipableStrategy<>();
    mock.placeCard(0, 0, 1);
    mock.placeCard(0, 1, 2);
    List<Move> options = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(options.size(), 3);
    tieBreaker = new SingleMove<>(bot);
    Move move = tieBreaker.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(move.getHandIndex(), 0);
  }

  @Test
  public void testStackCornersFlipMax() {
    bot = new TwoStategies<PlayCard>(new CornerStrategy<>(), new FlipMaxStrategy<>());

    List<Move> options = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(options.size(), 4);

    Move preFill = new SingleMove<PlayCard>(bot).chooseMove(mock, mock.getRedPlayer());
    mock.placeCard(0, 0, 0);
    mock.placeCard(0, 0, 2);
    mock.placeCard(0, 2, 0);
    mock.placeCard(0, 2, 2);

    options = bot.chooseMove(mock, mock.getRedPlayer());
    Assert.assertEquals(options.size(), 15);
    Assert.assertEquals(options.get(0).getHandIndex(), 0);

    Assert.assertNotEquals(preFill,
        new SingleMove<PlayCard>(bot).chooseMove(mock, mock.getRedPlayer()));
  }
}
