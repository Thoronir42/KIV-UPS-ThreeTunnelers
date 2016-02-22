package tunnelers.Game.IO;

import java.util.HashMap;
import java.util.Map.Entry;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Stepan
 */
public class KeyMap {

	public static final byte PLAYER_PRIMARY = 1,
			PLAYER_SECONDARY = 2;

	private final HashMap<KeyCode, PlrInput> map;

	public KeyMap() {
		map = new HashMap<>();
	}

	public void resetAll() {
		for (byte pIndex = 1; pIndex <= 2; pIndex++) {
			resetPlayer(pIndex);
		}
	}

	public void resetPlayer(byte pIndex) {
		switch (pIndex) {
			case PLAYER_PRIMARY:
				set(KeyCode.UP, pIndex, Input.movUp);
				set(KeyCode.LEFT, pIndex, Input.movLeft);
				set(KeyCode.RIGHT, pIndex, Input.movRight);
				set(KeyCode.DOWN, pIndex, Input.movDown);
				set(KeyCode.NUMPAD0, pIndex, Input.actShoot);
				break;
			case PLAYER_SECONDARY:
				set(KeyCode.W, pIndex, Input.movUp);
				set(KeyCode.A, pIndex, Input.movLeft);
				set(KeyCode.D, pIndex, Input.movRight);
				set(KeyCode.S, pIndex, Input.movDown);
				set(KeyCode.F, pIndex, Input.actShoot);
				break;
		}
	}

	public PlrInput set(KeyCode code, byte pIndex, Input i) {
		return this.set(code, new PlrInput(pIndex, i));
		
	}
	
	public PlrInput set(KeyCode code, PlrInput pin){
		PlrInput cur = null;
		if (map.containsValue(pin)) {
			cur = map.get(code);
		}
		map.put(code, pin);

		return cur;
	}

	public PlrInput getInput(KeyCode code) {
		return map.get(code);
	}

	public KeyCode getKey(PlrInput pin) {
		for (Entry<KeyCode, PlrInput> entry : map.entrySet()) {
			if (pin.equals(entry.getValue())) {
				return entry.getKey();
			}

		}
		return null;
	};
}
