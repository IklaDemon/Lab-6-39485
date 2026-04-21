package lab6.server.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lab6.server.collection.CollectionManager;
import lab6.server.command.CommandExecutor;
import lab6.server.io.XMLParser;
import lab6.server.io.XMLWriter;
import lab6.server.network.UDPServer;

/**
 * Entry point of the server application.
 *
 * <p>
 * This class initializes the XML reader and writer, loads the collection,
 * creates the command executor and UDP server, and starts the server.
 * It also registers a shutdown hook to save the collection before exit.
 */
public class ServerMain {

  private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

  /**
   * Starts the server application.
   *
   * <p>
   * The method reads the XML file path from the {@code path_to_xml}
   * environment variable, initializes all server components, registers
   * a shutdown hook for graceful termination, and starts the UDP server.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    UDPServer server = null;

    try {
      logger.info("Server startup requested.");

      String env = System.getenv("path_to_xml");

      if (env == null || env.isBlank()) {
        logger.error("Environment variable path_to_xml is not set.");
        System.out.println("No environment variable set");
        System.out.println("Please set 'export path_to_xml=[path_to_xml]' on Linux Bash/Fish.");
        System.out.println("Please set 'set path_to_xml=[path_to_xml]'    on Windows CMD.");
        System.out.println("Please set '$env:path_to_xml=[path_to_xml]'   on Windows PowerShell.");
        return;
      }

      logger.info("Using XML file: {}", env);

      XMLParser xmlParser = new XMLParser(env);
      XMLWriter xmlWriter = new XMLWriter(env);
      CollectionManager collectionManager = new CollectionManager(xmlParser, xmlWriter, env);

      CommandExecutor commandExecutor = new CommandExecutor(collectionManager);

      server = new UDPServer(5555, 65507, commandExecutor);

      UDPServer finalServer = server;
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        logger.info("Shutdown detected. Saving collection and stopping server...");
        System.out.println("\nShutdown detected. Saving collection and stopping server...");
        try {
          finalServer.stop();
          collectionManager.save();
          logger.info("Collection saved successfully.");

        } catch (Exception e) {
          logger.error("Error during shutdown save: {}", e.getMessage(), e);
          System.out.println("Error during shutdown save: " + e.getMessage());
        } finally {
          finalServer.close();
          logger.info("Server channel closed.");
        }
      }));

      server.start();

    } catch (Exception e) {
      System.out.println("Server startup error: " + e.getMessage());
      logger.error("Server startup error: {}", e.getMessage(), e);
      if (server != null) {
        server.close();
      }
    }
  }
}
