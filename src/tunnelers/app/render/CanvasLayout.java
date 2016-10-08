package tunnelers.app.render;

import java.util.List;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.render.rectangleLayout.RectangularLayout;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public abstract class CanvasLayout {

	public static CanvasLayout choseIdeal(int playerCount, Dimension2D d) {
		try {
			CanvasLayout layout = RectangularLayout.getLayoutFor(playerCount, d);
			return layout;
		} catch (CanvasLayoutException e) {
			throw new IllegalArgumentException(String.format("Could not find layout suitable for %d players.", playerCount));
		}
	}
	
	protected FxRenderer renderer;
	
	public void setRenderer(FxRenderer r) {
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

	public abstract void draw(GraphicsContext g, List<APlayer> players);
}
