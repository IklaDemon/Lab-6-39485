package lab6.server.command;

import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Command that displays information about the collection.
 */
public class Info implements Command {
  private final CollectionManager collection;

  public Info(CollectionManager collection) {
    this.collection = collection;
  }

  /**
   * Returns the usage description of the command.
   *
   * @return usage string
   */
  @Override
  public Response usage() {
    String res = "";
    res += " - info:\n";
    res += "Prints information about the collection.";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    return Response.ok(this.collection.info());
  }
}
