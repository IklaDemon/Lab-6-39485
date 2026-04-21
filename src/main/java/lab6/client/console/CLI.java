package lab6.client.console;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;

import lab6.client.network.UdpClient;
import lab6.common.enums.CommandType;
import lab6.common.enums.DragonCharacter;
import lab6.common.enums.DragonType;
import lab6.common.model.Coordinates;
import lab6.common.model.Dragon;
import lab6.common.model.DragonCave;
import lab6.common.model.DragonMaker;
import lab6.common.network.Request;
import lab6.common.network.Response;

/**
 * Handles user interaction through the command-line interface.
 *
 * <p>
 * This class reads commands from standard input or from an internal buffer,
 * builds network requests, sends them through the UDP client, and prints
 * server responses. It also supports script execution and interactive dragon
 * creation.
 */
public class CLI {
  private Queue<String> commandBuffer;
  private UdpClient udpClient;

  /**
   * Creates a new CLI instance.
   *
   * @param udpClient the UDP client used to send requests to the server
   */
  public CLI(UdpClient udpClient) {
    this.commandBuffer = new ArrayDeque<>();
    this.udpClient = udpClient;
  }

  /**
   * Starts the command-line interface loop.
   *
   * <p>
   * The method continuously reads commands from the user or from the internal
   * command buffer, processes special commands such as {@code execute_script}
   * and {@code exit}, sends valid requests to the server, and prints the server
   * response.
   * The loop stops when the user exits the program or when the input stream is
   * closed.
   */
  public void runCLI() {
    String[] inputCommand;
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.print("> ");

      if (commandBuffer.isEmpty()) {
        try {
          commandBuffer.offer(scanner.nextLine().strip());
        } catch (NoSuchElementException e) {
          System.out.println("CTRL + D generates a NoSuchElementException with message: " + e.getMessage() + ".");
          System.out.println("App is terminated");
          scanner.close();
          return;
        } catch (IllegalStateException e) {
          System.out.println(e.getMessage());
          scanner.close();
          return;
        }
      }

      inputCommand = commandBuffer.poll().split("\\s+", 3);

      if (inputCommand[0].isBlank()) {
        continue;
      }

      if (inputCommand[0].equals("execute_script")) {
        if (inputCommand.length != 2) {
          System.out.print("Wrong number of arguments\n");
          continue;
        }
        Script script = new Script();
        try {
          script.load(inputCommand[1]);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
          continue;
        }
        for (String string : script.getCommands()) {
          commandBuffer.offer(string);
        }
        continue;
      } else if (inputCommand[0].equals("exit")) {
        break;
      }

      Request request = this.buildRequest(inputCommand, scanner);
      if (request == null) {
        continue;
      }
      Response response = udpClient.send(request);
      System.out.print(response.getMessage());
    }
    scanner.close();
  }

  /**
   * Interactively creates a {@code Dragon} object from user input.
   *
   * <p>
   * The method requests all required dragon fields one by one, validates
   * the entered values, and builds a dragon object using the builder pattern.
   * Default placeholder values are assigned to the id and creation date.
   *
   * @param scanner the scanner used to read user input
   * @return the created dragon
   */
  private Dragon getDragonFromUser(Scanner scanner) {
    Dragon.Builder dragonBuilder = new Dragon.Builder();
    Coordinates.Builder coordinateBuilder = new Coordinates.Builder();
    boolean loop = true;
    int state = 0;
    String arg;

    while (loop) {
      switch (state) {
        case 0: // - name: not null and not empty
          System.out.print("Dragon name: ");
          arg = scanner.nextLine().strip();
          if (arg.isBlank() || arg.isEmpty()) {
            System.out.println("Name should not be empty or null");
            continue;
          }
          if (arg.matches(".*\\s+.*")) {
            System.out.println("White spaces are not allowed");
            continue;
          }
          try {
            dragonBuilder.name(arg.strip());
            state = 1;
          } catch (Exception e) {
            System.out.println("Illegal Name");
          }
          break;
        case 1: // coordinate x: any number. long
          System.out.print("Coordinate. x: ");
          arg = scanner.nextLine().strip();
          try {
            Long.parseLong(arg);
          } catch (NumberFormatException e) {
            System.out.println("Type a valid number.");
            continue;
          }
          try {
            coordinateBuilder.x(arg.strip());
            state = 2;
          } catch (Exception e) {
            System.out.println("Type a valid number.");
          }
          break;
        case 2: // coordinate y: not null. Long
          System.out.print("Coordinate. y: ");
          arg = scanner.nextLine().strip();
          try {
            Long.parseLong(arg);
          } catch (NumberFormatException e) {
            System.out.println("Type a valid number. Cannot be null.");
            continue;
          }
          try {
            coordinateBuilder.y(arg);
            dragonBuilder.coordinates(coordinateBuilder.build());
            state = 3;
          } catch (Exception e) {
            System.out.println("Type a valid number. Cannot be null.");
          }
          break;
        case 3: // age: can be null, > 0
          System.out.print("Age: ");
          arg = scanner.nextLine().strip();
          if (arg.isBlank() || arg.isEmpty()) {
            try {
              dragonBuilder.age(null);
              state = 4;
            } catch (Exception e) {
              System.out.println("Error with the age");
            }
          } else {
            try {
              int a = Integer.parseInt(arg);
              if (a <= 0) {
                System.out.println("Age should be > 0.");
                continue;
              }
              dragonBuilder.age(a);
              state = 4;
            } catch (NumberFormatException e) {
              System.out.println("Type a valid number. Should be > 0. Not null");
              continue;
            }
          }
          break;
        case 4: // wingspan: > 0.
          System.out.print("Wingspan: ");
          arg = scanner.nextLine().strip();
          if (arg.isBlank() || arg.isEmpty()) {
            System.out.println("Wingspan should not be empty or null.");
            continue;
          }
          try {
            Double a = Double.parseDouble(arg);
            if (a <= 0) {
              System.out.println("Wingspan should be > 0");
              continue;
            }
            dragonBuilder.wingspan(a);
            state = 5;
          } catch (NumberFormatException e) {
            System.out.println("Wingspan should not be empty or null. Should be > 0");
            continue;
          }
          break;
        case 5: // type: can be null (WATER, UNDERGROUND, AIR, FIRE)
          System.out.print("Dragon Type (WATER/UNDERGROUND/AIR/FIRE): ");
          arg = scanner.nextLine().strip();
          if (arg.isBlank() || arg.isEmpty()) {
            dragonBuilder.type(null);
            state = 6;
          } else {
            try {
              DragonType.valueOf(arg.toUpperCase());
              dragonBuilder.type(arg.toUpperCase());
              state = 6;
            } catch (Exception e) {
              System.out.println("Invalid option.");
              continue;
            }
          }
          break;
        case 6: // character: can be null (WISE, GOOD, CHAOTIC, CHAOTIC_EVIL)
          System.out.print("Dragon Character (WISE, GOOD, CHAOTIC, CHAOTIC_EVIL): ");
          arg = scanner.nextLine().strip();
          if (arg.isBlank() || arg.isEmpty()) {
            dragonBuilder.character(null);
            state = 7;
          } else {
            try {
              DragonCharacter.valueOf(arg.toUpperCase());
              dragonBuilder.character(arg.toUpperCase());
              state = 7;
            } catch (Exception e) {
              System.out.println("Invalid option.");
              continue;
            }
          }
          break;
        case 7: // cave: can be null
          System.out.print("Cave (Y/n): ");
          arg = scanner.nextLine().strip();
          if (arg.isBlank() || arg.isEmpty() || arg.toLowerCase().equals("y")) {
            state = 8;
          } else if (arg.toLowerCase().equals("n")) {
            dragonBuilder.cave(null);
            state = 9;
          } else {
            continue;
          }
          break;
        case 8: // cave. number of trasures: not null > 0. Double
          System.out.print("Cave. Number of treasures: ");
          try {
            dragonBuilder.cave(new DragonCave(scanner.nextLine().strip()));
            state = 9;
          } catch (IllegalArgumentException e) {
            System.out.println("Type a valid number. Should be > 0. Not null");
            continue;
          }
          break;
        default:
          loop = false;
          break;
      }
    }
    dragonBuilder.id(1);
    dragonBuilder.creationDate(new Date(0));
    return dragonBuilder.build();
  }

  /**
   * Builds a {@code Request} object from the parsed command input.
   *
   * <p>
   * The method determines which command was entered, validates its arguments,
   * creates the corresponding request object, and returns it. If the command
   * is invalid or its arguments are incorrect, an error message is printed and
   * {@code null} is returned.
   *
   * @param inputCommand the parsed command tokens
   * @param scanner      the scanner used for interactive dragon input when needed
   * @return the created request, or {@code null} if the command is invalid
   */
  private Request buildRequest(String[] inputCommand, Scanner scanner) {
    Dragon dragon;

    switch (inputCommand[0]) {
      case "help":
        return Request.simple(CommandType.HELP);

      case "info":
        return Request.simple(CommandType.INFO);

      case "show":
        return Request.simple(CommandType.SHOW);

      case "clear":
        return Request.simple(CommandType.CLEAR);

      case "print_field_descending_character":
        return Request.simple(CommandType.PRINT_FIELD_DESCENDING_CHARACTER);

      case "print_descending":
        return Request.simple(CommandType.PRINT_DESCENDING);

      case "remove_by_id":
        try {
          Long id = Long.parseLong(inputCommand[1]);
          return Request.withId(CommandType.REMOVE_BY_ID, id);
        } catch (IndexOutOfBoundsException e) {
          System.out.println("ID is needed.");
          break;
        } catch (NumberFormatException e) {
          System.out.println("Incorrect number format.");
          break;
        }

      case "count_greater_than_cave":
        try {
          double numberOfTreasures = Double.parseDouble(inputCommand[1]);
          return Request.withCaveValue(CommandType.COUNT_GREATER_THAN_CAVE, numberOfTreasures);
        } catch (IndexOutOfBoundsException e) {
          System.out.println("Number of treasures (double number) is needed.");
          break;
        } catch (NumberFormatException e) {
          System.out.println("Incorrect number format.");
          break;
        }

      case "add":
        try {
          dragon = this.getDragon(inputCommand, CommandType.ADD, scanner);
          return Request.withDragon(CommandType.ADD, dragon);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
          break;
        }

      case "add_if_max":
        try {
          dragon = this.getDragon(inputCommand, CommandType.ADD_IF_MAX, scanner);
          return Request.withDragon(CommandType.ADD_IF_MAX, dragon);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
          break;
        }

      case "add_if_min":
        try {
          dragon = this.getDragon(inputCommand, CommandType.ADD_IF_MIN, scanner);
          return Request.withDragon(CommandType.ADD_IF_MIN, dragon);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
          break;
        }

      case "remove_lower":
        try {
          dragon = this.getDragon(inputCommand, CommandType.REMOVE_LOWER, scanner);
          return Request.withDragon(CommandType.REMOVE_LOWER, dragon);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
          break;
        }

      case "update":
        try {
          Long id = Long.parseLong(inputCommand[1]);
          dragon = this.getDragon(inputCommand, CommandType.UPDATE, scanner);
          return Request.withIdAndDragon(CommandType.UPDATE, id, dragon);
        } catch (IndexOutOfBoundsException e) {
          System.out.println("ID is needed.");
          break;
        } catch (NumberFormatException e) {
          System.out.println("Incorrect number format.");
          break;
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
          break;
        }

      default:
        System.out.println("Unknown command.");
        return null;
    }

    return null;
  }

  /**
   * Creates a {@code Dragon} object for commands that accept dragon data.
   *
   * <p>
   * The dragon can be provided either inline as a command argument or
   * interactively through user input. Supported commands are
   * {@code add}, {@code update}, {@code add_if_max}, {@code add_if_min},
   * and {@code remove_lower}.
   *
   * @param inputCommand the parsed command tokens
   * @param commandType  the type of command being processed
   * @param scanner      the scanner used for interactive dragon input
   * @return the created dragon
   * @throws IllegalArgumentException if the command does not support dragon input
   *                                  or if the number or format of arguments is
   *                                  invalid
   */
  private Dragon getDragon(String[] inputCommand, CommandType commandType, Scanner scanner) {
    switch (commandType) {
      // these are the commands that can have an inline Dragon argument (no more than
      // 2 arguments including the command)
      case ADD:
      case ADD_IF_MAX:
      case ADD_IF_MIN:
      case REMOVE_LOWER:
        if (inputCommand.length == 2) {
          DragonMaker dragonMaker = new DragonMaker();
          try {
            return dragonMaker.make(inputCommand[1], 1, new Date(0));
          } catch (IllegalArgumentException e) {
            System.out.println(dragonMaker.getErrors());
            throw new IllegalArgumentException("Problem while making Dragon");
          }
        } else if (inputCommand.length == 1) {
          return this.getDragonFromUser(scanner);
        } else {
          throw new IllegalArgumentException("Invalid number of arguments\n");
        }

        // these are the commands that can have an inline Dragon argument (no more than
        // 3 arguments including the command)
      case UPDATE:
        if (inputCommand.length == 3) {
          DragonMaker dragonMaker = new DragonMaker();
          try {
            return dragonMaker.make(inputCommand[2], 1, new Date(0));
          } catch (IllegalArgumentException e) {
            System.out.println(dragonMaker.getErrors());
            throw new IllegalArgumentException("Problem while making Dragon");
          }
        } else if (inputCommand.length == 2) {
          return this.getDragonFromUser(scanner);
        } else {
          throw new IllegalArgumentException("Invalid number of arguments\n");
        }

      default:
        throw new IllegalArgumentException("This command does not support inline dragon\n");
    }
  }
}
