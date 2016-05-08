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

	private final HashMap<KeyCode, ControlInput> map;
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
		set(up, controlScheme, InputAction.movUp);
		set(left, controlScheme, InputAction.movLeft);
		set(right, controlScheme, InputAction.movRight);
		set(down, controlScheme, InputAction.movDown);
		set(shoot, controlScheme, InputAction.actShoot);
	}

	private ControlInput set(KeyCode code, AControlScheme ctrlScheme, InputAction i) {
		return this.set(code, new ControlInput(ctrlScheme, i));

	}

	public ControlInput set(KeyCode code, ControlInput pin) {
		ControlInput cur = null;
		map.remove(findKey(pin));
		if (map.containsKey(code)) {
			cur = map.remove(code);
		}
		map.put(code, pin);

		return cur;
	}

	public ControlInput getInput(KeyCode code) {
		return map.get(code);
	}

	public KeyCode findKey(ControlInput pin) {
		for (Entry<KeyCode, ControlInput> entry : map.entrySet()) {
			if (pin.equals(entry.getValue())) {
				return entry.getKey();
			}

		}
		return null;
	}
}
