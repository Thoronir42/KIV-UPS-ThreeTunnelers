package tunnelers.Game.Render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.model.GameContainer;

/**
 *
 * @author Stepan
 */
public abstract class CanvasLayout {

	public static CanvasLayout choseIdeal(GameContainer container, Dimension2D d) {
		try {
			CanvasLayout layout = RectangularCanLayout.getLayoutFor(container, d);
			return layout;
		} catch (CanvasLayoutException e) {
			throw new IllegalArgumentException(String.format("Could not find layout suitable for %d players.", container.getPlayerCount()));
		}
	}

	protected final GameContainer container;
	protected Renderer renderer;

	public CanvasLayout(GameContainer c) {
		this.container = c;
	}

	public void setRenderer(Renderer r) {
		this.renderer = r;
	}

	public abstract Dimension2D getBlockSize();

	public abstract void drawLayout(GraphicsContext g);
}
