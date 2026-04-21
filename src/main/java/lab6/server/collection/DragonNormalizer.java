package lab6.server.collection;

import java.util.Date;
import lab6.common.model.Dragon;

/**
 * Utility class for normalizing dragon objects before collection operations.
 *
 * <p>
 * This class contains only static methods used to assign the correct
 * identifier and creation date to dragons before adding or updating them.
 */
public final class DragonNormalizer {
  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private DragonNormalizer() {
    ;
  }

  /**
   * Normalizes a dragon before adding it to the collection.
   *
   * <p>
   * The method assigns a new identifier and a new creation date
   * to the specified dragon.
   *
   * @param dragon          the dragon to normalize
   * @param newId           the new identifier to assign
   * @param newCreationDate the new creation date to assign
   * @throws IllegalArgumentException if {@code dragon} is {@code null}
   *                                  or if {@code newCreationDate} is
   *                                  {@code null}
   */
  public static void normalizeForAdd(Dragon dragon, long newId, Date newCreationDate) {
    if (dragon == null) {
      throw new IllegalArgumentException("Dragon is null");
    }
    if (newCreationDate == null) {
      throw new IllegalArgumentException("Creation date is null");
    }
    dragon.setId(newId);
    dragon.setCreationDate(newCreationDate);
  }

  /**
   * Normalizes a dragon before updating an existing element in the collection.
   *
   * <p>
   * The method assigns the target identifier and preserves
   * the previous creation date of the existing dragon.
   *
   * @param dragon          the dragon to normalize
   * @param targetId        the identifier of the dragon being updated
   * @param oldCreationDate the original creation date to preserve
   * @throws IllegalArgumentException if {@code dragon} is {@code null}
   *                                  or if {@code oldCreationDate} is
   *                                  {@code null}
   */
  public static void normalizeForUpdate(Dragon dragon, long targetId, Date oldCreationDate) {
    if (dragon == null) {
      throw new IllegalArgumentException("Dragon is null");
    }
    if (oldCreationDate == null) {
      throw new IllegalArgumentException("Creation date is null");
    }
    dragon.setId(targetId);
    dragon.setCreationDate(oldCreationDate);
  }
}
