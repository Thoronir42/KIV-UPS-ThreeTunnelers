package tunnelers.core.player.controls;

/**
 *
 * @author Stepan
 */
public final class Controls {

	protected final byte schemeID;

	private int state;

	public Controls(int byteId) {
		this((byte) byteId);
	}

	public Controls(byte schemeId) {
		this.schemeID = schemeId;
		state = 0;
	}

	public byte getID() {
		return this.schemeID;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 *
	 * @param action Which input is being held
	 * @param newValue what value is being set under mentioned input
	 * @return true if the state on the input changed value
	 */
	public boolean set(InputAction action, boolean newValue) {
		int mask = calcMask(action);
		boolean prevValue = this.get(action);

		if (newValue) {
			this.state |= mask;
		} else {
			this.state &= ~mask;

		}

		return prevValue != newValue;
	}

	public boolean get(InputAction action) {
		int mask = calcMask(action);
		return (this.state & mask) > 0;
	}
	
	private int calcMask(InputAction action){
		return 1 << action.intVal();
	}

	@Override
	public String toString() {
		return String.format("Controls(Up=%s\t Dw=%s\t Lf=%s\t Rg=%s\t Sh=%s)",
				this.get(InputAction.movUp), this.get(InputAction.movDown), this.get(InputAction.movLeft),
				this.get(InputAction.movRight), this.get(InputAction.actShoot));
	}
}
