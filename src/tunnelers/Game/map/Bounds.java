package tunnelers.Game.map;

/**
 *
 * @author Stepan
 */
public class Bounds {

	public final int xMin, xMax, yMin, yMax;

	public Bounds(int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	public Bounds intersection(Bounds other) {
		return new Bounds(
				Math.max(xMin, other.xMin),
				Math.min(xMax, other.xMax),
				Math.max(yMin, other.yMin),
				Math.min(yMax, other.yMax)
		);
	}

}
