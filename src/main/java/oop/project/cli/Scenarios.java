package oop.project.cli;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scenarios {

    /**
     * Parses and returns the arguments of a command (one of the scenarios
     * below) into a Map of names to values. This method is provided as a
     * starting point that works for most groups, but depending on your command
     * structure and requirements you may need to make changes to adapt it to
     * your needs - use whatever is convenient for your design.
     */
    public static Map<String, Object> parse(String command) {
        //This assumes commands follow a similar structure to unix commands,
        //e.g. `command [arguments...]`. If your project uses a different
        //structure, e.g. Lisp syntax like `(command [arguments...])`, you may
        //need to adjust this a bit to work as expected.
        var split = command.split(" ", 2);
        var base = split[0];
        var arguments = split.length == 2 ? split[1] : "";
        return switch (base) {
            case "add" -> add(arguments);
            case "sub" -> sub(arguments);
            case "sqrt" -> sqrt(arguments);
            case "calc" -> calc(arguments);
            case "date" -> date(arguments);
            default -> throw new IllegalArgumentException("Unknown command.");
        };
    }

    /**
     * Takes two positional arguments:
     *  - {@code left: integer}
     *  - {@code right: integer}
     */
    private static Map<String, Object> add(String arguments) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.addPositionalArgument(0, "The left operand (required)", true, null);
        argumentParser.addPositionalArgument(1, "The right operand (required)", true, null);

        try {
            argumentParser.parseArguments(arguments.split(" "));
            int left = Integer.parseInt(argumentParser.getPositionalArgumentValue(0));
            int right = Integer.parseInt(argumentParser.getPositionalArgumentValue(1));
            return Map.of("left", left, "right", right);
        } catch (ArgumentParseException e) {
            throw new IllegalArgumentException("Error parsing arguments: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Operands must be integers.");
        }
    }

    /**
     * Takes two <em>named</em> arguments:
     *  - {@code left: double} (required) (default = 0)
     *  - {@code right: double} (required)
     */
    static Map<String, Object> sub(String arguments) {
        ArgumentParser parser = new ArgumentParser();

        parser.addNamedArgument("left", "The left operand (optional)", false, "0");
        parser.addNamedArgument("right", "The right operand (required)", true, null);

        try {
            parser.parseArguments(arguments.split(" "));
            double left = Double.parseDouble(parser.getNamedArgumentValue("left"));
            double right = Double.parseDouble(parser.getNamedArgumentValue("right"));
            return Map.of("left", left, "right", right);
        } catch (ArgumentParseException e) {
            throw new IllegalArgumentException("Error parsing arguments: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Operands must be numbers.");
        }
    }

    /**
     * Takes one positional argument:
     *  - {@code number: <your integer type>} where {@code number >= 0}
     */
    static Map<String, Object> sqrt(String arguments) {
        //TODO: Parse arguments and extract values.
        int number = 0;
        return Map.of("number", number);
    }

    /**
     * Takes one positional argument:
     *  - {@code subcommand: "add" | "div" | "sqrt" }, aka one of these values.
     *     - Note: Not all projects support subcommands, but if yours does you
     *       may want to take advantage of this scenario for that.
     */
    static Map<String, Object> calc(String arguments) {
        //TODO: Parse arguments and extract values.
        String subcommand = "";
        return Map.of("subcommand", subcommand);
    }

    /**
     * Takes one positional argument:
     *  - {@code date: Date}, a custom type representing a {@code LocalDate}
     *    object (say at least yyyy-mm-dd, or whatever you prefer).
     *     - Note: Consider this a type that CANNOT be supported by your library
     *       out of the box and requires a custom type to be defined.
     */
    static Map<String, Object> date(String arguments) {
        //Parse arguments and extract values.

        //initialize parser
        ArgumentParser parser = new ArgumentParser();

        //consider parsing locations and their respective representations
        parser.addPositionalArgument(0, "The Year in YYYY (required)", true, null);
        parser.addPositionalArgument(1, "The Month in MM (required)", true, null);
        parser.addPositionalArgument(2, "The Day in DD (required)", true, null);

        try {
            parser.parseArguments(arguments.split("-"));  //parse based on - for dates

            int year = Integer.parseInt(parser.getPositionalArgumentValue(0));  //first positional was determined to be year
            int month = Integer.parseInt(parser.getPositionalArgumentValue(1));
            int day = Integer.parseInt(parser.getPositionalArgumentValue(2));

            LocalDate date = LocalDate.of(year, month, day);   //create the date based off of parsed values
            return Map.of("date", date);
        } catch (ArgumentParseException e) {
            throw new IllegalArgumentException("Error parsing arguments: " + e.getMessage());    // wuh oh, something went wrong :)
        }

    }

    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a
    //good place to test/showcase your functionality in context.

}
