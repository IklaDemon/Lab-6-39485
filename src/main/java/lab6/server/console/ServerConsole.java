package lab6.server.console;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lab6.server.collection.CollectionManager;
import lab6.server.network.UDPServer;

public class ServerConsole implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(ServerConsole.class);

  private final CollectionManager collectionManager;
  private final UDPServer server;

  public ServerConsole(CollectionManager collectionManager, UDPServer server) {
    if (collectionManager == null) {
      throw new NullPointerException("collectionManager is null");
    }
    if (server == null) {
      throw new NullPointerException("server is null");
    }

    this.collectionManager = collectionManager;
    this.server = server;
  }

  @Override
  public void run() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      try {
        String line = scanner.nextLine().strip();

        switch (line) {
          case "":
            continue;

          case "help":
            System.out.println("Server-only commands:");
            System.out.println("  save - save collection to file");
            System.out.println("  exit - save collection and stop server");
            break;

          case "save":
            if (collectionManager.save()) {
              logger.info("Collection saved by server-only command.");
              System.out.println("Collection saved.");
            } else {
              logger.warn("Server-only save command failed.");
              System.out.println("Failed to save collection.");
            }
            break;

          case "exit":
            logger.info("Server-only exit command received.");
            if (collectionManager.save()) {
              logger.info("Collection saved before server shutdown.");
            } else {
              logger.warn("Failed to save collection before shutdown.");
            }
            server.stop();
            server.close();
            System.out.println("Server stopped.");
            scanner.close();
            return;

          default:
            System.out.println("Unknown server command. Type 'help'.");
            break;
        }

      } catch (NoSuchElementException e) {
        logger.warn("Server console input closed.");
        scanner.close();
        return;
      } catch (Exception e) {
        logger.error("Server console error: {}", e.getMessage(), e);
      }
    }
  }
}
