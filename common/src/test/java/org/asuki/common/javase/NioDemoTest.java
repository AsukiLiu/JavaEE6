package org.asuki.common.javase;

import static org.asuki.common.javase.NioDemo.fileToString;
import static org.asuki.common.javase.NioDemo.stringToFile;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

public class NioDemoTest {

	private final static String FILE_PATH = "./test.txt";

	@Test
	public void shouldTransferStringAndFile() throws IOException {

		final String expected = "This is test";

		stringToFile(FILE_PATH, expected);
		String actual = fileToString(FILE_PATH);

		assertThat(actual, is(expected));

		Files.deleteIfExists(Paths.get(FILE_PATH));
	}
}
