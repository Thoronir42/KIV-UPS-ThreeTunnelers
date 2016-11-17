package tunnelers.core.player.controls;

import tunnelers.core.model.entities.Direction;

/**
 *
 * @author Stepan
 */
public final class Controls {

	protected final byte schemeID;
	protected int playerID;
	
	private final boolean[] heldKeys;
	
	public Controls(int byteId){
		this((byte) byteId);
	}
	
	public Controls(byte schemeId) {
		this.schemeID = schemeId;
		
		InputAction[] inputs = InputAction.values();
		heldKeys = new boolean[inputs.length];
		for (InputAction input : inputs) {
			heldKeys[input.intVal()] = false;
		}
	}

	public byte getID() {
		return this.schemeID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int id) {
		this.playerID = id;
	}
	
	

	/**
	 *
	 * @param type Which input is being held
	 * @param newVal what value is being set under mentioned input
	 * @return true if the state on the input changed value
	 */
	public boolean setControlState(InputAction type, boolean newVal) {
		return this.set(type, newVal);
	}

	public boolean isShooting() {
		return this.heldKeys[InputAction.actShoot.intVal()];
	}

	public Direction getDirection() {
		int x = getDir(InputAction.movLeft, InputAction.movRight),
				y = getDir(InputAction.movUp, InputAction.movDown);
		return Direction.getDirection(x, y);
	}

	private int getDir(InputAction sub, InputAction add) {
		if (this.get(sub) && !this.get(add)) {
			return -1;
		}
		if (this.get(add) && !this.get(sub)) {
			return 1;
		}
		return 0;
	}
	
	private boolean get(InputAction action){
		return this.heldKeys[action.intVal()];
	}
	
	private boolean set(InputAction action, boolean newValue){
		boolean prevValue = heldKeys[action.intVal()];
		heldKeys[action.intVal()] = newValue;
		
		return prevValue != newValue;
	}

	@Override
	public String toString() {
		return String.format("Controls(Up=%s\t Dw=%s\t Lf=%s\t Rg=%s\t Sh=%s)",
				this.get(InputAction.movUp),    this.get(InputAction.movDown), this.get(InputAction.movLeft),
				this.get(InputAction.movRight), this.get(InputAction.actShoot));
	}
}
