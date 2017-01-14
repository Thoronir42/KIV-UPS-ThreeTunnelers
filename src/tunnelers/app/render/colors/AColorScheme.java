package tunnelers.app.render.colors;

import javafx.scene.paint.Color;
import tunnelers.core.model.map.Block;
/**
 *
 * @author Stepan
 */
public abstract class AColorScheme{

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

	protected Color opacify(Color c, double opacity) {
		return Color.color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
	}

	public Color getRandColor() {
		return this.playerColors.getRandom().color();
	}

	public Color getRandStatic(int col, int row, double pct) {

		return opacify(this.playerColors.getRandom().color(), pct);
	}

	public abstract Color getUiHitpoints();

	public abstract Color getUiEnergy();
	
	public Color getCannonColor(){
		return cannonColor;
	}

	public Color getProjectileColor() {
		return projectileColor;
	}
}
