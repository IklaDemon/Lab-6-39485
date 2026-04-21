package lab6.server.command;

import lab6.common.model.Dragon;
import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;
import lab6.server.collection.DragonNormalizer;

/**
 * Command that removes all dragons smaller than the specified one.
 */
public class RemoveLower implements Command {
  private final CollectionManager collection;

  public RemoveLower(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - remove_lower {element}:\n";
    res += "Removes all dragons smaller than the specified one.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    String res = "";
    if (req == null) {
      return Response.error("Request is null.");
    }

    Dragon dragon = req.getDragon();
    if (dragon == null) {
      return Response.error("Dragon is null.");
    }

    try {
      DragonNormalizer.normalizeForAdd(
          dragon,
          collection.genNewID(),
          collection.genNewDate());

      int removed = 0;

      while (true) {
        Dragon min = collection.getMin();

        if (min == null) {
          break;
        }

        if (min.compareTo(dragon) < 0) {
          collection.remove(min.getId());
          res += min.getName() + "\n";
          removed++;
        } else {
          break;
        }
      }

      return Response.ok("Removed " + removed + " dragon(s).\n" + res);
    } catch (Exception e) {
      return Response.error("Failed to remove lower dragons: " + e.getMessage());
    }
  }
}
