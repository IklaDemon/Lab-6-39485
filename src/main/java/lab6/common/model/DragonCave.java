package lab6.common.model;

import java.io.Serializable;

/**
 * Represents a dragon cave.
 *
 * <p>
 * Stores information about the number of treasures in the cave.
 * The number of treasures must not be {@code null} and must be greater than 0.
 */
public class DragonCave implements Serializable {
  private Double numberOfTreasures; // The field cannot be null, the field value must be greater than 0

  /**
   * Creates a new {@code DragonCave} object with the specified number of
   * treasures.
   *
   * @param numberOfTreasures the number of treasures; must not be {@code null}
   *                          and must be greater than 0
   * @throws NullPointerException     if {@code numberOfTreasures} is {@code null}
   * @throws IllegalArgumentException if {@code numberOfTreasures} is less than or
   *                                  equal to 0
   */
  public DragonCave(Double numberOfTreasures) {
    setNumberOfTreasures(numberOfTreasures);
  }

  /**
   * Creates a new {@code DragonCave} object from a string value.
   *
   * @param numberOfTreasures the string representation of the number of treasures
   * @throws IllegalArgumentException if the value cannot be parsed
   *                                  or is less than or equal to 0
   */
  public DragonCave(String numberOfTreasures) {
    setNumberOfTreasures(numberOfTreasures);
  }

  /**
   * Sets the number of treasures in the cave.
   *
   * @param numberOfTreasures the number of treasures; must not be {@code null}
   *                          and must be greater than 0
   * @throws NullPointerException     if {@code numberOfTreasures} is {@code null}
   * @throws IllegalArgumentException if {@code numberOfTreasures} is less than or
   *                                  equal to 0
   */
  public void setNumberOfTreasures(Double numberOfTreasures) {
    if (numberOfTreasures == null) {
      throw new NullPointerException("numberOfTreasures = null. Should not be null");
    }
    if (numberOfTreasures.doubleValue() <= 0) {
      throw new IllegalArgumentException("numberOfTreasures = " + numberOfTreasures + ". Should be > 0");
    }
    this.numberOfTreasures = numberOfTreasures;
  }

  /**
   * Parses and sets the number of treasures from a string value.
   *
   * @param numberOfTreasures the string representation of the number of treasures
   * @throws IllegalArgumentException if the value cannot be parsed
   *                                  or is less than or equal to 0
   */
  public void setNumberOfTreasures(String numberOfTreasures) {
    try {
      Double doubleNumberOfTreasures = Double.parseDouble(numberOfTreasures);
      this.setNumberOfTreasures(doubleNumberOfTreasures);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid Double number for number of treasures");
    }
  }

  public Double getNumberOfTreasures() {
    return numberOfTreasures;
  }

  @Override
  public String toString() {
    return String.format("Number of treasures: %s", numberOfTreasures.toString());
  }
}
