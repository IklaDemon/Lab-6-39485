package lab6.server.command;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

public class RemoveById implements Command {
  private final CollectionManager collection;

  public RemoveById(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - remove_by_id id:\n";
    res += "Removes an element from the collection by its id.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    if (req == null) {
      return Response.error("Request is null.");
    }

    Long id = req.getId();

    if (id == null) {
      return Response.error("ID is null.");
    }

    try {
      boolean removed = collection.remove(id);

      if (removed) {
        return Response.ok("Dragon with id " + id + " removed successfully.\n");
      }

      return Response.error("Dragon with id " + id + " was not removed.\n");
    } catch (Exception e) {
      return Response.error("Failed to remove dragon: " + e.getMessage());
    }
  }
}
