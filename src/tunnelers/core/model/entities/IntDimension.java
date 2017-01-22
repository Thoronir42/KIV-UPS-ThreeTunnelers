package tunnelers.core.model.entities;

import javafx.geometry.Dimension2D;

/**
 *
 * @author Skoro
 */
public class IntDimension {

	private final int width;
	private final int height;

	public IntDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Dimension2D fx() {
		return new Dimension2D(width, height);
	}

	public boolean contains(int x, int y) {
		return !(x < 0 || x >= width || y < 0 || y >= height);
	}

	public boolean contains(IntPoint baseChunk) {
		return this.contains(baseChunk.getX(), baseChunk.getY());
	}

}
