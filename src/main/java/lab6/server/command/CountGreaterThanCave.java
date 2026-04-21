package lab6.server.command;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

public class CountGreaterThanCave implements Command {
  private final CollectionManager collection;

  public CountGreaterThanCave(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - count_greater_than_cave number:\n";
    res += "Prints the number of elements whose cave value is greater than the specified one.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    if (req == null) {
      return Response.error("Request is null.");
    }

    Double caveValue = req.getCaveValue();

    if (caveValue == null) {
      return Response.error("Cave value is null.");
    }

    try {
      long count = collection.nGreaterThanCave(caveValue);
      return Response.ok(count + "\n");
    } catch (Exception e) {
      return Response.error("Failed to count dragons: " + e.getMessage());
    }
  }
}
