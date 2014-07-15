package org.asuki.common.javase;

import static org.asuki.common.javase.NioUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

public class NioUtilTest {

    private final static String FILE_PATH = "./test.txt";

    @Test
    public void shouldTransferStringAndFile() throws IOException {

        final String expected = "This is test";

        saveFile(FILE_PATH, expected);
        String actual = readFile(FILE_PATH);

        assertThat(actual, is(expected));

        Files.deleteIfExists(Paths.get(FILE_PATH));
    }

    @Test
    public void shouldDeleteDirectoriesAndFiles() throws IOException {

        final String ROOT_DIR = "./root";

        String[] directories = { ROOT_DIR, ROOT_DIR + "/dir" };
        String[] files = { ROOT_DIR + "/file.txt", ROOT_DIR + "/dir/file1.txt",
                ROOT_DIR + "/dir/file2.txt" };

        for (String directory : directories) {
            Files.createDirectories(Paths.get(directory));
            assertThat(Files.exists(Paths.get(directory)), is(true));
        }

        for (String file : files) {
            Files.createFile(Paths.get(file));
            assertThat(Files.exists(Paths.get(file)), is(true));
        }

        deleteRecursively(ROOT_DIR);

        assertThat(Files.exists(Paths.get(ROOT_DIR)), is(false));
    }
}
