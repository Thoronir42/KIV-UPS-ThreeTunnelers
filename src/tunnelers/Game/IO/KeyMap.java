package tunnelers.Game.IO;

import java.util.HashMap;
import java.util.Map.Entry;
import javafx.scene.input.KeyCode;
import tunnelers.Game.ControlSchemeManager;

/**
 *
 * @author Stepan
 */
public class KeyMap {

	public static String codeToStr(KeyCode kc) {
		if (kc == null) {
			return "N/A";
		}
		switch (kc) {

		}
		return kc.getName();
	}

	private final HashMap<KeyCode, PlrInput> map;
	private final ControlSchemeManager controlSchemeManager;

	public KeyMap(ControlSchemeManager controlSchemeManager) {
		map = new HashMap<>();
		this.controlSchemeManager = controlSchemeManager;
	}

	public void setSchemeDefault(byte sIndex) {
		AControlScheme controlScheme;
		switch (sIndex) {
			case ControlSchemeManager.KEYBOARD_PRIMARY:
				controlScheme = this.controlSchemeManager.getKeyboardScheme(sIndex);
				setScheme(controlScheme, KeyCode.UP, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.DOWN, KeyCode.NUMPAD0);
				break;
			case ControlSchemeManager.KEYBOARD_SECONDARY:
				controlScheme = this.controlSchemeManager.getKeyboardScheme(sIndex);
				setScheme(controlScheme, KeyCode.W, KeyCode.A, KeyCode.D, KeyCode.S, KeyCode.F);
				break;
		}
	}

	private void setScheme(AControlScheme controlScheme, KeyCode up, KeyCode left, KeyCode right, KeyCode down, KeyCode shoot) {
		set(up, controlScheme, Input.movUp);
		set(left, controlScheme, Input.movLeft);
		set(right, controlScheme, Input.movRight);
		set(down, controlScheme, Input.movDown);
		set(shoot, controlScheme, Input.actShoot);
	}

	private PlrInput set(KeyCode code, AControlScheme ctrlScheme, Input i) {
		return this.set(code, new PlrInput(ctrlScheme, i));

	}

	public PlrInput set(KeyCode code, PlrInput pin) {
		PlrInput cur = null;
		if (map.containsValue(pin)) {
			cur = map.remove(code);
		}
		map.put(code, pin);

		return cur;
	}

	public PlrInput getInput(KeyCode code) {
		return map.get(code);
	}

	public KeyCode findKey(PlrInput pin) {
		for (Entry<KeyCode, PlrInput> entry : map.entrySet()) {
			if (pin.equals(entry.getValue())) {
				return entry.getKey();
			}

		}
		return null;
	}
}
