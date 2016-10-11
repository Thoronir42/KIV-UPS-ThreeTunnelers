package tunnelers.app.render.rectangleLayout;

import tunnelers.app.render.CanvasLayout;
import tunnelers.app.render.CanvasLayoutException;
import java.util.List;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class RectangularLayout extends CanvasLayout {

	public static CanvasLayout getLayoutFor(int playerCount, Dimension2D canvasArea) throws CanvasLayoutException {
		int rows = 1, cols = 2;
		while (rows * cols < playerCount) {
			if (cols > rows) {
				rows++;
			} else {
				cols++;
			}
		}
		return new RectangularLayout(rows, cols, canvasArea);
	}

	private final int rows, cols;

	private final PlayerAreaRenderer playerArea;

	public RectangularLayout(int rows, int cols, Dimension2D availableArea) {
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
		List<APlayer> players = this.renderer.getPlayers();
		Affine defTransform = g.getTransform();
		Dimension2D playerAreaBounds = this.playerArea.getBounds();
		int row = 0, col = 0;

		for (int i = 0; i < players.size(); i++) {
			g.translate(col * playerAreaBounds.getWidth(), row * playerAreaBounds.getHeight());
			this.playerArea.draw(g, playerAreaBounds, players.get(i), players);

			if (++col >= cols) {
				col = 0;
				row++;
			}
			g.setTransform(defTransform);
		}
	}
}
