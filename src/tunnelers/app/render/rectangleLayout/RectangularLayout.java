package tunnelers.app.render.rectangleLayout;

import tunnelers.app.render.ARenderLayout;
import tunnelers.app.render.RenderLayoutException;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import tunnelers.app.render.FxRenderContainer;
import tunnelers.core.model.entities.Tank;

/**
 *
 * @author Stepan
 */
public class RectangularLayout extends ARenderLayout {

	public static ARenderLayout getLayoutFor(FxRenderContainer renderer, int playerCount, Dimension2D canvasArea) throws RenderLayoutException {
		int rows = 1, cols = 2;
		while (rows * cols < playerCount) {
			if (cols > rows) {
				rows++;
			} else {
				cols++;
			}
		}
		return new RectangularLayout(renderer, rows, cols, canvasArea);
	}

	private final int rows, cols;

	private final PlayerAreaRenderer playerArea;

	public RectangularLayout(FxRenderContainer renderer, int rows, int cols, Dimension2D availableArea) {
		super(renderer);
		this.rows = rows;
		this.cols = cols;
		Dimension2D playerAreaBounds = new Dimension2D(availableArea.getWidth() / cols,
				availableArea.getHeight() / rows);
		this.playerArea = new PlayerAreaRenderer(playerAreaBounds, renderer);
	}

	protected int getRowAmount() {
		return this.rows;
	}

	protected int getColAmount() {
		return this.cols;
	}

	@Override
	public void draw(GraphicsContext g) {
		Tank[] tanks = this.renderer.getTanks();
		Affine defTransform = g.getTransform();
		Dimension2D playerAreaBounds = this.playerArea.getBounds();
		int row = 0, col = 0;

		for (Tank tank : tanks) {
			if (tank == null) {
				continue;
			}
			g.translate(col * playerAreaBounds.getWidth(), row * playerAreaBounds.getHeight());
			this.playerArea.draw(g, playerAreaBounds, tank);
			if (++col >= cols) {
				col = 0;
				row++;
			}
			g.setTransform(defTransform);
		}
	}
}
