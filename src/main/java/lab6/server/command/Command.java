package lab6.server.command;

import lab6.common.network.Request;
import lab6.common.network.Response;

/**
 * Represents a server command that can validate usage information
 * and execute a client request.
 *
 * <p>
 * Each command implementation is responsible for returning
 * usage instructions and processing the corresponding request.
 */
public interface Command {

  /**
   * Returns usage information for this command.
   *
   * @return a {@code Response} describing how to use the command
   */
  public Response usage();

  /**
   * Executes this command using the specified request.
   *
   * @param req the client request containing command arguments
   * @return a {@code Response} containing the execution result
   */
  public Response exec(Request req);
}
