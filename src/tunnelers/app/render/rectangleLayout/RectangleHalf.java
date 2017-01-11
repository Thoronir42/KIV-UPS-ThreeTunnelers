package tunnelers.app.render.rectangleLayout;

import javafx.geometry.Rectangle2D;

/**
 *
 * @author Stepan
 */
public class RectangleHalf extends Rectangle2D {

	protected double halfWidth;
	protected double halfHeight;

	public RectangleHalf(double width, double height) {
		super(0, 0, width, height);
		this.halfHeight = height / 2;
		this.halfWidth = width / 2;
	}

	public double getHalfWidth() {
		return halfWidth;
	}

	public double getHalfHeight() {
		return halfHeight;
	}

}
