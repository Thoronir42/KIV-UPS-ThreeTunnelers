package tunnelers.app.render.colors;

import javafx.scene.paint.Color;
import tunnelers.core.model.map.Block;

public class FxDefaultColorScheme extends AColorScheme {

	private final Color[] breakable = {Color.BURLYWOOD, Color.BURLYWOOD.interpolate(Color.BROWN, 0.1)};
	private final Color[] tough = {Color.DARKGREY};
	private final Color[] empty = {Color.BLACK};

	private final Color UI_ENERGY = Color.DEEPPINK;
	private final Color UI_HITPOINTS = Color.LAWNGREEN;

	private ILocationRandomizer randomizer;

	public FxDefaultColorScheme(FxPlayerColorManager playerColors) {
		super(playerColors);
		this.randomizer = (int x, int y) -> 0;
	}

	public void setRandomizer(ILocationRandomizer randomizer) {
		this.randomizer = randomizer;
	}

	@Override
	public Color getError() {
		return playerColors.getRandom().color();
	}

	@Override
	public Color getBlockColor(int x, int y, Block block) {
		if (block.equals(Block.Undefined)) {
			return this.getError();
		}
		int var = this.randomizer.calc(x, y);
		Color[] c = getColGroup(block);
		if (c == null) {
			return this.getError();
		}

		return c[var % c.length];
	}

	private Color[] getColGroup(Block b) {
		switch (b) {
			case Dirt:
				return breakable;
			case Rock:
				return tough;
			case Empty:
				return empty;
		}

		return null;
	}

	@Override
	public Color getUiHitpoints() {
		return UI_HITPOINTS;
	}

	@Override
	public Color getUiEnergy() {
		return UI_ENERGY;
	}

	public FxPlayerColorManager getPlayerColorManager() {
		return this.playerColors;
	}
}
