package tunnelers.app.render;

import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
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
		g.strokeRect(render.getMinX(), render.getMinY(), render.getWidth(), render.getHeight());

		for (int col = 0; col * blockSize.getWidth() < render.getWidth(); col++) {
			for (int row = 0; row * blockSize.getHeight() < render.getHeight(); row++) {
				g.setFill(colorScheme.getRandStatic(col, row, opacity));
				g.fillRect(render.getMinX() + col * blockSize.getWidth(),
						render.getMinY() + row * blockSize.getHeight(),
						blockSize.getWidth(), blockSize.getHeight());
			}
		}
	}

	public float calculateStatic(int value, int maxValue) {
		return 1 - ( 1.0f * value / maxValue);
	}
}
