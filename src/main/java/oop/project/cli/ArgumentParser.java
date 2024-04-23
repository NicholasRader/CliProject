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

        /**
         * Constructs an Argument object for positional arguments
         *
         * @param index the index for the argument
         * @param description description of the argument
         * @param required whether the argument is required
         * @param defaultValue value if one is not passed in
         * @return the argument object
         */
        public Argument(int index, String description, boolean required, String defaultValue) {
            this.flag = null;
            this.index = index;
            this.description = description;
            this.required = required;
            this.value = defaultValue;
        }

        /**
         * Constructs an Argument object for named arguments
         *
         * @param flag the index for the argument
         * @param description description of the argument
         * @param required whether the argument is required
         * @param defaultValue value if one is not passed in
         * @return the argument object
         */
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

    /**
     * Constucts a positional argument object and stores it
     *
     * @param index the index for the argument
     * @param description description of the argument
     * @param required whether the argument is required
     * @param defaultValue value if one is not passed in
     */
    public void addPositionalArgument(int index, String description, boolean required, String defaultValue) {
        positionalArguments.put(index, new Argument(index, description, required, defaultValue));
    }

    /**
     * Constucts a named argument object and stores it
     *
     * @param flag the flag for the argument
     * @param description description of the argument
     * @param required whether the argument is required
     * @param defaultValue value if one is not passed in
     */
    public void addNamedArgument(String flag, String description, boolean required, String defaultValue) {
        namedArguments.put(flag, new Argument(flag, description, required, defaultValue));
    }

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

    public String getNamedArgumentValue(String flag) {
        return namedArguments.get(flag).getValue();
    }
    public String getPositionalArgumentValue(int index) {
        return positionalArguments.get(index).getValue();
    }

}