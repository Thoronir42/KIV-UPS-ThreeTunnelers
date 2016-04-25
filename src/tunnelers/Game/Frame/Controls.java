package tunnelers.Game.Frame;

import java.util.HashMap;
import java.util.Map;
import tunnelers.Game.IO.InputAction;

/**
 *
 * @author Stepan
 */
public class Controls {

	Map<InputAction, Boolean> heldKeys;

	public Controls() {
		heldKeys = new HashMap<>();
		InputAction[] inputs = InputAction.values();
		for (InputAction input : inputs) {
			heldKeys.put(input, false);
		}
	}

	/**
	 *
	 * @param type Which input is being held
	 * @param newVal what value is being set under mentioned input
	 * @return true if the state on the input changed value
	 */
	public boolean handleControl(InputAction type, boolean newVal) {
		boolean prevVal = heldKeys.get(type);
		heldKeys.put(type, newVal);
		return prevVal != newVal;
	}

	public boolean isShooting() {
		return this.heldKeys.get(InputAction.actShoot);
	}

	public Direction getDirection() {
		int x = getDir(InputAction.movLeft, InputAction.movRight),
				y = getDir(InputAction.movUp, InputAction.movDown);
		return Direction.getDirection(x, y);
	}

	private int getDir(InputAction sub, InputAction add) {
		if (heldKeys.get(sub) && !heldKeys.get(add)) {
			return -1;
		}
		if (heldKeys.get(add) && !heldKeys.get(sub)) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return String.format("Controls(Up=%s\t Dw=%s\t Lf=%s\t Rg=%s\t Sh=%s)",
				heldKeys.get(InputAction.movUp), heldKeys.get(InputAction.movDown), heldKeys.get(InputAction.movLeft),
				heldKeys.get(InputAction.movRight), heldKeys.get(InputAction.actShoot));
	}

}
