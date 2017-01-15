package tunnelers.core.model.entities;

/**
 *
 * @author Stepan
 */
public class IntRectangle {

	public final int xMin, xMax, yMin, yMax;

	public IntRectangle(int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}

	public IntRectangle intersection(IntRectangle other) {
		return new IntRectangle(
				Math.max(xMin, other.xMin),
				Math.min(xMax, other.xMax),
				Math.max(yMin, other.yMin),
				Math.min(yMax, other.yMax)
		);
	}

	public int getMinX() {
		return xMin;
	}

	public int getMaxX() {
		return xMax;
	}

	public int getMinY() {
		return yMin;
	}

	public int getMaxY() {
		return yMax;
	}
	
	
	
	public int getWidth(){
		return xMax - xMin;
	}
	
	public int getHeight(){
		return yMax - yMin;
	}

	public boolean contains(IntPoint location) {
		return (location.getX() >= xMin &&  location.getX() <= xMax
				&& location.getY() >= yMin && location.getY() <= yMax);
	}

}
