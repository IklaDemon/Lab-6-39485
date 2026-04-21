package lab6.server.command;

import lab6.common.model.Dragon;
import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;
import lab6.server.collection.DragonNormalizer;

public class Add implements Command {
  private final CollectionManager collection;

  public Add(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - add {element}:\n";
    res += "Adds a new dragon to the collection.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
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

      collection.addDragon(dragon);

      return Response.ok("Dragon added successfully.\n");
    } catch (Exception e) {
      return Response.error("Failed to add dragon: " + e.getMessage());
    }
  }
}
