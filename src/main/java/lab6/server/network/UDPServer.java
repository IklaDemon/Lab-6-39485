package lab6.server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lab6.common.network.Response;
import lab6.server.command.CommandExecutor;

/**
 * UDP server for receiving client requests and sending responses.
 *
 * <p>
 * This class manages the server datagram channel, receives incoming
 * requests, delegates their execution to the command executor, and sends
 * the resulting responses back to the clients.
 */
public class UDPServer {

  private static final Logger logger = LoggerFactory.getLogger(UDPServer.class);
  private final int port;
  private final int bufferSize;
  private final DatagramChannel channel;
  private final RequestReceiver requestReceiver;
  private final ResponseSender responseSender;
  private final CommandExecutor commandExecutor;
  private volatile boolean running;

  /**
   * Creates a new UDP server instance.
   *
   * @param port            the port on which the server will listen
   * @param bufferSize      the size of the buffer used for receiving and sending
   *                        datagrams
   * @param commandExecutor the command executor used to process incoming requests
   * @throws NullPointerException if {@code commandExecutor} is {@code null}
   * @throws IOException          if an I/O error occurs while opening or
   *                              configuring the channel
   */
  public UDPServer(int port, int bufferSize, CommandExecutor commandExecutor) throws IOException {
    if (commandExecutor == null) {
      throw new NullPointerException("commandExecutor is null");
    }

    this.port = port;
    this.bufferSize = bufferSize;
    this.commandExecutor = commandExecutor;
    this.requestReceiver = new RequestReceiver();
    this.responseSender = new ResponseSender();
    this.channel = DatagramChannel.open();
    this.channel.configureBlocking(false);
    this.running = false;
  }

  /**
   * Starts the UDP server.
   *
   * <p>
   * The method binds the server channel to the configured port, enters
   * the main server loop, receives requests from clients, executes them
   * through the command executor, and sends responses back to the clients.
   *
   * @throws Exception if a fatal error occurs while starting the server
   */
  public void start() throws Exception {
    channel.bind(new InetSocketAddress(port));
    running = true;

    // System.out.println("UDP server started on port " + port);
    logger.info("UDP server bound on port {}", port);
    logger.info("Server loop started.");

    while (running) {
      try {
        ReceivedRequest receivedRequest = requestReceiver.receive(channel, bufferSize);

        if (receivedRequest == null) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Server thread interrupted.");
            break;
          }
          continue;
        }

        logger.info("Received request from {}", receivedRequest.getClientAddress());
        Response response = commandExecutor.execute(receivedRequest.getRequest());

        responseSender.send(
            channel,
            receivedRequest.getClientAddress(),
            response,
            bufferSize);
        logger.info("Response sent to {}", receivedRequest.getClientAddress());

      } catch (Exception e) {
        logger.error("Server loop error: {}", e.getMessage(), e);
        System.out.println("Server loop error: " + e.getMessage());
      }
    }
  }

  /**
   * Stops the server loop.
   *
   * <p>
   * After this method is called, the main server loop will terminate.
   */
  public void stop() {
    running = false;
    logger.info("Server stop requested.");
  }

  /**
   * Closes the server datagram channel.
   *
   * <p>
   * If an error occurs while closing the channel, it is handled internally.
   */
  public void close() {
    try {
      channel.close();
      logger.info("Server channel closed.");
    } catch (IOException e) {
      System.out.println("Error while closing server channel: " + e.getMessage());
      logger.error("Error while closing server channel: {}", e.getMessage(), e);
    }
  }
}
