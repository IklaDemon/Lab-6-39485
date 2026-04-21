package lab6.client.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import lab6.common.network.Request;
import lab6.common.network.Response;
import lab6.common.util.SerializationUtils;

/**
 * UDP client for sending requests to the server and receiving responses.
 *
 * <p>
 * This class manages a non-blocking {@code DatagramChannel}, sends
 * serialized {@code Request} objects to the server, and waits for
 * serialized {@code Response} objects within a specified timeout.
 */
public class UdpClient {
  private final InetSocketAddress serverAddress;
  private final DatagramChannel channel;
  private final int bufferSize;
  private final long timeoutMillis;

  /**
   * Creates a new UDP client.
   *
   * @param host          the server host name or IP address
   * @param port          the server port
   * @param bufferSize    the size of the buffer used to receive server responses
   * @param timeoutMillis the maximum time to wait for a server response in
   *                      milliseconds
   * @throws IOException if an I/O error occurs while opening or configuring the
   *                     channel
   */
  public UdpClient(String host, int port, int bufferSize, long timeoutMillis) throws IOException {
    this.serverAddress = new InetSocketAddress(host, port);
    this.bufferSize = bufferSize;
    this.timeoutMillis = timeoutMillis;
    this.channel = DatagramChannel.open();
    this.channel.configureBlocking(false);
  }

  /**
   * Sends a request to the server and waits for a response.
   *
   * <p>
   * The request is serialized and sent through the UDP channel.
   * The method then waits until a response is received or the timeout expires.
   *
   * @param request the request to send
   * @return the server response if received successfully; otherwise an error
   *         response
   *         describing the failure reason
   */
  public Response send(Request request) {
    try {
      byte[] data = SerializationUtils.serialize(request);
      ByteBuffer sendBuffer = ByteBuffer.wrap(data);
      channel.send(sendBuffer, serverAddress);

      long deadline = System.currentTimeMillis() + timeoutMillis;
      ByteBuffer receiveBuffer = ByteBuffer.allocate(bufferSize);

      while (System.currentTimeMillis() < deadline) {
        receiveBuffer.clear();
        InetSocketAddress from = (InetSocketAddress) channel.receive(receiveBuffer);

        if (from != null) {
          receiveBuffer.flip();
          byte[] responseBytes = new byte[receiveBuffer.remaining()];
          receiveBuffer.get(responseBytes);
          return SerializationUtils.deserialize(responseBytes, Response.class);
        }

        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return Response.error("Client thread was interrupted.");
        }
      }

      return Response.error("Server is temporarily unavailable or did not respond in time.");

    } catch (Exception e) {
      return Response.error("Client network error: " + e.getMessage());
    }
  }

  /**
   * Closes the UDP channel used by this client.
   *
   * @throws IOException if an I/O error occurs while closing the channel
   */
  public void close() throws IOException {
    channel.close();
  }
}
