package io;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Visitor extends SimpleFileVisitor<Path> {
    private final List<Path> list;
    private final Predicate<Path> predicate;

    public Visitor(Predicate<Path> predicate) {
        this.list = new ArrayList<>();
        this.predicate = predicate;
    }

    public List<Path> getList() {
        return list;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
        if (predicate.test(file)) {
            list.add(file.toAbsolutePath());
        }
        return FileVisitResult.CONTINUE;
    }
}
