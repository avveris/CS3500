package cs3500.threetrios.controller;

import cs3500.threetrios.model.PlayCard;
import cs3500.threetrios.model.Tile;
import java.io.File;
import java.util.Scanner;

/**
 * An implementation of Map Reader that reads in the files and builds a grid with it.
 */
public class TriosReaderImpl implements MapReader<PlayCard> {

  @Override
  public Tile<PlayCard>[][] read(File file) {
    try (Scanner scanner = new Scanner(file)) {
      // Read dimensions
      int rows = scanner.nextInt();
      int cols = scanner.nextInt();
      if (rows <= 0 || cols <= 0) {
        throw new IllegalArgumentException("oops, dimensions cant be negative");
      }
      scanner.nextLine();

      // creates and fills the grid
      Tile<PlayCard>[][] tiles = new Tile[rows][cols];
      for (int row = 0; row < rows; row++) {
        String line = scanner.nextLine().trim();
        if (line.length() != cols) {
          throw new IllegalArgumentException("oop, invalid row length " + row);
        }

        for (int col = 0; col < cols; col++) {
          char cell = line.charAt(col);
          if (cell != 'X' && cell != 'C') {
            throw new IllegalArgumentException("oops, invalid cell type: " + cell);
          }
          tiles[row][col] = new Tile<>(cell == 'X');
        }
      }
      return tiles;

    } catch (Exception e) {
      throw new IllegalArgumentException("uh oh, error reading file: " + e.getMessage());
    }
  }
}