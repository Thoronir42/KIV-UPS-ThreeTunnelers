package tunnelers.core.model.entities;

/**
 *
 * @author Skoro
 */
public class Shape {

	private final boolean[] pixels;

	private final int width;
	private final int height;

	private final IntPoint min;
	private final IntPoint max;

	protected Shape(int width, int height, boolean[] pixels) {
		if (width * height != pixels.length) {
			throw new IllegalArgumentException("Shape dimensions must match pixel count");
		}
		this.width = width;
		this.height = height;
		this.pixels = pixels;

		this.min = new IntPoint(-width / 2, -height / 2);
		this.max = new IntPoint(width / 2, height / 2);
	}

	public boolean isPixelSolid(int x, int y) {
		int offset = y * this.width + x;
		if(offset < 0 || offset >= this.pixels.length){
			return false;
		}
		return this.pixels[offset];
	}

	public boolean isPixelSolidRelative(int sx, int sy) {
		return this.isPixelSolid(sx - this.min.getX(), sy - this.min.getY());
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public IntPoint getMin() {
		return min.copy();
	}

	public IntPoint getMax() {
		return max.copy();
	}

	public IntDimension getSize() {
		return new IntDimension(width, height);
	}

}
