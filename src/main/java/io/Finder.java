package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Finder {
    private static List<Path> filteredData = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ArgsName argsName = new ArgsName();
        Map<String, String> argsMap = argsName.parse(args);
        String start = argsMap.get("d");
        String condition = argsMap.get("n");
        String type = argsMap.get("t");
        String out = argsMap.get("o");
        Predicate<Path> predicate = createPredicate(type, condition);
        Visitor visitor = new Visitor(predicate);
        Files.walkFileTree(Path.of(start), visitor);
        filteredData = visitor.getList();
        writeToFile(out);
    }

    private static Predicate<Path> createPredicate(String type, String condition) {
        Predicate<Path> predicate = null;
        if ("regex".equalsIgnoreCase(type)) {
            Pattern regexPattern = Pattern.compile(condition);
            predicate = path -> regexPattern.matcher(path.getFileName().toString()).matches();
        } else if ("mask".equalsIgnoreCase(type)) {
            String regexFromMask = maskToRegex(condition);
            Pattern maskPattern = Pattern.compile(regexFromMask);
            return path -> maskPattern.matcher(path.getFileName().toString()).matches();
        }
        return predicate;
    }

    private static String maskToRegex(String mask) {
        return mask.replace(".", "\\.")
                .replace("*", ".*")
                .replace("?", ".");
    }

    public static void writeToFile(String out) throws IOException {
        File fileOne = new File("data/" + out);
        fileOne.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileOne, true))) {
            filteredData.forEach(writer::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
