package lab6.common.network;

import java.io.Serializable;
import lab6.common.enums.CommandType;
import lab6.common.model.Dragon;

/**
 * Represents a client request sent through the network.
 *
 * <p>
 * A request stores the command type and optional arguments such as
 * an identifier, a cave value, or a {@code Dragon} object.
 */
public class Request implements Serializable {

  private static final long serialVersionUID = 1L;

  private final CommandType commandType;
  private final Long id;
  private final Double caveValue;
  private final Dragon dragon;

  /**
   * Creates a new {@code Request} object with the specified command data.
   *
   * @param commandType the type of command to execute
   * @param id          the identifier associated with the request, or
   *                    {@code null} if not used
   * @param caveValue   the cave value associated with the request, or
   *                    {@code null} if not used
   * @param dragon      the dragon associated with the request, or {@code null} if
   *                    not used
   */
  public Request(CommandType commandType, Long id, Double caveValue, Dragon dragon) {
    this.commandType = commandType;
    this.id = id;
    this.caveValue = caveValue;
    this.dragon = dragon;
  }

  /**
   * Returns the command type of this request.
   *
   * @return the command type
   */
  public CommandType getCommandType() {
    return commandType;
  }

  /**
   * Returns the identifier associated with this request.
   *
   * @return the identifier, or {@code null} if no identifier was provided
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the cave value associated with this request.
   *
   * @return the cave value, or {@code null} if no cave value was provided
   */
  public Double getCaveValue() {
    return caveValue;
  }

  /**
   * Returns the dragon associated with this request.
   *
   * @return the dragon, or {@code null} if no dragon was provided
   */
  public Dragon getDragon() {
    return dragon;
  }

  /**
   * Creates a simple request containing only the command type.
   *
   * @param type the command type
   * @return a new request without additional arguments
   */
  public static Request simple(CommandType type) {
    return new Request(type, null, null, null);
  }

  /**
   * Creates a request containing a command type and an identifier.
   *
   * @param type the command type
   * @param id   the identifier associated with the request
   * @return a new request containing the command type and id
   */
  public static Request withId(CommandType type, Long id) {
    return new Request(type, id, null, null);
  }

  /**
   * Creates a request containing a command type and a dragon object.
   *
   * @param type   the command type
   * @param dragon the dragon associated with the request
   * @return a new request containing the command type and dragon
   */
  public static Request withDragon(CommandType type, Dragon dragon) {
    return new Request(type, null, null, dragon);
  }

  /**
   * Creates a request containing a command type, an identifier, and a dragon
   * object.
   *
   * @param type   the command type
   * @param id     the identifier associated with the request
   * @param dragon the dragon associated with the request
   * @return a new request containing the command type, id, and dragon
   */
  public static Request withIdAndDragon(CommandType type, Long id, Dragon dragon) {
    return new Request(type, id, null, dragon);
  }

  /**
   * Creates a request containing a command type and a cave value.
   *
   * @param type      the command type
   * @param caveValue the cave value associated with the request
   * @return a new request containing the command type and cave value
   */
  public static Request withCaveValue(CommandType type, Double caveValue) {
    return new Request(type, null, caveValue, null);
  }
}
