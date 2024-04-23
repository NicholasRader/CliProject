package oop.project.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArgumentParser {

    public static class Argument {
        private final String flag;
        public String getFlag() {
            return flag;
        }

        private final int index;
        public int getIndex() {
            return index;
        }

        private final String description;
        public String getDescription() {
            return description;
        }

        private final boolean required;
        public boolean isRequired() {
            return required;
        }

        private String value;
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }

        // For positional
        public Argument(int index, String description, boolean required, String defaultValue) {
            this.flag = null;
            this.index = index;
            this.description = description;
            this.required = required;
            this.value = defaultValue;
        }

        // For named
        public Argument(String flag, String description, boolean required, String defaultValue) {
            this.flag = flag;
            this.index = -1;
            this.description = description;
            this.required = required;
            this.value = defaultValue;
        }
    }

    private final HashMap<String, Argument> namedArguments = new HashMap<>();
    public HashMap<String, Argument> getNamedArguments() {
        return namedArguments;
    }

    private final HashMap<Integer, Argument> positionalArguments = new HashMap<>();
    public HashMap<Integer, Argument> getPositionalArguments() {
        return positionalArguments;
    }

    private final List<String> parsedNamedArgs = new ArrayList<>();
    private final List<Integer> parsedIndexes = new ArrayList<>();

    public void addPositionalArgument(int index, String description, boolean required, String defaultValue) {
        positionalArguments.put(index, new Argument(index, description, required, defaultValue));
    }
    public void addNamedArgument(String flag, String description, boolean required, String defaultValue) {
        namedArguments.put(flag, new Argument(flag, description, required, defaultValue));
    }

    /**
     * Parses command-line arguments provided as an array of strings.
     * This method iterates through each argument in the provided array and processes them accordingly.
     * It distinguishes between named arguments (flags) prefixed with "--" and positional arguments.
     *
     * @param args The array of command-line arguments to parse.
     * @throws ArgumentParseException If there is an error parsing the arguments.
     */
    public void parseArguments(String[] args) throws ArgumentParseException {
        int i = 0;

        while (i < args.length) {
            String arg = args[i];

            // Check if it's a flag (named argument)
            if (arg.startsWith("--")) {
                String flag = arg.substring(2);
                parsedNamedArgs.add(flag);
                Argument argument = namedArguments.get(flag);
                if (argument == null) {
                    throw new ArgumentParseException("Unknown named argument: " + flag);
                }

                // Store the parsed value (if provided)
                String value = (i + 1 < args.length) ? args[i + 1] : argument.getValue();
                if (value == null && argument.isRequired()) {
                    throw new ArgumentParseException("Missing value for required argument: " + argument.getFlag());
                }
                argument.setValue(value);
                namedArguments.put(flag, argument);
                i++;
            }
            else {
                // Handle positional arguments
                if (i >= positionalArguments.size()) {
                    throw new ArgumentParseException("Too many positional arguments provided.");
                }
                parsedIndexes.add(i);
                Argument argument = positionalArguments.get(i);
                argument.setValue(arg);
                positionalArguments.put(i, argument);
            }
            i++;
        }

        // Check for missing required arguments
        for (Argument argument : namedArguments.values()) {
            if (argument.isRequired() && !parsedNamedArgs.contains(argument.getFlag())) {
                throw new ArgumentParseException("Missing required named argument '" + argument.getFlag() + "'");
            }
        }
        for (Argument argument : positionalArguments.values()) {
            if (argument.isRequired() && !parsedIndexes.contains(argument.getIndex())) {
                throw new ArgumentParseException("Missing required positional argument at index " + argument.getIndex());
            }
        }
    }

    /**
     * Retrieves the value associated with the specified named argument flag.
     * This method retrieves the value associated with the specified named argument
     * flag from the parsed arguments.
     *
     * @param flag The flag of the named argument whose value is to be retrieved.
     * @return The value associated with the specified named argument flag.
     */
    public String getNamedArgumentValue(String flag) {
        return namedArguments.get(flag).getValue();
    }

    /**
     * Retrieves the value associated with the positional argument at the specified index.
     * This method retrieves the value associated with the positional argument at the specified
     * index from the parsed arguments.
     *
     * @param index The index of the positional argument whose value is to be retrieved.
     * @return The value associated with the positional argument at the specified index.
     */
    public String getPositionalArgumentValue(int index) {
        return positionalArguments.get(index).getValue();
    }

}