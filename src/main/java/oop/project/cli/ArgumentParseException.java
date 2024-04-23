package oop.project.cli;

/**
 * Creates an exception for argument parsing
 */
public class ArgumentParseException extends Exception {
    public ArgumentParseException(String message) {
        super(message);
    }
}
