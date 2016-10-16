package tunnelers.app.render.colors;

/**
 *
 * @author Stepan
 */
public interface ILocationRandomizer {
	/**
	 * A deterministic randomizer function interface to calculate a color 
	 * variant identifier
	 * @param x the X location of a tile
	 * @param y the Y location of a tile
	 * @return a positive integer
	 */
	public int calc(int x, int y);
}
