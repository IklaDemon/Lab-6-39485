package lab6.common.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lab6.common.enums.DragonCharacter;
import lab6.common.enums.DragonType;

/**
 * Represents a dragon stored in the collection.
 *
 * <p>
 * A dragon contains identifying data, coordinates, creation date,
 * optional characteristics and cave information. Instances can be
 * compared by wingspan and, if equal, by id.
 */
public class Dragon implements Comparable<Dragon>, Serializable {

  private static final long serialVersionUID = 1L;

  private long id; // The value of this field must be greater than 0, The value of this field must
                   // be unique, The value of this field must be generated automatically
  private String name; // Field cannot be null, String cannot be empty
  private Coordinates coordinates; // The field cannot be null
  private Date creationDate; // The field cannot be null, The value of this field must be generated
                             // automatically
  private Integer age; // The field value must be greater than 0. The field can be null.
  private double wingspan; // The field value must be greater than 0
  private DragonType type; // The field may be null
  private DragonCharacter character; // The field may be null
  private DragonCave cave; // The field may be null

  /**
   * Creates an empty dragon instance with default field values.
   */
  public Dragon() {
    id = 0;
    name = null;
    coordinates = null;
    creationDate = null;
    age = null;
    wingspan = 0;
    type = null;
    character = null;
    cave = null;
  }

  /**
   * Creates a dragon from the specified builder.
   *
   * @param builder builder containing dragon field values
   */
  public Dragon(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.coordinates = builder.coordinates;
    this.creationDate = builder.creationDate;
    this.age = builder.age;
    this.wingspan = builder.wingspan;
    this.type = builder.type;
    this.character = builder.character;
    this.cave = builder.cave;
  }

  /**
   * Builds {@link Dragon} objects step by step with validation of field values.
   */
  public static class Builder {
    private long id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private Integer age;
    private double wingspan;
    private DragonType type;
    private DragonCharacter character;
    private DragonCave cave;

    /**
     * Sets the dragon id.
     *
     * @param id dragon id, must be greater than 0
     * @return this builder instance
     * @throws IllegalArgumentException if {@code id <= 0}
     */
    public Builder id(long id) {
      if (id <= 0) {
        throw new IllegalArgumentException("id = " + id + ". Shoul be > 0");
      }
      this.id = id;
      return this;
    }

    /**
     * Sets the dragon name.
     *
     * @param name dragon name, must not be null or empty
     * @return this builder instance
     * @throws NullPointerException     if {@code name} is null
     * @throws IllegalArgumentException if {@code name} is empty
     */
    public Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("name = null. Should not be null");
      }
      if (name.equals("")) {
        throw new IllegalArgumentException("name = ''. Should not be empty");
      }
      this.name = name;
      return this;
    }

    /**
     * Sets the dragon coordinates.
     *
     * @param coordinates dragon coordinates
     * @return this builder instance
     * @throws NullPointerException if {@code coordinates} is null
     */
    public Builder coordinates(Coordinates coordinates) {
      if (coordinates == null) {
        throw new NullPointerException("coordinates = null. Should not be null");
      }
      this.coordinates = coordinates;
      return this;
    }

    /**
     * Sets the dragon creation date.
     *
     * @param creationDate creation date
     * @return this builder instance
     * @throws NullPointerException if {@code creationDate} is null
     */
    public Builder creationDate(Date creationDate) {
      if (creationDate == null) {
        throw new NullPointerException("creationDate = null. Should not be null");
      }
      this.creationDate = creationDate;
      return this;
    }

    /**
     * Sets the dragon age.
     *
     * @param age dragon age, may be null, otherwise must be greater than 0
     * @return this builder instance
     * @throws IllegalArgumentException if {@code age <= 0}
     */
    public Builder age(Integer age) {
      if (age == null) {
        this.age = null;
      } else if (age.intValue() <= 0) {
        throw new IllegalArgumentException("age = " + age + ". Should be > 0. Can be null");
      } else {
        this.age = age;
      }
      return this;
    }

    /**
     * Sets the dragon wingspan.
     *
     * @param wingspan wingspan value, must be greater than 0
     * @return this builder instance
     * @throws IllegalArgumentException if {@code wingspan <= 0}
     */
    public Builder wingspan(double wingspan) {
      if (wingspan <= 0.0) {
        throw new IllegalArgumentException("wingspan = " + wingspan + ". Should be > 0");
      }
      this.wingspan = wingspan;
      return this;
    }

    /**
     * Sets the dragon type from its string representation.
     *
     * @param type dragon type name, may be null
     * @return this builder instance
     * @throws IllegalArgumentException if the value does not match any enum
     *                                  constant
     */
    public Builder type(String type) {
      if (type == null) {
        this.type = null;
      } else {
        try {
          this.type = DragonType.valueOf(type);
        } catch (IllegalArgumentException e) {
          throw e;
        }
      }
      return this;
    }

    /**
     * Sets the dragon character from its string representation.
     *
     * @param character dragon character name, may be null
     * @return this builder instance
     * @throws IllegalArgumentException if the value does not match any enum
     *                                  constant
     */
    public Builder character(String character) {
      if (character == null) {
        this.character = null;
      } else {
        try {
          this.character = DragonCharacter.valueOf(character);
        } catch (IllegalArgumentException e) {
          throw e;
        }
      }
      return this;
    }

    /**
     * Sets the dragon cave.
     *
     * @param cave dragon cave, may be null
     * @return this builder instance
     */
    public Builder cave(DragonCave cave) {
      this.cave = cave;
      return this;
    }

    /**
     * Creates a validated {@link Dragon} instance from the current builder state.
     *
     * @return created dragon
     */
    public Dragon build() {
      Dragon d = new Dragon();
      d.setId(this.id);
      d.setName(this.name);
      d.setCoordinates(this.coordinates);
      d.setCreationDate(this.creationDate);
      d.setAge(this.age);
      d.setWingspan(this.wingspan);
      d.setType(this.type == null ? null : this.type.name());
      d.setCharacter(this.character == null ? null : this.character.name());
      d.setCave(this.cave);
      return d;
    }
  }

  /**
   * Sets the dragon id.
   *
   * @param id dragon id, must be greater than 0
   * @throws IllegalArgumentException if {@code id <= 0}
   */
  public void setId(long id) {
    if (id <= 0) {
      throw new IllegalArgumentException("id = " + id + ". Shoul be > 0");
    }
    this.id = id;
  }

  /**
   * Sets the dragon name.
   *
   * @param name dragon name, must not be null or empty
   * @throws NullPointerException     if {@code name} is null
   * @throws IllegalArgumentException if {@code name} is empty
   */
  public void setName(String name) {
    if (name == null) {
      throw new NullPointerException("name = null. Should not be null");
    }
    if (name.equals("")) {
      throw new IllegalArgumentException("name = ''. Should not be empty");
    }
    this.name = name;
  }

  /**
   * Sets the dragon coordinates.
   *
   * @param coordinates dragon coordinates
   * @throws NullPointerException if {@code coordinates} is null
   */
  public void setCoordinates(Coordinates coordinates) {
    if (coordinates == null) {
      throw new NullPointerException("coordinates = null. Should not be null");
    }
    this.coordinates = coordinates;
  }

  /**
   * Sets the dragon creation date.
   *
   * @param creationDate creation date
   * @throws NullPointerException if {@code creationDate} is null
   */
  public void setCreationDate(Date creationDate) {
    if (creationDate == null) {
      throw new NullPointerException("creationDate = null. Should not be null");
    }
    this.creationDate = creationDate;
  }

  /**
   * Sets the dragon age.
   *
   * @param age dragon age, may be null, otherwise must be greater than 0
   * @throws IllegalArgumentException if {@code age <= 0}
   */
  public void setAge(Integer age) {
    if (age == null) {
      this.age = age;
    } else if (age.intValue() <= 0) {
      throw new IllegalArgumentException("age = " + age + ". Should be > 0. Can be null");
    } else {
      this.age = age;
    }
  }

  /**
   * Parses and sets the dragon age from a string.
   *
   * @param age string representation of age
   * @throws IllegalArgumentException if the value is invalid or not greater than
   *                                  0
   */
  public void setAge(String age) {

    try {
      Integer intAge = Integer.parseInt(age);
      this.setAge(intAge);
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }

  /**
   * Sets the dragon wingspan.
   *
   * @param wingspan wingspan value, must be greater than 0
   * @throws IllegalArgumentException if {@code wingspan <= 0}
   */
  public void setWingspan(double wingspan) {
    if (wingspan <= 0.0) {
      throw new IllegalArgumentException("wingspan = " + wingspan + ". Should be > 0");
    }
    this.wingspan = wingspan;
  }

  /**
   * Parses and sets the dragon wingspan from a string.
   *
   * @param wingspan string representation of wingspan
   * @throws IllegalArgumentException if the value cannot be parsed or is not
   *                                  greater than 0
   */
  public void setWingspan(String wingspan) {
    try {
      double doubleWingspan = Double.parseDouble(wingspan);
      this.setWingspan(doubleWingspan);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid fouble for wingspan");
    }
  }

  /**
   * Sets the dragon type from its string representation.
   *
   * @param type dragon type name, may be null
   * @throws IllegalArgumentException if the value does not match any enum
   *                                  constant
   */
  public void setType(String type) {
    if (type == null) {
      this.type = null;
    } else {
      try {
        this.type = DragonType.valueOf(type);
      } catch (IllegalArgumentException e) {
        throw e;
      }
    }
  }

  /**
   * Sets the dragon character from its string representation.
   *
   * @param character dragon character name, may be null
   * @throws IllegalArgumentException if the value does not match any enum
   *                                  constant
   */
  public void setCharacter(String character) {
    if (character == null) {
      this.character = null;
    } else {
      try {
        this.character = DragonCharacter.valueOf(character);
      } catch (IllegalArgumentException e) {
        throw e;
      }
    }
  }

  /**
   * Sets the dragon cave.
   *
   * @param cave dragon cave, may be null
   */
  public void setCave(DragonCave cave) {
    this.cave = cave;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public Integer getAge() {
    return age;
  }

  public double getWingspan() {
    return wingspan;
  }

  public DragonType getType() {
    return type;
  }

  public DragonCharacter getCharacter() {
    return character;
  }

  public DragonCave getCave() {
    return cave;
  }

  /**
   * Compares this dragon with another dragon.
   *
   * <p>
   * Dragons are ordered first by wingspan and then by id.
   *
   * @param other dragon to compare with
   * @return a negative value, zero, or a positive value if this dragon is less
   *         than,
   *         equal to, or greater than the specified dragon
   */
  @Override
  public int compareTo(Dragon other) {
    int byWingspan = Double.compare(this.wingspan, other.wingspan);
    if (byWingspan != 0) {
      return byWingspan;
    }
    return Long.compare(this.id, other.id);
  }

  /**
   * Returns a string representation of this dragon.
   *
   * @return formatted string containing the main dragon fields
   */
  @Override
  public String toString() {
    String age;
    String type;
    String character;
    String cave;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    String creatioDate = formatter.format(this.creationDate);

    if (this.age == null)
      age = "null";
    else
      age = this.age.toString();

    if (this.type == null)
      type = "null";
    else
      type = this.type.toString();

    if (this.character == null)
      character = "null";
    else
      character = this.character.toString();

    if (this.cave == null)
      cave = "null";
    else
      cave = this.cave.toString();

    return String
        .format(
            "ID: %,d, name: %s, coordinates: %s, creation date: %s, age: %s, wingspan: %f, type: %s, character: %s, cave: %s",
            id, name, coordinates.toString(), creatioDate, age, wingspan, type.toString(),
            character.toString(),
            cave);
  }
}
