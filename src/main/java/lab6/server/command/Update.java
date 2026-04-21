package lab6.server.command;

import lab6.common.model.Dragon;
import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;
import lab6.server.collection.DragonNormalizer;

public class Update implements Command {
  private final CollectionManager collection;

  public Update(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - update id {element}:\n";
    res += "Updates the dragon with the specified id.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    if (req == null) {
      return Response.error("Request is null.");
    }

    Long id = req.getId();
    Dragon dragon = req.getDragon();

    if (id == null) {
      return Response.error("ID is null.");
    }

    if (dragon == null) {
      return Response.error("Dragon is null.");
    }

    try {
      Dragon oldDragon = collection.findById(id);

      if (oldDragon == null) {
        return Response.error("Dragon with id " + id + " not found.");
      }

      DragonNormalizer.normalizeForUpdate(
          dragon,
          id,
          oldDragon.getCreationDate());

      boolean updated = collection.update(id, dragon);

      if (updated) {
        return Response.ok("Dragon with id " + id + " updated successfully.\n");
      }

      return Response.error("Dragon with id " + id + " was not updated.\n");
    } catch (Exception e) {
      return Response.error("Failed to update dragon: " + e.getMessage());
    }
  }
}
