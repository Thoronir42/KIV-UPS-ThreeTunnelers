package tunnelers.app.render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.render.rectangleLayout.RectangularLayout;

/**
 *
 * @author Stepan
 */
public abstract class ARenderLayout {

	public static ARenderLayout choseIdeal(FxRenderContainer renderer, int playerCount, Dimension2D d) {
		try {
			ARenderLayout layout = RectangularLayout.getLayoutFor(renderer, playerCount, d);
			return layout;
		} catch (RenderLayoutException e) {
			throw new IllegalArgumentException(String.format("Could not find layout suitable for %d players.", playerCount));
		}
	}
	
	protected final FxRenderContainer renderer;
	
	public ARenderLayout(FxRenderContainer renderer){
		this.renderer = renderer;
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
