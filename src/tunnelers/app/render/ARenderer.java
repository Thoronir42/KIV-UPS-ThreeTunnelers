package tunnelers.app.render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Stepan
 */
abstract class ARenderer {

	protected final GraphicsContext g;
	protected final Dimension2D blockSize;

	public ARenderer(GraphicsContext g, Dimension2D blockSize) {
		this.g = g;
		this.blockSize = blockSize;

	}
}
