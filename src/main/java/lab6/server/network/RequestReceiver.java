package lab6.server.network;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lab6.common.network.Request;
import lab6.common.util.SerializationUtils;

/**
 * Receives serialized client requests from a UDP channel.
 *
 * <p>
 * This class reads raw datagrams from a {@code DatagramChannel},
 * deserializes them into {@code Request} objects, and wraps them together
 * with the sender address into {@code ReceivedRequest} objects.
 */
public class RequestReceiver {

  private static final Logger logger = LoggerFactory.getLogger(RequestReceiver.class);

  /**
   * Receives a request from the specified UDP channel.
   *
   * <p>
   * The method reads one datagram from the channel, deserializes its content
   * into a {@code Request} object, and returns it together with the client
   * address.
   * If no datagram is currently available, {@code null} is returned.
   *
   * @param channel    the datagram channel used to receive data
   * @param bufferSize the size of the buffer used for receiving the datagram
   * @return a {@code ReceivedRequest} containing the request and client address,
   *         or {@code null} if no data was received
   * @throws Exception if an error occurs while receiving or deserializing the
   *                   request
   */
  public ReceivedRequest receive(DatagramChannel channel, int bufferSize) throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

    SocketAddress clientAddress = channel.receive(buffer);

    if (clientAddress == null) {
      return null;
    }

    buffer.flip();

    byte[] data = new byte[buffer.remaining()];
    buffer.get(data);
    logger.info("Datagram received from {} ({} bytes).", clientAddress, data.length);

    Request request = SerializationUtils.deserialize(data, Request.class);

    return new ReceivedRequest(request, clientAddress);
  }
}
