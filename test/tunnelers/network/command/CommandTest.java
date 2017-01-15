package tunnelers.network.command;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assume;

/**
 *
 * @author Skoro
 */
public class CommandTest {

	@Test
	public void testGetType() {
		Command instance = new Command(CommandType.LeadIntroduce);
		assertEquals(CommandType.LeadIntroduce, instance.getType());
	}

	@Test
	public void testGetLength() {
		Command instance = new Command(CommandType.LeadIntroduce);

		assertEquals(0, instance.getLength());

		instance.append("12345678");
		assertEquals(8, instance.getLength());
	}

	/**
	 * Test of append method, of class Command.
	 */
	@Test
	public void testAppend_byte() {
		byte n = 23;
		Command instance = new Command(CommandType.LeadIntroduce);

		instance.append(n);
		assertEquals(2, instance.getLength());
		assertEquals("17", instance.getData());
	}

	/**
	 * Test of append method, of class Command.
	 */
	@Test
	public void testAppend_short() {
		short n = 163;
		Command instance = new Command(CommandType.LeadIntroduce);

		instance.append(n);
		assertEquals(4, instance.getLength());
		assertEquals("00A3", instance.getData());
	}

	/**
	 * Test of append method, of class Command.
	 */
	@Test
	public void testAppend_int() {
		int n = 2017;
		Command instance = new Command(CommandType.LeadIntroduce);

		instance.append(n);
		assertEquals(8, instance.getLength());
		assertEquals("000007E1", instance.getData());
	}

	/**
	 * Test of append method, of class Command.
	 */
	@Test
	public void testAppend_long() {
		long n = 47852135L;
		Command instance = new Command(CommandType.LeadIntroduce);

		instance.append(n);
		assertEquals(16, instance.getLength());
		assertEquals("0000000002DA2A67", instance.getData());
	}

	/**
	 * Test of append method, of class Command.
	 */
	@Test
	public void testAppend_String() {
		String str = "KAREL";
		Command instance = new Command(CommandType.LeadIntroduce);

		instance.append(str);
		assertEquals(5, instance.getLength());
		assertEquals(str, instance.getData());
	}

	/**
	 * Test of getData method, of class Command.
	 */
	@Test
	public void testGetSetData() {
		Command instance = new Command(CommandType.LeadIntroduce);
		instance.append(413);
		
		Assume.assumeTrue(instance.getLength() == 8);
		String testy_test = "TESTY_TEST";
		
		instance.setData(testy_test);
		
		assertEquals(10, instance.getLength());
		assertEquals(testy_test, instance.getData());
	}
}
