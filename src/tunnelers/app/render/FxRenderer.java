package tunnelers.app.render;

import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.engine.Engine;

/**
 *
 * @author Stepan
 */
public class FxRenderer {

	private final Engine engine;
	private final AColorScheme colorScheme;

	private GraphicsContext gc;

	protected MapRenderer mapRenderer;
	protected AssetsRenderer assetsRenderer;

	public FxRenderer(Engine engine, AColorScheme colorScheme, MapRenderer map, AssetsRenderer assets) {
		this.colorScheme = colorScheme;
		this.engine = engine;
		this.mapRenderer = map;
		this.assetsRenderer = assets;
	}

	public void setGraphicsContext(GraphicsContext context) {
		this.gc = context;
		
		mapRenderer.setGraphicsContext(context);
		assetsRenderer.setGraphicsContext(context);
	}

	public void render(GraphicsContext gc) {
		
	}

	public AColorScheme getColorScheme() {
		return this.colorScheme;
	}

	public MapRenderer getMapRenderer() {
		return this.mapRenderer;
	}

	public AssetsRenderer getAssetsRenderer() {
		return this.assetsRenderer;
	}
}
