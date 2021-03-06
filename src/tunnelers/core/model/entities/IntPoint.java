package tunnelers.core.model.entities;

import javafx.geometry.Point2D;

public class IntPoint {

	int x, y;

	public IntPoint() {
		this(0, 0);
	}

	public IntPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public IntPoint add(int x, int y) {
		this.x += x;
		this.y += y;

		return this;
	}

	public IntPoint add(IntPoint other) {
		return this.add(other.x, other.y);
	}

	public IntPoint multiply(int n) {
		this.x *= n;
		this.y *= n;

		return this;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public IntPoint copy() {
		return new IntPoint(x, y);
	}

	/**
	 * TODO: is this shortcut really neccesary??
	 *
	 * @return
	 */
	public Point2D fx() {
		return new Point2D(this.x, this.y);
	}

	@Override
	public String toString() {
		return "IntPoint [" + x + ", " + y + ']';
	}

}
