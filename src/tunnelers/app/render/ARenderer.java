package tunnelers.app.render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;

abstract class ARenderer {

	protected GraphicsContext g;
	protected Dimension2D blockSize;
	protected AColorScheme colorScheme;

	ARenderer(AColorScheme colorScheme) {
		this.setColorScheme(colorScheme);
	}

	void setGraphicsContext(GraphicsContext g) {
		this.g = g;
	}

	void setBlockSize(Dimension2D blockSize) {
		this.blockSize = blockSize;
	}

	void setColorScheme(AColorScheme colorScheme) {
		this.colorScheme = colorScheme;
	}
}
