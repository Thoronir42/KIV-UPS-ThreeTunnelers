package tunnelers.app.render.colors;

import javafx.scene.paint.Color;
import tunnelers.core.model.map.Block;

public abstract class AColorScheme {

	protected final FxPlayerColorManager playerColors;

	private final Color cannonColor = Color.GOLD;
	private final Color projectileColor = Color.YELLOW;

	public AColorScheme(FxPlayerColorManager colors) {
		this.playerColors = colors;
	}

	public abstract Color getError();

	public abstract Color getBlockColor(int x, int y, Block block);

	public FxPlayerColorManager playerColors() {
		return this.playerColors;
	}

	public Color getRandColor() {
		return this.playerColors.getRandom().color();
	}

	public Color getRandStatic(int col, int row, double pct) {
		Color c = this.playerColors.getRandom().color();
		return Color.color(c.getRed(), c.getGreen(), c.getBlue(), pct);
	}

	public abstract Color getUiHitpoints();

	public abstract Color getUiEnergy();

	public Color getCannonColor() {
		return cannonColor;
	}

	public Color getProjectileColor() {
		return projectileColor;
	}
}
