package generic;

import java.io.File;
import java.io.IOException;

public class FileStorage {

	protected final String storageRoot;

	private final static String illegalCharacters = "#%&*{}<>?|'\\";

	public FileStorage(String root) {
		if (root.charAt(root.length() - 1) != File.separatorChar) {
			root += File.separatorChar;
		}

		this.storageRoot = root;
	}

	protected File createFile(String filename) throws IOException {
		for (char a : illegalCharacters.toCharArray()) {
			if (filename.indexOf(a) != -1) {
				throw new IOException("File name must not contain illegal characters ( " + illegalCharacters + " )");
			}
		}

		File f = (new File(this.storageRoot + filename)).getAbsoluteFile();
		File parent = f.getParentFile();

		System.out.println(f.getAbsoluteFile().toString());

		if (parent != null && !parent.exists() && !parent.mkdirs()) {
			throw new IOException("Failed creating directories for " + f.getAbsolutePath());
		}

		if (!f.createNewFile()) {
			throw new IOException("Failed creating file " + f.getParent());
		}


		return f;
	}
}
