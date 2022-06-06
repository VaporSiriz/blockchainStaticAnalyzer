package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Scanner {
	String json;

	public String createJson(String inputFile) {
		String astFile = inputFile + ".ast";
		
		byte[] byteData;
		try {
			Path path = Paths.get(astFile);
			System.out.println("path : " + path);
			byteData = Files.readAllBytes(path);
			json = new String(byteData);
			json = json.substring(json.indexOf("{"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return json;
	}
}

