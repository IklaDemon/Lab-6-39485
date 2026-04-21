package lab6.server.command;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

public class PrintFieldDescendingCharacter implements Command {
  private final CollectionManager collection;

  public PrintFieldDescendingCharacter(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }
    this.collection = collection;
  }

  @Override
  public Response usage() {
    String res = "";
    res += " - print_field_descending_character:\n";
    res += "Prints the values of the field character of all elements in descending order.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    try {
      String data = collection.getCharactersDescending();

      if (data == null || data.isBlank()) {
        return Response.ok("No character values found.\n");
      }

      return Response.ok(data + "\n");
    } catch (Exception e) {
      return Response.error("Failed to print character values: " + e.getMessage());
    }
  }
}
