package lab6.server.network;

import java.net.SocketAddress;
import lab6.common.network.Request;

/**
 * Represents a request received by the server together with the client address.
 *
 * <p>
 * This class stores the deserialized {@code Request} object and the
 * network address of the client that sent it.
 */
public class ReceivedRequest {
  private final Request request;
  private final SocketAddress clientAddress;

  /**
   * Creates a new {@code ReceivedRequest} object.
   *
   * @param request       the received request; must not be {@code null}
   * @param clientAddress the address of the client that sent the request;
   *                      must not be {@code null}
   * @throws NullPointerException if {@code request} is {@code null}
   *                              or if {@code clientAddress} is {@code null}
   */
  public ReceivedRequest(Request request, SocketAddress clientAddress) {
    if (request == null) {
      throw new NullPointerException("request is null");
    }
    if (clientAddress == null) {
      throw new NullPointerException("clientAddress is null");
    }
    this.request = request;
    this.clientAddress = clientAddress;
  }

  /**
   * Returns the received request.
   *
   * @return the request object
   */
  public Request getRequest() {
    return request;
  }

  /**
   * Returns the address of the client that sent the request.
   *
   * @return the client socket address
   */
  public SocketAddress getClientAddress() {
    return clientAddress;
  }
}
