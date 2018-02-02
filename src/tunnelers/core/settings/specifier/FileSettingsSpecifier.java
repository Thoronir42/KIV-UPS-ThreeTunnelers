package tunnelers.core.settings.specifier;

import tunnelers.core.settings.Settings;

import java.io.*;

public class FileSettingsSpecifier implements ISettingsSpecifier {

	private final File file;

	public FileSettingsSpecifier(String path) throws FileNotFoundException {
		file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("Configuration file " + path + " was not found.");
		}
		System.out.println("Config file found but not used.");
	}

	@Override
	public void initialize(Settings settings) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			System.out.println("Loading config " + file.getAbsolutePath());
			String row;
			while ((row = br.readLine()) != null) {
				System.out.println(row);
			}
			System.out.println("Config loaded");
		} catch (IOException e) {
			System.err.println("Failed to load config file");
		}
	}

}
