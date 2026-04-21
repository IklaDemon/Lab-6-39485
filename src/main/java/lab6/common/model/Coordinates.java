package lab6.common.model;

import java.io.Serializable;

/**
 * Represents a pair of dragon coordinates.
 *
 * <p>
 * Stores the {@code x} and {@code y} values of the coordinates.
 * The {@code y} field must not be {@code null}.
 */
public class Coordinates implements Serializable {

  private static final long serialVersionUID = 1L;

  private long x;
  private Long y; // The field cannot be null

  /**
   * Creates a new {@code Coordinates} object from numeric values.
   *
   * @param x the x coordinate
   * @param y the y coordinate; must not be {@code null}
   * @throws NullPointerException if {@code y} is {@code null}
   */
  public Coordinates(long x, Long y) {
    setX(x);
    setY(y);
  }

  /**
   * Builder for creating {@code Coordinates} objects step by step.
   */
  public static class Builder {
    private long x;
    private Long y;

    /**
     * Sets the x coordinate value for the builder.
     *
     * @param x the x coordinate
     * @return this builder instance
     */
    public Builder x(long x) {
      this.x = x;
      return this;
    }

    /**
     * Parses and sets the x coordinate value for the builder.
     *
     * @param x the string representation of the x coordinate
     * @return this builder instance
     * @throws IllegalArgumentException if {@code x} cannot be parsed as a
     *                                  {@code long}
     */
    public Builder x(String x) {
      try {
        this.x = Long.parseLong(x);
      } catch (Exception e) {
        throw new IllegalArgumentException("Invalid long number for x");
      }
      return this;
    }

    /**
     * Sets the y coordinate value for the builder.
     *
     * @param y the y coordinate; must not be {@code null}
     * @return this builder instance
     * @throws NullPointerException if {@code y} is {@code null}
     */
    public Builder y(Long y) {
      if (y == null) {
        throw new NullPointerException("y = null. Should not be null");
      }
      this.y = y;
      return this;
    }

    /**
     * Parses and sets the y coordinate value for the builder.
     *
     * @param y the string representation of the y coordinate
     * @return this builder instance
     * @throws IllegalArgumentException if {@code y} is {@code null}
     *                                  or cannot be parsed as a {@code long}
     */
    public Builder y(String y) {
      if (y == null) {
        throw new IllegalArgumentException("y should not be null");
      }
      try {
        this.y = Long.parseLong(y);
      } catch (Exception e) {
        throw new IllegalArgumentException("Invalid long number for y");
      }
      return this;
    }

    /**
     * Builds a new {@code Coordinates} object using the values stored in the
     * builder.
     *
     * @return a new {@code Coordinates} instance
     */
    public Coordinates build() {
      Coordinates coordinates = new Coordinates(this.x, this.y);
      return coordinates;
    }
  }

  /**
   * Creates a new {@code Coordinates} object from string values.
   *
   * @param x the string representation of the x coordinate
   * @param y the string representation of the y coordinate
   * @throws IllegalArgumentException if one of the values cannot be parsed
   *                                  correctly
   */
  public Coordinates(String x, String y) {
    setX(x);
    setY(y);
  }

  /**
   * Sets the x coordinate.
   *
   * @param x the x coordinate
   */
  public void setX(long x) {
    this.x = x;
  }

  /**
   * Sets the y coordinate.
   *
   * @param y the y coordinate; must not be {@code null}
   * @throws NullPointerException if {@code y} is {@code null}
   */
  public void setY(Long y) {
    if (y == null) {
      throw new NullPointerException("y = null. Should not be null");
    }
    this.y = y;
  }

  /**
   * Parses and sets the x coordinate from a string.
   *
   * @param x the string representation of the x coordinate
   * @throws IllegalArgumentException if {@code x} cannot be parsed as a
   *                                  {@code long}
   */
  public void setX(String x) {
    try {
      this.x = Long.parseLong(x);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid long number for x");
    }
  }

  /**
   * Parses and sets the y coordinate from a string.
   *
   * @param y the string representation of the y coordinate
   * @throws IllegalArgumentException if {@code y} is {@code null}
   *                                  or cannot be parsed as a {@code long}
   */
  public void setY(String y) {
    if (y == null) {
      throw new IllegalArgumentException("y should not be null");
    }
    try {
      this.y = Long.parseLong(y);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid long number for y");
    }
  }

  public long getX() {
    return x;
  }

  public Long getY() {
    return y;
  }

  @Override
  public String toString() {
    return String.format("x = %,d, y = %s", x, y.toString());
  }
}
