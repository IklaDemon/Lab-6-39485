package lab6.server.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.EnumMap;
import java.util.Map;
import lab6.common.enums.CommandType;
import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.server.collection.CollectionManager;

/**
 * Executes server commands based on incoming requests.
 *
 * <p>
 * This class stores the mapping between {@code CommandType} values
 * and their corresponding command implementations, and delegates request
 * execution to the appropriate command.
 */
public class CommandExecutor {

  private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

  private final Map<CommandType, Command> commands;

  /**
   * Creates a new command executor and registers all available commands.
   *
   * @param collection the collection manager used by command implementations
   * @throws NullPointerException if {@code collection} is {@code null}
   */
  public CommandExecutor(CollectionManager collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }

    this.commands = new EnumMap<>(CommandType.class);
    this.commands.put(CommandType.INFO, new Info(collection));
    this.commands.put(CommandType.SHOW, new Show(collection));
    this.commands.put(CommandType.ADD, new Add(collection));
    this.commands.put(CommandType.ADD_IF_MAX, new AddIfMax(collection));
    this.commands.put(CommandType.ADD_IF_MIN, new AddIfMin(collection));
    this.commands.put(CommandType.REMOVE_LOWER, new RemoveLower(collection));
    this.commands.put(CommandType.UPDATE, new Update(collection));
    this.commands.put(CommandType.REMOVE_BY_ID, new RemoveById(collection));
    this.commands.put(CommandType.COUNT_GREATER_THAN_CAVE, new CountGreaterThanCave(collection));
    this.commands.put(CommandType.CLEAR, new Clear(collection));
    this.commands.put(CommandType.PRINT_DESCENDING, new PrintDescending(collection));
    this.commands.put(CommandType.PRINT_FIELD_DESCENDING_CHARACTER, new PrintFieldDescendingCharacter(collection));
    this.commands.put(CommandType.HELP, new Help(commands));

    logger.info("CommandExecutor initialized with {} command(s).", this.commands.size());
  }

  /**
   * Executes the command described by the specified request.
   *
   * <p>
   * The method validates the request, finds the corresponding command,
   * and delegates execution to it. If the request is invalid, the command
   * is unsupported, or an exception occurs during execution, an error response is
   * returned.
   *
   * @param request the request to execute
   * @return a {@code Response} containing the execution result or an error
   *         message
   */
  public Response execute(Request request) {
    if (request == null) {
      logger.warn("Received null request.");
      return Response.error("Request is null.");
    }

    if (request.getCommandType() == null) {
      logger.warn("Received request with null command type.");
      return Response.error("Command type is null.");
    }

    CommandType type = request.getCommandType();
    Command command = this.commands.get(type);

    if (command == null) {
      logger.warn("Unsupported command requested: {}", type);
      return Response.error("Unsupported command: " + request.getCommandType());
    }

    logger.info("Executing command: {}", type);

    try {
      Response response = command.exec(request);
      logger.info("Command {} executed. success={}", type, response.isSuccess());
      return response;
    } catch (Exception e) {
      logger.error("Server error while executing command {}: {}", type, e.getMessage(), e);
      return Response.error("Server error while executing command: " + e.getMessage());
    }
  }

  /**
   * Returns a copy of the registered command map.
   *
   * @return a new map containing all registered commands
   */
  public Map<CommandType, Command> getCommands() {
    return new EnumMap<>(this.commands);
  }
}
