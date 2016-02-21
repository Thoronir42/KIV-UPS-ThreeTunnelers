package tunnelers.Game.IO;

import java.util.HashMap;
import java.util.Map;
import tunnelers.Game.structure.Direction;

/**
 *
 * @author Stepan
 */
public class Controls {

	Map<Input, Boolean> keys;

	public Controls() {
		keys = new HashMap<>();
		Input[] inputs = Input.values();
		for (Input i : inputs) {
			keys.put(i, false);
		}
	}

	/**
	 *
	 * @param type Which input is being held
	 * @param newVal what value is being set under mentioned input
	 * @return true if the state on the input changed value
	 */
	public boolean handleControl(Input type, boolean newVal) {
		boolean prevVal = keys.get(type);
		keys.put(type, newVal);
		return prevVal != newVal;
	}

	public Direction getDirection() {
		int x = getDir(Input.movLeft, Input.movRight),
				y = getDir(Input.movUp, Input.movDown);
		return Direction.getDirection(x, y);
	}

	private int getDir(Input sub, Input add) {
		if (keys.get(sub) && !keys.get(add)) {
			return -1;
		}
		if (keys.get(add) && !keys.get(sub)) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return String.format("Controls(Up=%s\t Dw=%s\t Lf=%s\t Rg=%s\t Sh=%s)",
				keys.get(Input.movUp), keys.get(Input.movDown), keys.get(Input.movLeft),
				keys.get(Input.movRight), keys.get(Input.actShoot));
	}

}
