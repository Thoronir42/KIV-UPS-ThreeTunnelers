package tunnelers.Game.Render;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import tunnelers.Game.Map.Block;
import tunnelers.Configuration.Settings;

/**
 *
 * @author Stepan
 */
public class TunColors {

	private static final Color[] breakable = {Color.BURLYWOOD, Color.BURLYWOOD.interpolate(Color.BROWN, 0.1)};
	private static final Color[] tough = {Color.DARKGREY};
	private static final Color[] empty = {Color.DARKRED};
	public static final Color error = Color.RED;

	private static final Color cannonColor = Color.GOLD;

	public static final Color UI_ENERGY = Color.DEEPPINK;
	public static final Color UI_HITPOINTS = Color.LAWNGREEN;

	public static Color getBlockColor(int x, int y, Block block) {
		if (block.equals(Block.Undefined)) {
			return error;
		}
		int var = ((int) Math.abs(Math.sin((x + 2) * 7) * 6 + Math.cos(y * 21) * 6));
		Color[] c = getColGroup(block);
		if (c == null) {
			return error;
		}
		return c[var % c.length];
	}

	private static Color[] getColGroup(Block b) {
		switch (b) {
			case Breakable:
				return breakable;
			case Tough:
				return tough;
			case Empty:
				return empty;
		}
		return null;
	}

	public static Color getRandPlayerColor(double opacity) {
		int i = Settings.getRandInt(Settings.PLAYER_COLORS.length);
		return opacify(Settings.PLAYER_COLORS[i], opacity);
	}

	private static Color opacify(Color c, double opacity) {
		return Color.color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
	}

	public static Color getRandColor() {
		return getRandPlayerColor(1);
	}

	public static Paint getRandStatic(int col, int row, double pct) {
		return getRandPlayerColor(0.2);
	}

	public static Color getCannonColor() {
		return cannonColor;
	}
}
