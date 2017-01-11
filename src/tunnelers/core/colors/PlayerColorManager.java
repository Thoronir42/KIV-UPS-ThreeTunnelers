package tunnelers.core.colors;

import generic.RNG;
import tunnelers.core.chat.IChatParticipant;

/**
 *
 * @author Stepan
 * @param <PCC> player color class
 */
public class PlayerColorManager<PCC extends PlayerColor> {

	protected final PCC[] colors;
	private final PCC systemColor;

	public PlayerColorManager(PCC[] colors, PCC systemColor) {
		this.colors = colors;
		this.systemColor = systemColor;
	}

	public int size() {
		return this.colors.length;
	}

	/**
	 * TODO: Revise this naughty method
	 *
	 * @param current
	 * @param colorId
	 * @return
	 * @throws IllegalStateException
	 */
	public PCC useColor(PCC current, int colorId) throws IllegalStateException {
		boolean available = colorId >= 0 && colorId < colors.length && !this.colors[colorId].isInUse();
		int oldCol = (current == null) ? -1 : current.intValue();

		if (oldCol == -1) {
			if (available) {
				this.colors[colorId].setInUse(true);
				return colors[colorId];
			} else {
				return this.useColor(null, this.colorFirstUnused());
			}
		} else {
			if (!available || oldCol == colorId) {
				return current;
			}
			this.colors[oldCol].setInUse(false);
			return useColor(null, colorId);
		}
	}

	public PCC useRandomColor() {
		return this.useColor(null, RNG.getRandInt(this.colors.length));
	}

	private int colorFirstUnused() throws IllegalStateException {
		for (int i = 0; i < colors.length; i++) {
			if (!this.colors[i].isInUse()) {
				return i;
			}
		}
		throw new IllegalStateException("Attempted to use more colors than we recognise.");
	}

	public PCC get(int i) {
		if (i == IChatParticipant.SYSTEM_ID) {
			return systemColor;
		}
		return this.colors[i];
	}

	public PCC get(IColorable c) {
		return this.get(c.getColor());
	}

	public PCC getRandom() {
		int i = RNG.getRandInt(this.colors.length);
		return this.get(i);
	}

	public void resetColorUsage() {
		for (PCC color : this.colors) {
			color.setInUse(false);
		}
	}
}
