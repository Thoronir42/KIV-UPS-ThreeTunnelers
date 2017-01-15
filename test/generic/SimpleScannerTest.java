package generic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stepan
 */
public class SimpleScannerTest {

	private static final int TESTED_RADIX = SimpleScanner.RADIX_HEXADECIMAL;
	private static final String TESTED_STRING = "123456789ABCDEF0";

	private SimpleScanner sc;

	public SimpleScannerTest() {
	}

	@Before
	public void setUp() {
		sc = new SimpleScanner(TESTED_STRING, TESTED_RADIX);
	}

	@Test
	public void testSetSourceString() {
		sc.nextShort();
		assertEquals("Scanner is in unspecified state", 12, sc.remainingLength());
		sc.setSourceString("10");
		assertEquals("New string is 2 characters long", 2, sc.remainingLength());
		assertEquals("New string should yield 16", 16, sc.nextByte());
	}

	@Test
	public void testRemainingLength() {
		assertEquals("Initial scanned string is 16 chars long", 16, sc.remainingLength());
	}

	@Test
	public void testNextByte() {
		short[] tests = {0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC, 0xDE, 0xF0};

		for (short expected : tests) {
			assertEquals(expected, sc.nextByte());
		}
	}

	@Test
	public void testNextShort() {
		// , 0x9ABC, 0xDEF0 is out of range
		int[] tests = {0x1234, 0x5678};
		int n = 0;
		for (int expected : tests) {
			assertEquals(++n + "", (short)expected, sc.nextShort());
		}
	}

	@Test(expected = NumberFormatException.class)
	public void testNextShortRangeException() {
		sc.setSourceString("8000");
		assertEquals(0, sc.nextShort());
	}
	

	@Test
	public void testNextInt() {
		//  0x9ABCDEF0 is out of range
		int[] tests = {0x12345678};

		for (int expected : tests) {
			assertEquals(expected, sc.nextInt());
		}
	}
	
	@Test(expected = NumberFormatException.class)
	public void testNextIntRangeException(){
		sc.setSourceString("80000000");
		sc.nextInt();
	}

	@Test
	public void testRead() {
		assertEquals("12", sc.read(2));
		assertEquals("345", sc.read(3));
		assertEquals("6", sc.read(1));
		assertEquals("789A", sc.read(4));
		assertEquals("BCDEF", sc.read(5));
		assertEquals("0", sc.read(1));
	}

	@Test
	public void testReadToEnd() {
		assertEquals(TESTED_STRING, sc.readToEnd());
	}

}
