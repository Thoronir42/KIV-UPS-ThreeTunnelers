package generic;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author Stepan
 */
public class RectangleHalf extends Rectangle{

	protected double halfWidth;
	protected double halfHeight;
	
	public RectangleHalf(double width, double height) {
		super(width, height);
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
