package tunnelers.Game.IO;

import tunnelers.core.io.AControls;
import tunnelers.core.io.InputAction;
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
		// fixme: magic constants
		AControls controlScheme;
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

	private void setScheme(AControls controlScheme, KeyCode up, KeyCode left, KeyCode right, KeyCode down, KeyCode shoot) {
		set(up, controlScheme, InputAction.movUp);
		set(left, controlScheme, InputAction.movLeft);
		set(right, controlScheme, InputAction.movRight);
		set(down, controlScheme, InputAction.movDown);
		set(shoot, controlScheme, InputAction.actShoot);
	}

	private ControlInput set(KeyCode code, AControls ctrlScheme, InputAction i) {
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
