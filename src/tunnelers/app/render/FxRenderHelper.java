package tunnelers.app.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.engine.Engine;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class FxRenderHelper {

	private final Engine engine;
	private final AColorScheme colorScheme;

	private Dimension2D blockSize;
	private GraphicsContext gc;

	protected MapRenderer mapRenderer;
	protected AssetsRenderer assetsRenderer;

	public FxRenderHelper(Engine engine, AColorScheme colorScheme, MapRenderer map, AssetsRenderer assets) {
		this.colorScheme = colorScheme;
		this.engine = engine;
		this.mapRenderer = map;
		this.assetsRenderer = assets;
	}

	public void setBlockSize(Dimension2D blockSize) {
		this.blockSize = blockSize;
		this.mapRenderer.setBlockSize(blockSize);
		this.assetsRenderer.setBlockSize(blockSize);
	}

	public void setGraphicsContext(GraphicsContext context) {
		this.gc = context;
		this.mapRenderer.setGraphicsContext(context);
		this.assetsRenderer.setGraphicsContext(context);
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
		return this.engine.getContainer().getPlayers();
	}

	public Collection<Projectile> getProjectiles() {
		return this.engine.getWarzone().getProjectiles();
	}

	/**
	 * FIXME: add own references directly to the tanks
	 *
	 * @return
	 */
	public Collection<Tank> getTanks() {
		Collection<APlayer> players = this.getPlayers();
		Collection<Tank> tanks = new ArrayList<>(players.size());
		players.forEach((p) -> {
			tanks.add(p.getTank());
		});

		return tanks;
	}

	public void offsetBlocks(double x, double y) {
		gc.translate(
				x * this.blockSize.getWidth(),
				y * this.blockSize.getHeight()
		);
	}

	public void offsetBlocks(Point2D point) {
		this.offsetBlocks(point.getX(), point.getY());
	}
}
