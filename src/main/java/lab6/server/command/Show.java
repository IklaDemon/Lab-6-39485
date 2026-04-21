package lab6.server.command;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

public class Show implements Command {
  private final CollectionManager collection;

  public Show(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - show:\n";
    res += "Prints all elements of the collection.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    try {
      String data = collection.showAscending();

      if (data == null || data.isBlank()) {
        return Response.ok("Collection is empty.\n");
      }

      return Response.ok(data + "\n");
    } catch (Exception e) {
      return Response.error("Failed to show collection: " + e.getMessage());
    }
  }
}
