package lab6.common.model;

import java.util.Date;

/**
 * Creates {@link Dragon} objects from their string representation.
 *
 * <p>
 * This class validates parsed values and collects error messages
 * that can be retrieved after a failed creation attempt.
 */
public class DragonMaker {
  private String errors;

  /**
   * Creates a new dragon maker with an empty error buffer.
   */
  public DragonMaker() {
    errors = "";
  }

  /**
   * Creates a {@link Dragon} from a comma-separated string and generated
   * metadata.
   *
   * <p>
   * The input string must contain exactly 8 fields in the expected order.
   * If one or more fields are invalid, the method stores detailed messages
   * that can be obtained through {@link #getErrors()} and throws an exception.
   *
   * @param strDragon    comma-separated dragon data
   * @param id           generated dragon id
   * @param creationDate generated creation date
   * @return created dragon
   * @throws IllegalArgumentException if the input format is invalid or the dragon
   *                                  cannot be created
   */
  public Dragon make(String strDragon, long id, Date creationDate) {
    this.errors = "";
    String[] strParts = strDragon.split(",");
    Dragon.Builder dragonBuilder = new Dragon.Builder();

    if (strParts.length != 8) {
      String error = "Not enough arguments for the dragon\n";
      this.errors += error;
      throw new IllegalArgumentException(error);
    }

    try {
      dragonBuilder.id(id);
    } catch (IllegalArgumentException e) {
      this.errors += "Problem generating ID: " + e.getMessage() + "\n";
    }

    try {
      dragonBuilder.name(strParts[0]);
    } catch (Exception e) {
      this.errors += "Problem with the name: " + e.getMessage() + "\n";
    }

    try {
      Coordinates coordinate = new Coordinates(0L, 0L);
      coordinate.setX(Long.parseLong(strParts[1]));
      coordinate.setY(Long.parseLong(strParts[2]));
      dragonBuilder.coordinates(coordinate);
    } catch (Exception e) {
      this.errors += "Problem with the coordinate: " + e.getMessage() + "\n";
    }

    dragonBuilder.creationDate(creationDate);

    try {
      if (strParts[3].isBlank() || strParts[3].isEmpty() ||
          strParts[3].toLowerCase().equals("null")) {
        dragonBuilder.age(null);
      } else {
        dragonBuilder.age(Integer.parseInt(strParts[3]));
      }
    } catch (Exception e) {
      this.errors += "Problem with the age: " + e.getMessage() + "\n";
    }

    try {
      dragonBuilder.wingspan(Double.parseDouble(strParts[4]));
    } catch (Exception e) {
      this.errors += "Problem with the wingspan: " + e.getMessage() + "\n";
    }

    try {
      if (strParts[5].isBlank() || strParts[5].isEmpty() ||
          strParts[5].toLowerCase().equals("null")) {
        dragonBuilder.type(null);
      } else {
        dragonBuilder.type(strParts[5].toUpperCase());
      }
    } catch (Exception e) {
      this.errors += "Problem with the dragon type: " + e.getMessage() + "\n";
    }

    try {
      if (strParts[6].isBlank() || strParts[6].isEmpty() ||
          strParts[6].toLowerCase().equals("null")) {
        dragonBuilder.character(null);
      } else {
        dragonBuilder.character(strParts[6].toUpperCase());
      }
    } catch (Exception e) {
      this.errors += "Problem with the dragon character: " + e.getMessage() + "\n";
    }

    try {
      if (strParts[7].isBlank() || strParts[7].isEmpty() ||
          strParts[7].toLowerCase().equals("null")) {
        dragonBuilder.cave(null);
      } else {
        dragonBuilder.cave(new DragonCave(Double.parseDouble(strParts[7])));
      }
    } catch (Exception e) {
      this.errors += "Problem with the dragon cave: " + e.getMessage() + "\n";
    }

    if (this.errors.isBlank()) {
      return dragonBuilder.build();
    } else {
      throw new IllegalArgumentException("Error while creating dragon. Call getErrors to get details");
    }
  }

  /**
   * Returns the collected error messages from the last creation attempt.
   *
   * @return error description string
   */
  public String getErrors() {
    return errors;
  }
}
