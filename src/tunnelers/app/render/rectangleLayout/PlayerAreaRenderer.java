package tunnelers.app.render.rectangleLayout;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Affine;
import tunnelers.app.render.FxRenderContainer;
import tunnelers.app.render.MapRenderer;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.gameRoom.WarzoneRules;
import tunnelers.core.model.entities.IntDimension;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.IntRectangle;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;

/**
 * TODO: This class is owed some refactorisation love. Unused arguments are
 * passed only to be passed more, etc...
 *
 * @author Stepan
 */
public class PlayerAreaRenderer {

	public static final int MIN_RENDERED_BLOCKS_ON_DIMENSION = 27;

	private final Dimension2D bounds;
	private final Rectangle2D renderWindow;
	private final Dimension2D blockSize;
	private final IntDimension sourceWindowSize;

	private final FxRenderContainer renderer;

	private final FontLoader fontLoader;

	private final Font statusBarFont;

	PlayerAreaRenderer(Dimension2D bounds, FxRenderContainer renderer) {
		this.bounds = bounds;
		this.renderWindow = new Rectangle2D(bounds.getWidth() * 0.05, bounds.getHeight() * 0.05, bounds.getWidth() * 0.9, bounds.getHeight() * 0.6);
		this.blockSize = calcBlockSize(this.renderWindow, MIN_RENDERED_BLOCKS_ON_DIMENSION);
		this.sourceWindowSize = calcSourceSize(this.renderWindow, this.blockSize);
		this.renderer = renderer;

		this.fontLoader = Toolkit.getToolkit().getFontLoader();

		int fontSize = 32;
		Font font = Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, fontSize);
		if (font == null) {
			font = Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, fontSize);
		}
		if (font == null) {
			font = Font.font(fontSize);
		}

		this.statusBarFont = font;
		System.out.println("Player area renderer initialized with font " + font.getName());

		renderer.setBlockSize(blockSize);
	}

	public Dimension2D getBounds() {
		return this.bounds;
	}

	protected void draw(GraphicsContext g, Dimension2D bounds, Tank tank) {
		if (this.renderer == null) {
			System.err.println("Renderer was not previously set");
			return;
		}
		Affine defTransform = g.getTransform();
		AColorScheme colors = renderer.getColorScheme();
		WarzoneRules rules = renderer.getWarzoneRules();

		g.setFill(colors.playerColors().get(tank).color());
		g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());

		g.translate(renderWindow.getMinX(), renderWindow.getMinY());
		drawViewWindow(g, tank.getLocation(), renderer.getTanks(), renderer.getProjectiles(), tank);
		g.setTransform(defTransform);

		Rectangle inBounds = new Rectangle(bounds.getWidth() * 0.8, bounds.getHeight() * 0.1);
		inBounds.setX(bounds.getWidth() * 0.1);
		inBounds.setY(bounds.getHeight() * 0.7);
		renderStatusBar(g, inBounds, colors.getUiHitpoints(), tank.getHitpoints(), rules.getTankMaxHP());

		inBounds.setY(bounds.getHeight() * 0.85);
		renderStatusBar(g, inBounds, colors.getUiEnergy(), tank.getEnergy(), rules.getTankMaxEP());
	}

	private void renderStatusBar(GraphicsContext g, Rectangle r, Color c, int value, int maxValue) {
		float pct = Math.max(0, Math.min(1.0f * value / maxValue, 1));
		int decimals = (int) Math.log10(maxValue) + 1;
		String textFormat = String.format("%%0%dd/%%%dd", decimals, decimals);

		g.setFill(Color.DIMGREY);
		g.fillRect(r.getX() - 2, r.getY() - 2, r.getWidth() + 4, r.getHeight() + 4);
		g.setFill(c);
		g.fillRect(r.getX(), r.getY(), r.getWidth() * pct, r.getHeight());

		String text = String.format(textFormat, value, maxValue);

		double width = fontLoader.computeStringWidth(text, this.statusBarFont);
		double height = fontLoader.getFontMetrics(this.statusBarFont).getLineHeight();

		double textX = r.getX() + (r.getWidth() - width) / 2;
		double textY = r.getY() + (r.getHeight() - height) / 2;

		g.setFont(this.statusBarFont);

		g.setFill(Color.BLACK);
		g.fillRoundRect(textX - 10, textY, width + 20, height, 15, 15);
		g.setStroke(c);
		g.strokeRoundRect(textX - 10, textY, width + 20, height, 15, 15);

		g.setFill(c);
		g.fillText(text, textX, textY + height * 7 / 9); // fixme: I'M SO SORRY

	}

	private void drawViewWindow(GraphicsContext g, IntPoint center, Tank[] tanks, Projectile[] projectiles, Tank currentTank) {
		Affine defTransform = g.getTransform();
		MapRenderer mr = renderer.getMapRenderer();

		g.setFill(Color.BLACK);
		g.fillRect(0 - 2, 0 - 2, renderWindow.getWidth() + 6, renderWindow.getHeight() + 4);
		IntRectangle source = alignSourceWindow(sourceWindowSize, center, mr.getMapSize());
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

		float staticPct = currentTank.getStatus() == Tank.Status.Destroyed ? 1
				: 0.8f * (1 - currentTank.getEnergy() / this.renderer.getWarzoneRules().getTankMaxEP());

		this.renderer.getAfterFX().renderStaticNoise(g, renderWindow, 0.8f * staticPct, this.blockSize);

	}

	private int drawProjectiles(GraphicsContext g, IntRectangle render, Projectile[] projectiles) {
		Affine defTransform = g.getTransform();
		int n = 0;
		for (Projectile projectile : projectiles) {
			if (projectile == null) {
				continue;
			}
			if (!render.contains(projectile.getLocation())) {
				continue;
			}
			renderer.offsetBlocks(g, projectile.getLocation());
			renderer.getAssetsRenderer().drawProjectile(projectile);

			g.setTransform(defTransform);
			n++;
		}

		return n;
	}

	private void drawTanks(GraphicsContext g, IntRectangle render, Tank[] tanks) {
		Affine defTransform = g.getTransform();

		for (Tank tank : tanks) {
			if (tank == null) {
				continue;
			}
			if (!render.contains(tank.getLocation())) {
				continue;
			}
			renderer.offsetBlocks(g, tank.getLocation());
			renderer.getAssetsRenderer().drawTank(tank);

			g.setTransform(defTransform);
		}
	}

	private IntRectangle alignSourceWindow(IntDimension source, IntPoint center, IntDimension mapSize) {
		int halfWidth = source.getWidth() / 2,
				halfHeight = source.getHeight() / 2;
		int mapWidth = mapSize.getWidth(),
				mapHeight = mapSize.getHeight();

		int x, y;

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

		return new IntRectangle(x, y, source.getWidth(), source.getHeight());
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

	private IntDimension calcSourceSize(Rectangle2D viewWindow, Dimension2D blockSize) {
		return new IntDimension(
				(int) Math.floor(viewWindow.getWidth() / blockSize.getWidth() - 1),
				(int) Math.floor(viewWindow.getHeight() / blockSize.getHeight() - 1)
		);
	}

	public Dimension2D getBlockSize() {
		return blockSize;
	}

}
