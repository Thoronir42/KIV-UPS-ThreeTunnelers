package tunnelers.app.render;

import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import tunnelers.app.render.colors.FxDefaultColorScheme;

/**
 *
 * @author Skoro
 */
public class AfterFX {

	private final FxDefaultColorScheme colorScheme;

	public AfterFX(FxDefaultColorScheme colorScheme) {
		this.colorScheme = colorScheme;
	}

	public void renderStaticNoise(GraphicsContext g, Dimension2D blockSize, double opacity, Rectangle2D render) {
		Affine defaultTransform = g.getTransform();
		g.translate(render.getMinX(), render.getMinY());
		renderStaticNoise(g, blockSize, opacity, render.getWidth(), render.getHeight());
		g.setTransform(defaultTransform);
	}
	
	public void renderStaticNoise(GraphicsContext g, Dimension2D blockSize, double opacity, double width, double height) {
		for (int col = 0; col * blockSize.getWidth() < width; col++) {
			for (int row = 0; row * blockSize.getHeight() < width; row++) {
				g.setFill(colorScheme.getRandStatic(col, row, opacity));
				g.fillRect(col * blockSize.getWidth(), row * blockSize.getHeight(),
						blockSize.getWidth(), blockSize.getHeight());
			}
		}
	}

	public float calculateStatic(int value, int maxValue) {
		return 1 - ( 1.0f * value / maxValue);
	}
}
