package io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ArgsName {
    private final Map<String, String> values = new HashMap<>();

    public Map<String, String> parse(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("Number of arguments must be at least 4");
        }
        argsValidation(args);
        for (String key : values.keySet()) {
            if (!("d").equals(key) && !("n").equals(key) && !("t").equals(key) && !("o").equals(key)) {
                throw new IllegalArgumentException(
                        String.format("Error: This argument '%s' has wrong name", key));
            }
        }
        Path start = Paths.get(values.get("d"));
        pathValidation(start);
        String type = values.get("t");
        typeValidation(type);
        String fileName = values.get("o");
        fileNameValidation(fileName);
        return values;
    }

    private void argsValidation(String[] args) {
        for (String line : args) {
            if (!line.startsWith("-")) {
                throw new IllegalArgumentException(
                        String.format("Error: This argument '%s' does not start with a '-' character", line));
            }
            if (!line.contains("=")) {
                throw new IllegalArgumentException(
                        String.format("Error: This argument '%s' does not contain an equal sign", line));
            }
            String[] pair = line.split("=", 2);
            if (pair[0].length() == 1) {
                throw new IllegalArgumentException(
                        String.format("Error: This argument '%s' does not contain a key", line));
            }
            if (pair[1].isEmpty()) {
                throw new IllegalArgumentException(
                        String.format("Error: This argument '%s' does not contain a value", line));
            }
            values.put(pair[0].substring(1).toLowerCase(), pair[1].toLowerCase());
        }
    }

    private static void pathValidation(Path start) {
        if (!Files.exists(start)) {
            throw new IllegalArgumentException(String.format("Not exist %s", start.toAbsolutePath()));
        }
        if (!Files.isDirectory(start)) {
            throw new IllegalArgumentException(String.format("Not directory %s", start.toAbsolutePath()));
        }
    }

    private static void typeValidation(String type) {
        if (!"mask".equals(type) && !"regex".equals(type)) {
            throw new IllegalArgumentException(String.format("Wrong type of search %s", type));
        }
    }

    private static void fileNameValidation(String fileName) {
        if (!fileName.endsWith(".txt")) {
            throw new IllegalArgumentException(String.format("Incorrect extension %s", fileName));
        }
    }
}
