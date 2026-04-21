package lab6.server.command;

import lab6.common.model.Dragon;
import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;
import lab6.server.collection.DragonNormalizer;

public class AddIfMax implements Command {
  private final CollectionManager collection;

  public AddIfMax(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - add_if_max {element}:\n";
    res += "Adds a new dragon if it is greater than the current maximum element.";
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

      Dragon max = collection.getMax();

      if (max == null || dragon.compareTo(max) > 0) {
        collection.addDragon(dragon);
        return Response.ok("Dragon added successfully.\n");
      }

      return Response.ok("Dragon was not added because it is not greater than " + max.getName() + "\n");
    } catch (Exception e) {
      return Response.error("Failed to add dragon: " + e.getMessage());
    }
  }
}
