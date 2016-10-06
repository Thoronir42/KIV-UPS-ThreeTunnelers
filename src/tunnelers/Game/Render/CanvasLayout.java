package tunnelers.Game.Render;

import tunnelers.app.render.ZoneRenderer;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.AssetsRenderer;

/**
 *
 * @author Stepan
 */
public abstract class CanvasLayout {

	public static CanvasLayout choseIdeal(int playerCount, Dimension2D d) {
		try {
			CanvasLayout layout = RectangularCanLayout.getLayoutFor(playerCount, d);
			return layout;
		} catch (CanvasLayoutException e) {
			throw new IllegalArgumentException(String.format("Could not find layout suitable for %d players.", playerCount));
		}
	}
	
	protected ZoneRenderer mapRenderer;
	protected AssetsRenderer assetsRenderer;
	
	public void setZoneRenderer(ZoneRenderer r) {
		this.mapRenderer = r;
	}
	public void setAssetsRenderer(AssetsRenderer r){
		this.assetsRenderer = r;
	}

	public abstract Dimension2D getBlockSize();

	public abstract void drawLayout(GraphicsContext g);
}
