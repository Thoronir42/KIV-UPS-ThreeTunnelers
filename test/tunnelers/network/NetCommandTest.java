package tunnelers.network;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stepan
 */
public class NetCommandTest {
	
	NetCommand instance;
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of parse method, of class NetCommand.
	 */
	@Test
	public void testParse() throws Exception {
		String msg = "0A2F00650000";
		System.out.println("parse msg="+msg);
		
		NetCommand expResult = null;
		NetCommand result = NetCommand.parse(msg);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCommandCode method, of class NetCommand.
	 */
	@Test
	public void testGetCommandCode() {
		System.out.print("getCommandCode: ");
		instance = new LeadCommand.Approve((byte)12, 2);
		String expResult = "";
		String result = instance.getCommandCode();
		System.out.println(result);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isLeadCommand method, of class NetCommand.
	 */
	@Test
	public void testIsLeadCommand() {
		System.out.println("isLeadCommand");
		NetCommand instance = null;
		boolean expResult = false;
		boolean result = instance.isLeadCommand();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class NetCommandImpl extends NetCommand {

		public NetCommandImpl() {
			super((short)0);
		}
	}
	
}
