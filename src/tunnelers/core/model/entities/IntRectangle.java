package tunnelers.core.model.entities;

/**
 *
 * @author Stepan
 */
public class IntRectangle {

	protected final int x, y;
	protected final int width, height;

	public IntRectangle(IntPoint location, IntDimension size) {
		this(location.getX(), location.getY(), size.getWidth(), size.getHeight());
	}

	public IntRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public IntRectangle intersection(IntRectangle other) {
		int xMin = Math.max(this.getMinX(), other.getMinX());
		int xMax = Math.min(this.getMaxX(), other.getMaxX());
		int yMin = Math.max(this.getMinY(), other.getMinY());
		int yMax = Math.min(this.getMaxY(), other.getMaxY());

//		System.out.format("min: [%d, %d], max: [%d, %d]\n", xMin, yMin, xMax, yMax);
		if(xMax - xMin < 0 || yMax - yMin < 0){
			return new IntRectangle(0, 0, 0, 0);
		}
		
		return new IntRectangle(xMin, yMin, xMax - xMin, yMax - yMin);

	}

	public int getMinX() {
		return x;
	}

	public int getMaxX() {
		return x + width;
	}

	public int getMinY() {
		return y;
	}

	public int getMaxY() {
		return y + height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean contains(IntPoint location) {
		return (location.getX() >= x && location.getX() <= x + width
				&& location.getY() >= y && location.getY() <= y + height);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + this.x;
		hash = 89 * hash + this.y;
		hash = 89 * hash + this.width;
		hash = 89 * hash + this.height;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
		final IntRectangle other = (IntRectangle) obj;
		return (this.x == other.x && this.y == other.y &&
				this.width == other.width && this.height == other.height);
	}

	@Override
	public String toString() {
		return "IntRectangle{" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + '}';
	}
	
}
