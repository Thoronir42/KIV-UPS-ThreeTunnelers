package generic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stepan
 */
public class SimpleScannerTestEdge {

	private static final int TESTED_RADIX = 16;

	
	@Test
	public void testAhojMessage() {
		String ahoj = "0028Ahoj";
		SimpleScanner sc = new SimpleScanner(ahoj, TESTED_RADIX);
		
		assertEquals(8, sc.remainingLength());
		assertEquals(40, sc.nextShort());
		assertEquals("Ahoj", sc.readToEnd());
	}

}
