package tunnelers.app.render.colors;

import javafx.scene.paint.Color;
import tunnelers.core.model.map.Block;
import tunnelers.core.view.IColorScheme;

/**
 *
 * @author Stepan
 */
public abstract class AColorScheme implements IColorScheme {

	protected final PlayerColors playerColors;

	public AColorScheme() {
		this(new PlayerColors());
	}

	public AColorScheme(PlayerColors colors) {
		this.playerColors = colors;
	}

	public abstract Color getError();

	public abstract Color getBlockColor(int x, int y, Block block);

	public PlayerColors playerColors() {
		return this.playerColors;
	}

	protected Color opacify(Color c, double opacity) {
		return Color.color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
	}

	public Color getRandColor() {
		return this.playerColors.getRandom();
	}

	public Color getRandStatic(int col, int row, double pct) {

		return opacify(this.playerColors.getRandom(), pct);
	}

	public abstract Color getUiHitpoints();

	public abstract Color getUiEnergy();

	@Override
	public int getAvailablePlayerColors() {
		return this.playerColors.size();
	}
}
