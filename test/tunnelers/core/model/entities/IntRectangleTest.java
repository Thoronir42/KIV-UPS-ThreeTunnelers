package tunnelers.core.model.entities;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Skoro
 */
public class IntRectangleTest {
	
	@Test
	public void testIntersection() {
		IntRectangle other = new IntRectangle(0, 0, 4, 4);
		IntRectangle instance = new IntRectangle(1, 1, 4, 4);
		
		IntRectangle expectedResult = new IntRectangle(1, 1, 3, 3);
		
		assertEquals(instance.intersection(other), other.intersection(instance));
		assertEquals(expectedResult, instance.intersection(other));
	}
	
	@Test
	public void testIntersectionEmpty(){
		IntRectangle other = new IntRectangle(0, 0, 4, 4);
		IntRectangle instance = new IntRectangle(5, 0, 4, 4);
		
		IntRectangle expectedResult = new IntRectangle(0, 0, 0, 0);
		
		assertEquals(expectedResult, instance.intersection(other));
	}
	
	/**
	 * Test of getMinX method, of class IntRectangle.
	 */
	@Test
	public void testGetters() {
		IntRectangle instance = new IntRectangle(2, 4, 3, 2);
		
		assertEquals(2, instance.getMinX());
		assertEquals(4, instance.getMinY());
		assertEquals(5, instance.getMaxX());
		assertEquals(6, instance.getMaxY());
		assertEquals(3, instance.getWidth());
		assertEquals(2, instance.getHeight());
	}

	/**
	 * Test of contains method, of class IntRectangle.
	 */
	@Test
	public void testContains() {
		
		IntRectangle instance = new IntRectangle(1, 0, 2, 4);
		IntPoint locationInside = new IntPoint(2, 3);
		IntPoint locationOutside = new IntPoint(-4, 5);
		
		assertTrue(instance.contains(locationInside));
		assertFalse(instance.contains(locationOutside));
	}
	
}
