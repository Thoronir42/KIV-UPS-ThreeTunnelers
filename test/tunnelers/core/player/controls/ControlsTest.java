package tunnelers.core.player.controls;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Skoro
 */
public class ControlsTest {

	/**
	 * Test of setControlState method, of class Controls.
	 */
	@Test
	public void testGetSet() {
		Controls instance = new Controls(0);
		for (InputAction ia : InputAction.values()) {
			assertFalse(instance.get(ia));
		}

		for (InputAction ia : InputAction.values()) {
			assertTrue(instance.set(ia, true));
			assertTrue(instance.get(ia));

			assertEquals("Mask of " + ia, (int)Math.pow(2, ia.intVal()), instance.getState());

			assertTrue(instance.set(ia, false));
			assertFalse(instance.get(ia));
		}
	}
	
	@Test
	public void testSetState(){
		Controls instance = new Controls(0);
		
		instance.setState(20);
		assertFalse(instance.get(InputAction.movUp));
		assertFalse(instance.get(InputAction.movDown));
		assertTrue(instance.get(InputAction.movLeft));
		assertFalse(instance.get(InputAction.movRight));
		assertTrue(instance.get(InputAction.actShoot));
	}

}
