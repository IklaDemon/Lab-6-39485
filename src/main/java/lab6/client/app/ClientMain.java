package lab6.client.app;

import lab6.client.console.CLI;
import lab6.client.network.UdpClient;

/**
 * Entry point of the client application.
 *
 * <p>
 * This class starts the UDP client, creates the command-line interface,
 * and launches the client interaction loop.
 */
public class ClientMain {
  /**
   * Starts the client application.
   *
   * <p>
   * The method creates a UDP client, initializes the command-line interface,
   * starts the CLI loop, and closes the network client when execution finishes.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    try {
      UdpClient udpClient = new UdpClient("localhost", 5555, 65507, 2000);
      CLI cli = new CLI(udpClient);
      cli.runCLI();
      udpClient.close();
    } catch (Exception e) {
      System.out.println("Client startup error: " + e.getMessage());
    }
  }
}
