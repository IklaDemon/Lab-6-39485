package lab6.server.io;

import java.util.PriorityQueue;
import java.util.Set;
import lab6.common.model.Dragon;

/**
 * Defines reading operations for loading dragons from a data source.
 *
 * <p>
 * An implementation of this interface must be able to read a collection
 * of dragons and provide the set of their identifiers.
 */
public interface Reader {

  /**
   * Reads dragons from the data source.
   *
   * @return priority queue containing loaded dragons
   */
  public PriorityQueue<Dragon> read();

  /**
   * Returns the set of identifiers of loaded dragons.
   *
   * @return set of dragon ids
   */
  public Set<Long> getIDs();
}
