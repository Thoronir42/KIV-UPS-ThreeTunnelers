package tunnelers.core.engine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stepan
 */
public class PersistentStringTest {

	private static final String TEST_TEXT = "Ahoj";
	private static final String TEST_FILE = "test/test.txt";
	
	@Test
	public void testSetGet() {
		PersistentString storage = new PersistentString(TEST_FILE);
		
		assertTrue(storage.set(TEST_TEXT));
		
		assertEquals(TEST_TEXT, storage.get());
	}
}
