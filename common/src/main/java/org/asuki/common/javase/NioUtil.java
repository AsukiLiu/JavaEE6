package org.asuki.common.javase;

import static org.asuki.common.exception.CommonError.CANNOT_BE_INSTANCED;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.asuki.common.exception.CommonError;

public final class NioUtil {

    private NioUtil() {
        // prevent Reflection
        throw new CommonError(CANNOT_BE_INSTANCED);
    }

    public static void saveFileByIO(String filePath, String content)
            throws IOException {

        File file = new File(filePath);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes());
        }
    }

    public static void saveFile(String filePath, String content)
            throws IOException {

        // Path path = FileSystems.getDefault().getPath(filePath);
        Path path = Paths.get(filePath);
        // Files.write(path, content.getBytes());
        Files.write(path, content.getBytes(), StandardOpenOption.CREATE_NEW);
    }

    public static String readFileByIO(String filePath) throws IOException {

        File file = new File(filePath);

        if (!file.exists()) {
            return "";
        }

        byte[] content = new byte[(int) file.length()];

        try (InputStream intputStream = new FileInputStream(file)) {
            intputStream.read(content);
        }

        return new String(content);
    }

    public static String readFile(String filePath) throws IOException {

        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return "";
        }

        return new String(Files.readAllBytes(path));
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
