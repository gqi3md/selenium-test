package app.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {
	public final String BaseURL;
	public final String correctUsername;
	public final String correctPassword;
	
	private Config() {
		List<String> lines = new ArrayList<String>(3);
		try {
			lines = Files.lines(Path.of("src/main/resources/config.txt")).toList();
		} catch (IOException e) {
		}
		BaseURL = lines.get(0);
		correctUsername = lines.get(1);
		correctPassword = lines.get(2);
	}
	
	private static Config _instance = null;
	
	public static Config instance() {
		if(_instance == null)
			_instance = new Config();
		return _instance;
	}
}
