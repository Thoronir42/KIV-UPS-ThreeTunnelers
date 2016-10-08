package tunnelers.app.render.colors;

import generic.RNG;
import java.util.Arrays;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class PlayerColors {

	protected final Color[] colors;
	protected final boolean[] usage;

	public PlayerColors() {
		this(new Color[]{
			Color.web("0x55D43F"), // 01. green
			Color.web("0x000084"), // 02. navy blue
			Color.web("0xFF6600"), // 03. orange
			Color.web("0x663399"), // 04. purple
			Color.web("0xFF0080"), // 05. deep pink
			Color.web("0x3399FF"), // 06. blue
			Color.web("0xFFC200"), // 07. orange yellow
			Color.web("0x9C2A00"), // 08. red orange
			Color.web("0xFEFCD7"), // 09. moon glow
			Color.web("0xAA0078"), // 10. fuchsia
			Color.web("0xFE4902"), // 11. vermilion
			Color.web("0x005A04"), // 12. camarone
			Color.web("0x66A7C5"), // 13. teal
		});
	}

	public PlayerColors(Color[] colors) {
		this.colors = colors;
		this.usage = new boolean[colors.length];
	}

	public int size(){
		return this.colors.length;
	}
	
	public Color getColor(Color color, int colorId) {
		boolean available = (colorId >= 0 && colorId < colors.length) && !this.usage[colorId];
		int oldCol = (color == null) ? -1 : Arrays.asList(colors).indexOf(color);

		if (oldCol == -1) {
			if (available) {
				this.usage[colorId] = true;
				return colors[colorId];
			} else {
				int unused = this.colorFirstUnused();
				if (unused != -1) {
					return this.getColor(null, unused);
				} else {
					return colors[RNG.getRandInt(colors.length)];
				}
			}
		} else {
			if (!available || oldCol == colorId) {
				return color;
			}
			this.usage[oldCol] = false;
			return getColor(null, colorId);
		}
	}

	private int colorFirstUnused() {
		for (int i = 0; i < colors.length; i++) {
			if (!this.usage[i]) {
				return i;
			}
		}
		return -1;
	}

	public Color get(int i) {
		return this.colors[i];
	}
}
