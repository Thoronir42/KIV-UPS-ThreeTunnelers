package tunnelers.app.render.colors;

import generic.RNG;
import javafx.scene.paint.Color;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class AColorScheme {

	protected static final String SYSTEM_COLOR = "800000";
	
	private final Color[] breakable = {Color.BURLYWOOD, Color.BURLYWOOD.interpolate(Color.BROWN, 0.1)};
	private final Color[] tough = {Color.DARKGREY};
	private final Color[] empty = {Color.DARKRED};
	public final Color error = Color.RED;

	private final Color cannonColor = Color.GOLD;

	public final Color UI_ENERGY = Color.DEEPPINK;
	public final Color UI_HITPOINTS = Color.LAWNGREEN;

	protected final PlayerColors playerColors;

	public AColorScheme() {
		this(new PlayerColors());
	}

	public AColorScheme(PlayerColors colors) {
		this.playerColors = colors;
	}

	public Color getBlockColor(int x, int y, Block block) {
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

	private Color[] getColGroup(Block b) {
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

	public Color getRandPlayerColor(double opacity) {
		int i = RNG.getRandInt(this.playerColors.size());
		return opacify(this.playerColors.get(i), opacity);
	}

	private Color opacify(Color c, double opacity) {
		return Color.color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
	}

	public Color getRandColor() {
		return getRandPlayerColor(1);
	}

	public Color getRandStatic(int col, int row, double pct) {
		return getRandPlayerColor(0.2);
	}

	public Color getCannonColor() {
		return cannonColor;
	}
	
	public Color getSystemColor(){
		return Color.web(SYSTEM_COLOR);
	}

	public Color getPlayerColor(int id) {
		return this.playerColors.get(id);
	}

	public Color getPlayerColor(APlayer player) {
		return this.getPlayerColor(player.getColor());
	}
}
