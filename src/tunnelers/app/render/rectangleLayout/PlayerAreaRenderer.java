package tunnelers.app.render.rectangleLayout;

import java.util.Collection;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import tunnelers.app.render.FxRenderHelper;
import tunnelers.app.render.MapRenderer;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;

/**
 *
 * @author Stepan
 */
public class PlayerAreaRenderer {

	public static final int MIN_RENDERED_BLOCKS_ON_DIMENSION = 27;

	private final Dimension2D bounds;
	private final Rectangle viewWindow;
	private final Dimension2D blockSize;
	private final RectangleHalf render;

	private FxRenderHelper renderer;

	PlayerAreaRenderer(Dimension2D bounds) {
		this.bounds = bounds;
		this.viewWindow = new Rectangle(bounds.getWidth() * 0.05, bounds.getHeight() * 0.05, bounds.getWidth() * 0.9, bounds.getHeight() * 0.6);
		this.blockSize = calcBlockSize(this.viewWindow, MIN_RENDERED_BLOCKS_ON_DIMENSION);
		this.render = calcRender(this.viewWindow, this.blockSize);
	}

	public Dimension2D getBounds() {
		return this.bounds;
	}

	protected void draw(GraphicsContext g, Dimension2D bounds, Tank currentTank) {
		Collection<Tank> players = renderer.getTanks();
		Collection<Projectile> projectiles = renderer.getProjectiles();

		Affine defTransform = g.getTransform();

		if (this.renderer == null) {
			System.err.println("Renderer was not previously set");
			return;
		}
		
		AColorScheme colors = renderer.getColorScheme();

		g.setFill(colors.playerColors().get(currentTank));
		g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());

		g.translate(viewWindow.getX(), viewWindow.getY());
		drawViewWindow(g, currentTank.getLocation(), players, projectiles);
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

	private void drawViewWindow(GraphicsContext g, Point2D center, Collection<Tank> tanks, Collection<Projectile> projectiles) {
		Affine defTransform = g.getTransform();
		MapRenderer mr = renderer.getMapRenderer();

		g.setFill(Color.BLACK);
		g.fillRect(0 - 2, 0 - 2, viewWindow.getWidth() + 6, viewWindow.getHeight() + 4);
		clampRender(render, center, mr.getMapBounds());
		try {
			
			this.renderer.offsetBlocks(-render.getX(), -render.getY());
			mr.drawMap(render);
			int rendered = this.drawProjectiles(g, viewWindow, projectiles);
			System.out.format("%03.0fx%03.0f - Rendering %d projectiles\n", center.getX(), center.getY(), rendered);
			this.drawTanks(g, render, tanks);
			
			g.setTransform(defTransform);
		} catch (Exception e) {
			System.err.println("Error with drawViewWindow: " + e.getMessage());
			g.setTransform(defTransform);
			throw e;
		}

		//this.renderStatic(g, render, curPlayer.getTank().getEnergyPct());
		this.renderStatic(g, render, 0.95);

	}

	private int drawProjectiles(GraphicsContext g, Rectangle render, Collection<Projectile> projectiles) {
		Affine defTransform = g.getTransform();
		
		return (int)projectiles.stream().filter(
				(projectile) -> (render.contains(projectile.getLocation()))
		).peek((proj) -> {
			renderer.offsetBlocks(proj.getLocation());
			renderer.getAssetsRenderer().drawProjectile(proj);
			
			g.setTransform(defTransform);
		}).count();
	}

	private void drawTanks(GraphicsContext g, Rectangle render, Collection<Tank> tanks) {
		Affine defTransform = g.getTransform();
		
		tanks.stream().filter(
				(tank) -> (render.contains(tank.getLocation()))
		).forEach((t) -> {
			renderer.offsetBlocks(t.getLocation());
			renderer.getAssetsRenderer().drawTank(t);
			
			g.setTransform(defTransform);
		});
	}

	private void clampRender(RectangleHalf render, Point2D center, Dimension2D mapSize) {
		double halfWidth = render.getHalfWidth(),
				halfHeight = render.getHalfHeight();
		double mapWidth = mapSize.getWidth(),
				mapHeight = mapSize.getHeight();

		if (center.getX() - halfWidth < 0) {
			render.setX(0);
		} else if (center.getX() + halfWidth > mapWidth) {
			render.setX(mapWidth - 2 * halfWidth);
		} else {
			render.setX(center.getX() - (int) halfWidth);
		}

		if (center.getY() - halfHeight < 0) {
			render.setY(0);
		} else if (center.getY() + halfHeight > mapHeight) {
			render.setY(mapHeight - 2 * halfHeight);
		} else {
			render.setY(center.getY() - (int) halfHeight);
		}
	}

	private void renderStatic(GraphicsContext g, Rectangle render, double energyPct) {
		for (int col = 0; col < render.getWidth(); col++) {
			for (int row = 0; row < render.getHeight(); row++) {
				g.setFill(renderer.getColorScheme().getRandStatic(col, row, 1 - energyPct));
				g.fillRect(col * blockSize.getWidth(), row * blockSize.getHeight(), blockSize.getWidth(), blockSize.getHeight());
			}
		}
	}

	private Dimension2D calcBlockSize(Rectangle viewWindow, int minimumBlocksOnAxis) {
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

	private RectangleHalf calcRender(Rectangle viewWindow, Dimension2D blockSize) {
		return new RectangleHalf(
				Math.floor(viewWindow.getWidth() / blockSize.getWidth()),
				Math.floor(viewWindow.getHeight() / blockSize.getHeight())
		);
	}

	void setRenderer(FxRenderHelper renderer) {
		this.renderer = renderer;
	}

	public Dimension2D getBlockSize() {
		return blockSize;
	}

}
