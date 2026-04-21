package lab6.server.command;

import java.util.Map;

import lab6.common.enums.CommandType;
import lab6.common.network.Request;
import lab6.common.network.Response;

/**
 * Command that displays usage information for all available commands.
 */
public class Help implements Command {
  private Map<CommandType, Command> commands;

  /**
   * Creates the command with the specified command registry.
   *
   * @param commands map of available commands
   * @throws NullPointerException if {@code commands} is null
   */
  public Help(Map<CommandType, Command> commands) {
    if (commands == null)
      throw new NullPointerException("Commands is null");
    this.commands = commands;
  }

  /**
   * Returns the usage description of the command.
   *
   * @return usage string
   */
  @Override
  public Response usage() {
    String res = "";
    res += " - help:\n";
    res += "Shows usage about available commands";
    return Response.ok(res);
  }

  @Override
  public Response exec(Request req) {
    String res = "";
    for (Command command : this.commands.values()) {
      res += command.usage().getMessage() + "\n";
    }
    return Response.ok(res);
  }
}
