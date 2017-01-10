package tunnelers.app.render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.render.rectangleLayout.RectangularLayout;

/**
 *
 * @author Stepan
 */
public abstract class RenderLayout {

	public static RenderLayout choseIdeal(int playerCount, Dimension2D d) {
		try {
			RenderLayout layout = RectangularLayout.getLayoutFor(playerCount, d);
			return layout;
		} catch (RenderLayoutException e) {
			throw new IllegalArgumentException(String.format("Could not find layout suitable for %d players.", playerCount));
		}
	}
	
	protected FxRenderContainer renderer;
	
	public void setRenderer(FxRenderContainer r) {
		this.renderer = r;
	}
	
	protected MapRenderer getMapRenderer(){
		return this.renderer.mapRenderer;
	}
	
	protected AssetsRenderer getAssetsRenderer(){
		return this.renderer.assetsRenderer;
	}
	
	protected AColorScheme getColorScheme(){
		return this.renderer.getColorScheme();
	}

	public abstract void draw(GraphicsContext g);
}
