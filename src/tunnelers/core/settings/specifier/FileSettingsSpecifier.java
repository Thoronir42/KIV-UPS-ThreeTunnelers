package tunnelers.core.settings.specifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import tunnelers.core.settings.Settings;

/**
 *
 * @author Stepan
 */
public class FileSettingsSpecifier implements ISettingsSpecifier{
	
	private File file;
	
	public FileSettingsSpecifier(String path) throws FileNotFoundException{
		file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("Configuration file " + path + " was not found.");
		}
		System.out.println("Config file found but not used.");
	}

	@Override
	public void set(Settings settings) {
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			System.out.println("Loading config " + file.getAbsolutePath());
			String row;
			while((row = br.readLine()) != null){
				System.out.println(row);
			}
			System.out.println("Config loaded");
		} catch (IOException e){
			System.err.println("Failed to load config file");
		}
	}
	
}
