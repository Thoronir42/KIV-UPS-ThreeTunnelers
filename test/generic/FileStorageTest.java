package generic;

import java.io.File;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileStorageTest {

	private static final String TEST_FILE = "files/test.txt";

	FileStorage instance;

	@Before
	public void setUp() {
		instance = new FileStorage(Paths.get("").toAbsolutePath().toString());

		File f = new File(instance.storageRoot + TEST_FILE);
		if (f.exists()) {
			f.delete();
		}
	}

	@Test
	public void testCreateFile() throws Exception {
		File f = instance.createFile(TEST_FILE);

		assertNotNull(f);
	}

}
