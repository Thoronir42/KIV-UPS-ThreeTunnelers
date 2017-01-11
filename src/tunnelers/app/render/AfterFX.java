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
	
	public AfterFX(FxDefaultColorScheme colorScheme){
		this.colorScheme = colorScheme;
	}
	
	
	public void renderStaticNoise(GraphicsContext g, Rectangle2D render, double opacity, Dimension2D blockSize) {
		for (int col = (int) render.getMinX(); col * blockSize.getWidth() < render.getWidth(); col++) {
			for (int row = (int) render.getMinY(); row * blockSize.getHeight() < render.getHeight(); row++) {
				g.setFill(colorScheme.getRandStatic(col, row, opacity));
				g.fillRect(col * blockSize.getWidth(), row * blockSize.getHeight(), blockSize.getWidth(), blockSize.getHeight());
			}
		}
	}
}
