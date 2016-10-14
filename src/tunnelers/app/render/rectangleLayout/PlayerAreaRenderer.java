package tunnelers.app.render.rectangleLayout;

import java.util.Collection;
import java.util.List;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import tunnelers.app.render.FxRenderHelper;
import tunnelers.app.render.MapRenderer;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class PlayerAreaRenderer {

	private final Dimension2D bounds;
	private final Rectangle viewWindow;
	private final Dimension2D blockSize;
	private final RectangleHalf render;
	
	private FxRenderHelper renderer;

	PlayerAreaRenderer(Dimension2D bounds) {
		this.bounds = bounds;
		this.viewWindow = new Rectangle(bounds.getWidth() * 0.05, bounds.getHeight() * 0.05, bounds.getWidth() * 0.9, bounds.getHeight() * 0.6);
		this.blockSize = calcBlockSize(this.viewWindow, 37);
		this.render = calcRender(this.viewWindow, this.blockSize);
	}

	public Dimension2D getBounds() {
		return this.bounds;
	}

	protected void draw(GraphicsContext g, Dimension2D bounds, APlayer currentPlayer, List<APlayer> players) {
		Affine defTransform = g.getTransform();
		
		if(this.renderer == null){
			System.err.println("Renderer was not previously set");
			return;
		}
		
		AColorScheme colors = renderer.getColorScheme();
		
		g.setFill(colors.getPlayerColor(currentPlayer));
		g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());

		g.translate(viewWindow.getX(), viewWindow.getY());
		drawViewWindow(g, players, currentPlayer);
		g.setTransform(defTransform);

		Rectangle inBounds = new Rectangle(bounds.getWidth() * 0.8, bounds.getHeight() * 0.1);
		inBounds.setX(bounds.getWidth() * 0.1);
		inBounds.setY(bounds.getHeight() * 0.7);
		fillStatusBar(g, inBounds, colors.UI_HITPOINTS);

		inBounds.setY(bounds.getHeight() * 0.85);
		fillStatusBar(g, inBounds, colors.UI_ENERGY);
	}

	private void fillStatusBar(GraphicsContext g, Rectangle r, Color c) {
		g.setFill(Color.DIMGREY);
		g.setLineWidth(4);
		g.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
		g.setFill(c);
		g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	private void drawViewWindow(GraphicsContext g, List<APlayer> p, APlayer curPlayer) {
		Affine defTransform = g.getTransform();
		
		MapRenderer mr = renderer.getMapRenderer();
		
		g.setFill(Color.BLACK);
		g.fillRect(0 - 2, 0 - 2, viewWindow.getWidth() + 6, viewWindow.getHeight() + 4);
		clampRender(render, curPlayer.getTank().getLocation(), mr.getMapBounds());
		try {
			g.translate(-render.getX() * blockSize.getWidth(),
					-render.getY() * blockSize.getHeight());
			mr.drawMap(render);
			this.drawTanks(g, render, p);
		} catch (Exception e) {
			System.err.println("Error with drawViewWindow: " + e.getMessage());
			throw e;
		} finally {
			g.setTransform(defTransform);
		}

		this.renderStatic(g, render, curPlayer.getTank().getEnergyStatus());

	}

	private void drawTanks(GraphicsContext g, Rectangle render, Collection<APlayer> players) {
		Affine defTransform = g.getTransform();
		double bw = blockSize.getWidth(),
				bh = blockSize.getHeight();
		for (APlayer plr : players) {
			Tank t = plr.getTank();
			Point2D po = t.getLocation();
			if (render.contains(po)) {
				po = new Point2D(po.getX() * bw, po.getY() * bh);
				g.setFill(renderer.getColorScheme().getPlayerColor(plr));
				g.translate(po.getX(), po.getY());
				renderer.getAssetsRenderer().drawTank(t);
				g.setTransform(defTransform);
			}
		}
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
