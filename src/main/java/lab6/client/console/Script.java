package lab6.client.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a script loader for reading and expanding command scripts.
 *
 * <p>
 * This class supports nested script execution, keeps track of visited
 * script files to prevent recursion, and returns the resulting list of
 * commands.
 */
public class Script {
  private File scriptFilePath;
  private Set<String> history;
  private ArrayList<String> commands;

  /**
   * Creates a script handler with empty command history and command list.
   */
  public Script() {
    this.history = new HashSet<String>();
    this.commands = new ArrayList<String>();
  }

  /**
   * Loads the script file from the specified path.
   *
   * @param scriptPath path to the script file
   * @throws NullPointerException     if {@code scriptPath} is null
   * @throws IllegalArgumentException if the path is empty, invalid, or unreadable
   */
  public void load(String scriptPath) {
    this.scriptFilePath = this.getFile(scriptPath);
  }

  /**
   * Returns the list of commands read from the loaded script.
   *
   * <p>
   * The method clears previous state, reads the script file, expands nested
   * {@code execute_script} instructions, and prevents infinite recursion.
   *
   * @return list of commands from the script
   * @throws IllegalArgumentException if the canonical path of the script cannot
   *                                  be obtained
   */
  public ArrayList<String> getCommands() {
    commands.clear();
    history.clear();

    if (scriptFilePath != null) {
      try {
        history.add(scriptFilePath.getCanonicalPath());
        this.readScript(scriptFilePath);
      } catch (IOException e) {
        throw new IllegalArgumentException("Error while getting canonical path of: " + scriptFilePath.getName());
      }
    }
    return new ArrayList<>(this.commands);
  }

  /**
   * Reads commands from the specified script file.
   *
   * <p>
   * If an {@code execute_script} command is encountered, the referenced script
   * is read recursively unless it would create a loop.
   *
   * @param script script file to read
   */
  private void readScript(File script) {
    String canonicalPath;
    try {
      canonicalPath = script.getCanonicalPath();
    } catch (IOException e) {
      throw new IllegalArgumentException("Error while getting canonical path of: " + script.getName());
    }
    try (Scanner scanner = new Scanner(script)) {
      while (scanner.hasNextLine()) {
        String command = scanner.nextLine().strip();
        String[] commandParts = command.split("\\s+");

        if (commandParts.length == 2 && commandParts[0].equals("execute_script")) {
          try {
            File nextScript = this.getFile(new File(script.getParentFile(), commandParts[1]).getAbsolutePath());
            if (this.history.add(nextScript.getCanonicalPath()) == true)
              this.readScript(nextScript);
            else {
              System.out.print("Loop detected with instruction: " + command + "\n");
            }
          } catch (IOException | IllegalArgumentException | NullPointerException e) {
            System.out.print("Failed to open script in command: " + command + ". " + e.getMessage() + "\n");
          }
        } else {
          this.commands.add(command);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.print("File " + script.getName() + " not found.\n");
    } finally {
      history.remove(canonicalPath);
    }
  }

  /**
   * Splits an inline dragon representation into separate field values.
   *
   * @param inlineDragon comma-separated dragon data
   * @return list of parsed dragon fields
   */
  public static ArrayList<String> parseInlineDragon(String inlineDragon) {
    // ID,name,coordinates.x,coordinates.y,creationDate,age,wingspan,type,character,cave.
    // ---1----2-------------3--------------------------4---5--------6----7---------8
    ArrayList<String> dragonParts = new ArrayList<>();

    for (String string : inlineDragon.split(",")) {
      dragonParts.add(string);
    }

    return dragonParts;
  }

  /**
   * Returns a validated file object for the specified path.
   *
   * @param filePath path to the file
   * @return readable file object
   * @throws NullPointerException     if {@code filePath} is null
   * @throws IllegalArgumentException if the path is empty, invalid, or not
   *                                  readable
   */
  private File getFile(String filePath) {
    if (filePath == null) {
      throw new NullPointerException("filePath is null. Should not be null");
    }
    if (filePath.equals("")) {
      throw new IllegalArgumentException("filePath = " + filePath + ". Should not be empty");
    }
    File file = new File(filePath);
    if (file.exists() && !file.isDirectory()) {
      if (!file.canRead()) {
        throw new IllegalArgumentException("file is not readable");
      }
      return file;
    } else {
      throw new IllegalArgumentException("filePath does not exists or is a directory");
    }
  }
}
