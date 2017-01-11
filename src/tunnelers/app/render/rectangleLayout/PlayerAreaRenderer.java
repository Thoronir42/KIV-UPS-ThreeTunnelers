package tunnelers.app.render.rectangleLayout;

import java.util.Collection;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import tunnelers.app.render.FxRenderContainer;
import tunnelers.app.render.MapRenderer;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;

/**
 *
 * @author Stepan
 */
public class PlayerAreaRenderer {

	public static final int MIN_RENDERED_BLOCKS_ON_DIMENSION = 27;

	private final Dimension2D bounds;
	private final Rectangle2D renderWindow;
	private final Dimension2D blockSize;
	private final RectangleHalf sourceWindow;

	private final FxRenderContainer renderer;

	PlayerAreaRenderer(Dimension2D bounds, FxRenderContainer renderer) {
		this.bounds = bounds;
		this.renderWindow = new Rectangle2D(bounds.getWidth() * 0.05, bounds.getHeight() * 0.05, bounds.getWidth() * 0.9, bounds.getHeight() * 0.6);
		this.blockSize = calcBlockSize(this.renderWindow, MIN_RENDERED_BLOCKS_ON_DIMENSION);
		this.sourceWindow = calcSource(this.renderWindow, this.blockSize);
		this.renderer = renderer;
		
		renderer.setBlockSize(blockSize);
	}

	public Dimension2D getBounds() {
		return this.bounds;
	}

	protected void draw(GraphicsContext g, Dimension2D bounds, Tank currentTank) {
		if (this.renderer == null) {
			System.err.println("Renderer was not previously set");
			return;
		}

		Tank[] tanks = renderer.getTanks();
		Collection<Projectile> projectiles = renderer.getProjectiles();

		Affine defTransform = g.getTransform();

		AColorScheme colors = renderer.getColorScheme();

		g.setFill(colors.playerColors().get(currentTank).color());
		g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());

		g.translate(renderWindow.getMinX(), renderWindow.getMinY());
		drawViewWindow(g, currentTank.getLocation(), tanks, projectiles);
		g.setTransform(defTransform);

		Rectangle inBounds = new Rectangle(bounds.getWidth() * 0.8, bounds.getHeight() * 0.1);
		inBounds.setX(bounds.getWidth() * 0.1);
		inBounds.setY(bounds.getHeight() * 0.7);
		fillStatusBar(g, inBounds, colors.getUiHitpoints());

		inBounds.setY(bounds.getHeight() * 0.85);
		fillStatusBar(g, inBounds, colors.getUiEnergy());
	}

	private void fillStatusBar(GraphicsContext g, Rectangle r, Color c) {
		g.setFill(Color.DIMGREY);
		g.setLineWidth(4);
		g.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
		g.setFill(c);
		g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	private void drawViewWindow(GraphicsContext g, IntPoint center, Tank[] tanks, Collection<Projectile> projectiles) {
		Affine defTransform = g.getTransform();
		MapRenderer mr = renderer.getMapRenderer();
		
		g.setFill(Color.BLACK);
		g.fillRect(0 - 2, 0 - 2, renderWindow.getWidth() + 6, renderWindow.getHeight() + 4);
		Rectangle2D source = alignSourceWindow(sourceWindow, center, mr.getMapBounds());
		try {
			this.renderer.offsetBlocks(g, -source.getMinX(), -source.getMinY());
			mr.drawMap(source);
			this.drawProjectiles(g, source, projectiles);
			this.drawTanks(g, source, tanks);

			g.setTransform(defTransform);
		} catch (Exception e) {
			System.err.println("Error with drawViewWindow: " + e.getMessage());
			g.setTransform(defTransform);
			throw e;
		}

		//this.renderStatic(g, render, curPlayer.getTank().getEnergyPct());
		this.renderer.getAfterFX().renderStaticNoise(g, sourceWindow, 1 - 0.95, this.blockSize);

	}

	private int drawProjectiles(GraphicsContext g, Rectangle2D render, Collection<Projectile> projectiles) {
		Affine defTransform = g.getTransform();
		int n = 0;
		for (Projectile projectile : projectiles) {
			if (!render.contains(projectile.getLocation().fx())) {
				continue;
			}
			renderer.offsetBlocks(g, projectile.getLocation());
			renderer.getAssetsRenderer().drawProjectile(projectile);

			g.setTransform(defTransform);
			n++;
		}

		return n;
	}

	private void drawTanks(GraphicsContext g, Rectangle2D render, Tank[] tanks) {
		Affine defTransform = g.getTransform();

		for (Tank tank : tanks) {
			if(tank == null){
				continue;
			}
			if (!render.contains(tank.getLocation().fx())) {
				continue;
			}
			renderer.offsetBlocks(g, tank.getLocation());
			renderer.getAssetsRenderer().drawTank(tank);

			g.setTransform(defTransform);
		}
	}

	private Rectangle2D alignSourceWindow(RectangleHalf source, IntPoint center, Dimension2D mapSize) {
		double halfWidth = source.getHalfWidth(),
				halfHeight = source.getHalfHeight();
		double mapWidth = mapSize.getWidth(),
				mapHeight = mapSize.getHeight();

		double x,y;
		
		if (center.getX() - halfWidth < 0) {
			x = 0;
		} else if (center.getX() + halfWidth > mapWidth) {
			x = mapWidth - source.getWidth();
		} else {
			x = center.getX() - (int) halfWidth;
		}

		if (center.getY() - halfHeight < 0) {
			y = 0;
		} else if (center.getY() + halfHeight > mapHeight) {
			y = mapHeight - source.getHeight();
		} else {
			y = center.getY() - (int) halfHeight;
		}
		
		return new Rectangle2D(x, y, source.getWidth(), source.getHeight());
	}

	private Dimension2D calcBlockSize(Rectangle2D viewWindow, int minimumBlocksOnAxis) {
		double width, height;
		int tmp;
		double bWidth = viewWindow.getWidth(),
				bHeight = viewWindow.getHeight();
		if (bWidth < bHeight) {
			width = bWidth / minimumBlocksOnAxis;
			tmp = (int) Math.ceil(bHeight / width);
			if (tmp % 2 == 0) {
				tmp--;
			}
			height = bHeight / tmp;
		} else {
			height = bHeight / minimumBlocksOnAxis;
			tmp = (int) Math.ceil(bWidth / height);
			if (tmp % 2 == 0) {
				tmp--;
			}
			width = bWidth / tmp;
		}
		return new Dimension2D((int) width, (int) height);
	}

	private RectangleHalf calcSource(Rectangle2D viewWindow, Dimension2D blockSize) {
		return new RectangleHalf(
				Math.floor(viewWindow.getWidth() / blockSize.getWidth()),
				Math.floor(viewWindow.getHeight() / blockSize.getHeight())
		);
	}

	public Dimension2D getBlockSize() {
		return blockSize;
	}

}
