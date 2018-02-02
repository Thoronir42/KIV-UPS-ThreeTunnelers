package tunnelers.app.render.colors;

public interface ILocationRandomizer {
	/**
	 * A deterministic randomizer function interface to compute
	 * a color variant identifier.
	 *
	 * @param x the X location of a tile
	 * @param y the Y location of a tile
	 * @return a positive integer
	 */
	int calc(int x, int y);
}
