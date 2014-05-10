package org.asuki.common.javase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NioDemo {

	public static void stringToFile(String filePath, String content)
			throws IOException {
		Files.write(Paths.get(filePath), content.getBytes());
	}

	public static String fileToString(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}

}
