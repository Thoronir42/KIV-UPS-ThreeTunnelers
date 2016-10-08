package generic;

import java.util.Random;

/**
 *
 * @author Stepan
 */
public class RNG {

	private static final Random RNG = new Random(420);
	
	public static void setSeed(long seed){
		RNG.setSeed(seed);
	}
	
	public static int getRandInt(int i) {
		return RNG.nextInt(i);
	}
}
