package cs3500.threetrios.controller;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cs3500.threetrios.controller.MapReader;
import cs3500.threetrios.controller.TriosReaderImpl;
import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.Tile;


/**
 * Tests for our Reader for our Grid, which is a map reader.  Our map translates files to be
 * configurable with our game, so we are testing the behavior of the reader with different
 * kinds of valid and invalid input.
 * We are using a fake file that will load in what we need it to load, and delete after the
 * test runs, so then we don't need to create a new file for every instance.
 */
public class TriosReaderTest {
  private MapReader<PlayCard> reader;
  private File tempFile;

  // set up for our tests
  @Before
  public void setup() throws IOException {
    reader = new TriosReaderImpl();
    tempFile = File.createTempFile("test", "txt");
    tempFile.deleteOnExit();
  }

  // helper method to write test content to temporary file
  private void writeToFile(String content) throws IOException {
    try (FileWriter writer = new FileWriter(tempFile)) {
      writer.write(content);
    }
  }

  // tests reading a valid grid
  @Test
  public void testValidGridRead() throws IOException {
    writeToFile("4 4\nCCCC\nCXCC\nCCXC\nCCCC");
    Tile<PlayCard>[][] grid = reader.read(tempFile);
    assertNotNull(grid);
    assertEquals(4, grid.length);
    assertEquals(4, grid[0].length);
  }

  // tests behavior of grid with invalid dimensions
  @Test
  public void testInvalidDimensions() throws IOException {
    try {
      writeToFile("0 4\nCCCC\nCXCC\nCCXC\nCCCC");
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests behavior of grid with invalid characters
  @Test
  public void testInvalidCharacter() throws IOException {
    try {
      writeToFile("4 4\nCCCC\nCYCC\nCCXC\nCCCC");
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests behavior of grid with inconsistent row lengths
  @Test
  public void testWrongRowLength() throws IOException {
    try {
      writeToFile("4 4\nCCCC\nCCC\nCCXC\nCCCC");
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests behavior of grid with fewer rows than needed
  @Test
  public void testTooFewRows() throws IOException {
    try {
      writeToFile("4 4\nCCCC\nCXCC\nCCXC");
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests rejection of null file input
  @Test
  public void testNullFile() {
    try {
      reader.read(null);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests reading smallest possible valid grid (1x1)
  @Test
  public void testMinimalGrid() throws IOException {
    writeToFile("1 1\nC");
    Tile<PlayCard>[][] grid = reader.read(tempFile);
    assertNotNull(grid);
    assertEquals(1, grid.length);
    assertEquals(1, grid[0].length);
    assertFalse(grid[0][0].isHole());
  }

  // tests reading grid containing only holes
  @Test
  public void testAllHoles() throws IOException {
    writeToFile("2 2\nXX\nXX");
    Tile<PlayCard>[][] grid = reader.read(tempFile);
    assertNotNull(grid);
    assertTrue(grid[0][0].isHole());
    assertTrue(grid[0][1].isHole());
    assertTrue(grid[1][0].isHole());
    assertTrue(grid[1][1].isHole());
  }

  // tests attempting to read an empty file
  @Test
  public void testEmptyFile() throws IOException {
    writeToFile("");
    try {
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests grid with invalid dimensions format (not numbers)
  @Test
  public void testInvalidDimensionsFormat() throws IOException {
    writeToFile("a b\nCCCC\nCXCC\nCCXC\nCCCC");
    try {
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests with negative dimensions
  @Test
  public void testNegativeDimensions() throws IOException {
    writeToFile("-1 4\nCCCC\nCXCC\nCCXC\nCCCC");
    try {
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests dimensions that don't match content
  @Test
  public void testMismatchedDimensions() throws IOException {
    writeToFile("3 3\nCCCC\nCXCC\nCCXC\nCCCC");
    try {
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  // tests reading grid with leading/trailing whitespace
  @Test
  public void testGridWithWhitespace() throws IOException {
    writeToFile("2 2  \n CC\n CC  ");
    Tile<PlayCard>[][] grid = reader.read(tempFile);
    assertNotNull(grid);
    assertEquals(2, grid.length);
    assertEquals(2, grid[0].length);
  }

  @Test
  public void testEmptyLines() throws IOException {
    try {
      writeToFile("2 2\n\nCC\nCC");  // Empty line in content
      reader.read(tempFile);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("uh oh, error reading file: oop, invalid row length 0"));
    }
  }

  // tests a grid with maximum reasonable dimensions
  @Test
  public void testLargeGrid() throws IOException {
    StringBuilder content = new StringBuilder("10 10\n");
    for (int i = 0; i < 10; i++) {
      content.append("CCCCCCCCCC\n");
    }
    writeToFile(content.toString());
    Tile<PlayCard>[][] grid = reader.read(tempFile);
    assertNotNull(grid);
    assertEquals(10, grid.length);
    assertEquals(10, grid[0].length);
  }

}
