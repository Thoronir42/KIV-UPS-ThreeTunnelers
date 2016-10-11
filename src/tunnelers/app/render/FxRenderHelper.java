package tunnelers.app.render;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.engine.Engine;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class FxRenderHelper {

	private final Engine engine;
	private final AColorScheme colorScheme;

	protected MapRenderer mapRenderer;
	protected AssetsRenderer assetsRenderer;

	public FxRenderHelper(Engine engine, AColorScheme colorScheme, MapRenderer map, AssetsRenderer assets) {
		this.colorScheme = colorScheme;
		this.engine = engine;
		this.mapRenderer = map;
		this.assetsRenderer = assets;
	}

	public void setGraphicsContext(GraphicsContext context) {
		mapRenderer.setGraphicsContext(context);
		assetsRenderer.setGraphicsContext(context);
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

	public List<APlayer> getPlayers() {
		return this.engine.getPlayers();
	}
}
