package lab6.server.collection;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import lab6.common.model.Dragon;
import lab6.server.io.Reader;
import lab6.server.io.Writer;

/**
 * Manages the collection of dragons stored in a priority queue.
 *
 * <p>
 * This class is responsible for loading the collection from an external source,
 * storing it in memory, generating unique identifiers, and providing operations
 * for adding, updating, removing, saving, and querying dragons.
 */
public class CollectionManager {
  private Reader reader;
  private Writer writer;
  private PriorityQueue<Dragon> collection;
  private Set<Long> iDs;
  private Date creationDate;
  private File file;

  /**
   * Creates a new collection manager and initializes the collection from an
   * external source.
   *
   * @param reader   the reader used to load dragons from storage
   * @param writer   the writer used to save dragons to storage
   * @param filePath the path to the data file
   * @throws NullPointerException     if {@code reader}, {@code writer}, or
   *                                  {@code filePath} is {@code null}
   * @throws IllegalArgumentException if the file path is invalid
   */
  public CollectionManager(Reader reader, Writer writer, String filePath) {
    if (reader == null) {
      throw new NullPointerException("Reader is null");
    }
    if (writer == null) {
      throw new NullPointerException("writer is null");
    }
    this.reader = reader;
    this.writer = writer;
    this.creationDate = new Date();
    this.setFilePath(filePath);
    this.importFromExternalSource();
  }

  /**
   * Validates and sets the file path used by the collection manager.
   *
   * @param filePath the path to the data file
   * @throws NullPointerException     if {@code filePath} is {@code null}
   * @throws IllegalArgumentException if {@code filePath} is empty, does not
   *                                  exist,
   *                                  or refers to a directory
   */
  private void setFilePath(String filePath) {
    if (filePath == null) {
      throw new NullPointerException("filePath is null. Should not be null");
    }
    if (filePath.equals("")) {
      throw new IllegalArgumentException("filePath = " + filePath + ". Should not be empty");
    }
    File file = new File(filePath);
    if (file.exists() && !file.isDirectory()) {
      this.file = file;
    } else {
      throw new IllegalArgumentException("filePath does not exists or is a directory");
    }
  }

  /**
   * Imports the collection and the set of used identifiers from the external
   * source.
   */
  private void importFromExternalSource() {
    this.collection = reader.read();
    iDs = reader.getIDs();
  }

  /**
   * Exports the current collection to the external source.
   *
   * @return {@code true} if the collection was written successfully,
   *         {@code false} otherwise
   */
  public boolean exportToExternalSource() {
    return this.writer.write(this.collection);
  }

  /**
   * Saves the current collection to the external source.
   *
   * @return {@code true} if the collection was saved successfully, {@code false}
   *         otherwise
   */
  public boolean save() {
    return this.exportToExternalSource();
  }

  /**
   * Generates a new unique identifier for a dragon.
   *
   * @return a new unique identifier
   * @throws IllegalArgumentException if the set of used identifiers is not
   *                                  initialized
   */
  public long genNewID() {
    if (iDs == null) {
      throw new IllegalArgumentException("iDs is null, method should be called after import and getIDs");
    }
    long newId = 1;
    while (iDs.contains(newId)) {
      newId++;
    }
    return newId;
  }

  /**
   * Generates a new creation date value.
   *
   * @return the current date
   */
  public Date genNewDate() {
    return new Date();
  }

  /**
   * Searches for a dragon by its identifier.
   *
   * @param id the identifier of the dragon
   * @return the matching dragon, or {@code null} if no dragon with such id exists
   */
  public Dragon findById(long id) {
    return collection.stream()
        .filter(dragon -> dragon.getId() == id)
        .findFirst()
        .orElse(null);
  }

  /**
   * Adds a dragon to the collection.
   *
   * @param dragon the dragon to add
   * @throws IllegalArgumentException if {@code dragon} is {@code null}
   *                                  or its identifier already exists in the
   *                                  collection
   */
  public void addDragon(Dragon dragon) {
    if (dragon == null)
      throw new IllegalArgumentException("Dragon is null");
    if (this.iDs.add(dragon.getId()) == false)
      throw new IllegalArgumentException("Duplicated ID");
    this.collection.add(dragon);
  }

  /**
   * Replaces the dragon with the specified identifier.
   *
   * @param id        the identifier of the dragon to update
   * @param newDragon the new dragon value
   * @return {@code true} if the dragon was updated successfully,
   *         {@code false} if no dragon with the specified id was found in the
   *         collection
   * @throws IllegalArgumentException if {@code newDragon} is {@code null}
   *                                  or if the identifier does not exist
   */
  public boolean update(long id, Dragon newDragon) {
    if (newDragon == null)
      throw new IllegalArgumentException("Dragon is null");

    if (!this.iDs.contains(id))
      throw new IllegalArgumentException("ID not found");

    Dragon old = findById(id);
    if (old == null)
      return false;

    collection.remove(old);
    collection.add(newDragon);
    return true;
  }

  /**
   * Removes the dragon with the specified identifier from the collection.
   *
   * @param id the identifier of the dragon to remove
   * @return {@code true} if the dragon was removed successfully, {@code false}
   *         otherwise
   * @throws IllegalArgumentException if the identifier does not exist
   */
  public boolean remove(long id) {
    if (!this.iDs.contains(id))
      throw new IllegalArgumentException("ID not found");
    Dragon d = findById(id);
    iDs.remove(id);
    return d != null && collection.remove(d);
  }

  /**
   * Removes all dragons from the collection and clears the set of used
   * identifiers.
   */
  public void clear() {
    this.iDs.clear();
    collection.clear();
  }

  /**
   * Returns information about the collection.
   *
   * <p>
   * The returned string contains the collection type, initialization date,
   * current size, file path, and, if the collection is not empty, the minimum
   * and maximum elements.
   *
   * @return a formatted string with collection information
   */
  public String info() {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("Type: ").append(collection.getClass().getName()).append("\n");
    strBuilder.append("Init time: ").append(this.creationDate).append("\n");
    strBuilder.append("Size: ").append(collection.size()).append("\n");
    strBuilder.append("File: ").append(this.file.getPath()).append("\n");
    if (!collection.isEmpty()) {
      strBuilder.append("Min element (by wingspan and id):\n").append(collection.peek().toString()).append("\n");
      strBuilder.append("Max element (by wingspan and id):\n").append(getMax().toString()).append("\n");
    }
    return strBuilder.toString();
  }

  /**
   * Returns all dragons in ascending order.
   *
   * @return a string representation of the collection sorted in ascending order
   */
  public String showAscending() {
    return collection.stream()
        .sorted()
        .map(Dragon::toString)
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns the maximum dragon in the collection.
   *
   * @return the maximum dragon, or {@code null} if the collection is empty
   */
  public Dragon getMax() {
    return collection.stream()
        .max(Comparator.naturalOrder())
        .orElse(null);
  }

  /**
   * Returns the minimum dragon in the collection.
   *
   * @return the minimum dragon, or {@code null} if the collection is empty
   */
  public Dragon getMin() {
    return collection.peek();
  }

  /**
   * Counts dragons whose caves contain more treasures than the specified value.
   *
   * @param treasures the number of treasures used for comparison
   * @return the number of dragons whose cave treasure count is greater than the
   *         specified value
   */
  public long nGreaterThanCave(Double treasures) {
    return collection.stream()
        .filter(dragon -> dragon.getCave() != null)
        .filter(dragon -> dragon.getCave().getNumberOfTreasures() > treasures)
        .count();
  }

  /**
   * Returns all dragons in descending order.
   *
   * @return a string representation of the collection sorted in descending order
   */
  public String getDecreasing() {
    return collection.stream()
        .sorted(Comparator.reverseOrder())
        .map(Dragon::toString)
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns all non-null dragon characters in descending order.
   *
   * @return a string containing dragon character names sorted in descending order
   */
  public String getCharactersDescending() {
    return collection.stream()
        .map(Dragon::getCharacter)
        .filter(character -> character != null)
        .sorted(Comparator.reverseOrder())
        .map(Enum::name)
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string representation of the entire collection.
   *
   * @return a string containing all dragons in the collection
   */
  @Override
  public String toString() {
    return collection.stream()
        .map(Dragon::toString)
        .collect(Collectors.joining("\n"));
  }
}
