package tunnelers.app.views.components.flash;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stepan
 */
public class LinearEaseFunctionTest {

	private static final float TEST_SPEED = 0.1f;
	private static final float TEST_DELTA = 0.01f;

	@Test
	public void testGo01() {
		int start = 0;
		int end = 1;
		LinearEaseFunction ease = new LinearEaseFunction(start, end, TEST_SPEED);

		float expectedValue = start;
		float actualValue;

		for (int i = start; i * TEST_SPEED < end; i++) {
			expectedValue += TEST_SPEED;
			actualValue = ease.next();
			compare(i + 1, expectedValue, actualValue);
		}
		compare(-1, end, ease.next());
	}

	@Test
	public void testGo10() {
		int start = 1;
		int end = 0;
		LinearEaseFunction ease = new LinearEaseFunction(start, end, TEST_SPEED);

		float expectedValue = start;

		for (int i = 0; start - i * TEST_SPEED > end; i++) {
			expectedValue -= TEST_SPEED;
			compare(i + 1, expectedValue, ease.next());
		}
		
		compare(-1, end, ease.next());
	}

	private void compare(int step, float expected, float actual) {
		//System.out.format("Step %d: expected: %.4f, got: %.4f\n", step, expected, actual);
		assertEquals(expected, actual, TEST_DELTA);
	}

}
