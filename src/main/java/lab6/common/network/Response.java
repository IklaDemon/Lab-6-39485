package lab6.common.network;

import java.io.Serializable;

/**
 * Represents a server response sent through the network.
 *
 * <p>
 * A response contains the execution status and a message
 * describing the result of the request processing.
 */
public class Response implements Serializable {

  private static final long serialVersionUID = 1L;

  private final boolean success;
  private final String message;

  /**
   * Creates a new {@code Response} object.
   *
   * @param success {@code true} if the request was processed successfully,
   *                {@code false} otherwise
   * @param message the response message
   */
  public Response(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  /**
   * Returns whether the request was processed successfully.
   *
   * @return {@code true} if the operation was successful, {@code false} otherwise
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * Returns the response message.
   *
   * @return the response message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Creates a successful response with the specified message.
   *
   * @param message the response message
   * @return a successful {@code Response} object
   */
  public static Response ok(String message) {
    return new Response(true, message);
  }

  /**
   * Creates an error response with the specified message.
   *
   * @param message the response message
   * @return an unsuccessful {@code Response} object
   */
  public static Response error(String message) {
    return new Response(false, message);
  }
}
