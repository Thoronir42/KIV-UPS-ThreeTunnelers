package generic;

import java.util.Random;

/**
 *
 * @author Stepan
 */
public class RNG {

	private static final RNG INSTANCE = new RNG(420);

	public static int getRandInt(int i) {
		return INSTANCE.getInt(i);
	}

	private final Random rand;

	public RNG(long seed) {
		rand = new Random(seed);
	}

	public int getInt(int i) {
		if (i < 1) {
			return 0;
		}
		return rand.nextInt(i);
	}

	public void setSeed(long seed) {
		this.rand.setSeed(seed);
	}

	public boolean nextBoolean() {
		return this.rand.nextBoolean();
	}
}
