package tunnelers.core.engine;

import generic.FileStorage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author Stepan
 */
public class PersistentString extends FileStorage{

	private static final int MAX_LENGTH = 6;

	private final String pathToFile;

	public PersistentString(String relativePath) {
		super(Paths.get("").toAbsolutePath().toString());
		
		this.pathToFile = relativePath;
	}

	public String get() {
		try (BufferedReader reader = new BufferedReader(new FileReader(getFile()))) {
			String secret = reader.readLine();
			if(secret == null){
				return "";
			}
			if (secret.length() > MAX_LENGTH) {
				return secret.substring(0, MAX_LENGTH);
			}
			return secret;
		} catch (IOException ex) {
			System.err.println("Could not read connection secret : " + ex.toString());
			return "";
		}
	}

	public boolean set(String value) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(true)))) {
			writer.write(value);
			return true;
		} catch (IOException ex) {
			System.err.println("Could not write connection secret : " + ex.toString());
			return false;
		}
	}

	private File getFile() throws IOException {
		return this.getFile(false);
	}

	private File getFile(boolean clean) throws IOException {
		File f = new File(pathToFile);
		if (clean && f.exists()) {
			f.delete();
		}

		if (!f.exists()) {
			return this.createFile(pathToFile);
		}
		
		return f;
	}
}
