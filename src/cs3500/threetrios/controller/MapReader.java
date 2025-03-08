package cs3500.threetrios.controller;

import java.io.File;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Tile;

/**
 * A reader designed to take in configuration files and read them in context of a Three Trios Map.
 */
public interface MapReader<C extends Card<C>> {

  /**
   * reads the file of a given Map configuration and translates it into a 2D array
   * for the Grid to use.
   * param file the given file to read.
   * return a 2D Tile array of a given Type of Card.
   * throws IllegalArgumentException if ROWS or COLS is > 0.
   * throws IllegalArgumentException if ROWS and/or COLS is not present on the first
   * row of the file.
   * throws IllegalArgumentException if a character is present other than 'X' or 'C'
   * throws IllegalArgumentException if a row or col in the file doesn't match its given specs.
   */
  Tile<C>[][] read(File file);
}
