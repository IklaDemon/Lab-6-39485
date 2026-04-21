package lab6.server.command;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

public class Clear implements Command {
  private final CollectionManager collection;

  public Clear(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - clear:\n";
    res += "Clears the collection.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    try {
      collection.clear();
      return Response.ok("Collection cleared successfully.\n");
    } catch (Exception e) {
      return Response.error("Failed to clear collection: " + e.getMessage());
    }
  }
}
