package lab6.server.network;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lab6.common.network.Response;
import lab6.common.util.SerializationUtils;

/**
 * Sends serialized server responses to clients through a UDP channel.
 *
 * <p>
 * This class converts {@code Response} objects into byte arrays
 * and sends them to the specified client address. If the serialized
 * response exceeds the maximum UDP packet size, a fallback error response is
 * sent instead.
 */
public class ResponseSender {

  private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);

  /**
   * Sends a response to the specified client through the UDP channel.
   *
   * <p>
   * The response is serialized before sending. If its size exceeds
   * the allowed packet size, an error response is serialized and sent instead.
   *
   * @param channel       the datagram channel used to send the response
   * @param clientAddress the address of the client that should receive the
   *                      response
   * @param response      the response to send
   * @param maxPacketSize the maximum allowed UDP packet size in bytes
   * @throws Exception if an error occurs while serializing or sending the
   *                   response
   */
  public void send(DatagramChannel channel, SocketAddress clientAddress, Response response, int maxPacketSize)
      throws Exception {
    byte[] data = SerializationUtils.serialize(response);

    if (data.length > maxPacketSize) {
      logger.warn(
          "Response for {} is too large ({} bytes, max {}). Sending fallback error response.",
          clientAddress,
          data.length,
          maxPacketSize);
      Response fallback = Response.error("Response is too large to be sent via UDP.");
      data = SerializationUtils.serialize(fallback);
    }

    ByteBuffer buffer = ByteBuffer.wrap(data);
    int sentBytes = channel.send(buffer, clientAddress);

    logger.info(
        "Response sent to {} ({} bytes, success={}, messageLength={}).",
        clientAddress,
        sentBytes,
        response.isSuccess(),
        response.getMessage() == null ? 0 : response.getMessage().length());
  }
}
