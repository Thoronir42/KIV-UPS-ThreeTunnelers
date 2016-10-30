package tunnelers.app.controls;

import tunnelers.core.player.Controls;
import tunnelers.core.player.InputAction;
import java.util.HashMap;
import java.util.Map.Entry;
import javafx.scene.input.KeyCode;

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
	private final ControlsManager controlSchemeManager;

	public KeyMap(ControlsManager controlSchemeManager) {
		map = new HashMap<>();
		this.controlSchemeManager = controlSchemeManager;
	}

	public void setSchemeDefault(byte sIndex) {
		// fixme: magic constants
		Controls controlScheme;
		switch (sIndex) {
			case 0:
				controlScheme = this.controlSchemeManager.getKeyboardScheme(sIndex);
				setScheme(controlScheme, KeyCode.UP, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.DOWN, KeyCode.NUMPAD0);
				break;
			case 1:
				controlScheme = this.controlSchemeManager.getKeyboardScheme(sIndex);
				setScheme(controlScheme, KeyCode.W, KeyCode.A, KeyCode.D, KeyCode.S, KeyCode.F);
				break;
		}
	}

	private void setScheme(Controls controlScheme, KeyCode up, KeyCode left, KeyCode right, KeyCode down, KeyCode shoot) {
		set(up, controlScheme, InputAction.movUp);
		set(left, controlScheme, InputAction.movLeft);
		set(right, controlScheme, InputAction.movRight);
		set(down, controlScheme, InputAction.movDown);
		set(shoot, controlScheme, InputAction.actShoot);
	}

	private ControlInput set(KeyCode code, Controls ctrlScheme, InputAction i) {
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
