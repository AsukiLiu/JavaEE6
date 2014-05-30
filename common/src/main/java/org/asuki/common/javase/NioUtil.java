package org.asuki.common.javase;

import static org.asuki.common.exception.CommonError.CANNOT_BE_INSTANCED;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.asuki.common.exception.CommonError;

public final class NioUtil {

    private NioUtil() {
        // Reflectionを防ぐ
        throw new CommonError(CANNOT_BE_INSTANCED);
    }

    public static void stringToFile(String filePath, String content)
            throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    public static String fileToString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static void deleteRecursively(String directoryPath)
            throws IOException {

        Path path = Paths.get(directoryPath);

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file,
                    BasicFileAttributes attrs) throws IOException {

                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {

                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
    }

}
