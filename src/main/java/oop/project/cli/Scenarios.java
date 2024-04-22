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
     *  - {@code left: Integer}
     *  - {@code right: Integer}
     */
    private static Map<String, Object> add(String arguments) {
        List<String> integers = List.of(arguments.split(" "));
        if (integers.size() != 2)
            throw new IllegalArgumentException("This command requires 2 arguments (valid integers).");
        try {
            int left = Integer.parseInt(integers.get(0));
            int right = Integer.parseInt(integers.get(1));
            return Map.of("left", left, "right", right);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Arguments must be valid integers.");
        }
    }

    /**
     * Takes two <em>named</em> arguments:
     *  - {@code left: Double} (required) (default = 0)
     *  - {@code right: Double} (required)
     */
    static Map<String, Object> sub(String arguments) {
        Optional<Double> left = Optional.empty();
        Double right = null;
        boolean hasLeft = false;
        boolean hasRight = false;

        for (String arg : arguments.split(" ")) {
            if (arg.equals("--left")) {
                hasLeft = true;
            } else if (arg.equals("--right")) {
                hasRight = true;
            } else if (hasLeft) {
                try {
                    left = Optional.of(Double.parseDouble(arg));
                    hasLeft = false;
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid number provided for --left.");
                }
            } else if (hasRight) {
                try {
                    right = Double.parseDouble(arg);
                    hasRight = false;
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid number provided for --right");
                }
            } else {
                throw new IllegalArgumentException("Unexpected argument: " + arg);
            }
        }

        if (right == null) {
            throw new IllegalArgumentException("--right argument must be present and followed by a number.");
        }

        var leftReturn = left.isPresent() ? left.get() : Optional.empty();
        return Map.of("left", leftReturn, "right", right);
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
        //TODO: Parse arguments and extract values.
        System.out.println(arguments);

        String[] parts = arguments.split("-");

        // Extract year, month, and day from the parts array
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        // Create a LocalDate object using the extracted values
        LocalDate date = LocalDate.of(year, month, day);

        return Map.of("date", date);
    }

    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a
    //good place to test/showcase your functionality in context.

}
