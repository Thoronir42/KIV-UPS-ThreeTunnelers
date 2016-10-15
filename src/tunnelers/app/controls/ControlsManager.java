package tunnelers.app.controls;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import tunnelers.core.io.AControls;
import tunnelers.core.io.InputAction;

/**
 *
 * @author Stepan
 */
public class ControlsManager {

	private EventHandler<InputEvent> onInputChanged;

	public byte[] getKeyboardLayoutIDs() {
		// TODO: optimize
		byte[] ids = new byte[this.keyboardSchemes.length];
		for (int i = 0; i < this.keyboardSchemes.length; i++) {
			ids[i] = this.keyboardSchemes[i].getID();
		}
		return ids;
	}

	public static InputAction[] getEditableInputs() {
		return new InputAction[]{
			InputAction.movUp, InputAction.movDown,
			InputAction.movLeft, InputAction.movRight,
			InputAction.actShoot,};
	}
//
	private final KeyMap keyMap;

	private final KeyboardControls[] keyboardSchemes;

	public ControlsManager() {
		this.keyMap = new KeyMap(this);
		this.keyboardSchemes = new KeyboardControls[2];
		for (byte i = 0; i < 2; i++) {
			this.keyboardSchemes[i] = new KeyboardControls(i);
			this.keyMap.setSchemeDefault(i);
		}
	}

	public AControls[] getAllSchemes() {
		AControls[] schemes = new AControls[this.keyboardSchemes.length];
		System.arraycopy(this.keyboardSchemes, 0, schemes, 0, schemes.length);

		return schemes;
	}

	public KeyboardControls getKeyboardScheme(byte sIndex) {
		return this.keyboardSchemes[sIndex];
	}

	public ControlInput getControlInputByKey(KeyCode code) {
		ControlInput pi = this.keyMap.getInput(code);
		return pi;
	}

	public KeyCode getKeyCode(ControlInput plrInput) {
		return this.keyMap.findKey(plrInput);
	}

	public ControlInput replaceKeyInput(KeyCode kc, ControlInput plrInput) {
		ControlInput oldOccurence = this.keyMap.set(kc, plrInput);
		return oldOccurence;
	}

	public void setOnInputChanged(EventHandler<InputEvent> onInputChanged) {
		this.onInputChanged = onInputChanged;
	}

	public void keyPressSet(KeyCode kc, boolean pressed) {
		ControlInput pi = this.getControlInputByKey(kc);
//		System.out.format("%s´= %s -> %s", kc, pressed, pi);
		if (pi == null) {
			return;
		}
		AControls controlSchemeId = pi.getControlScheme();
		InputAction inp = pi.getInput();

		if (this.onInputChanged != null) {
			this.onInputChanged.handle(new InputEvent(controlSchemeId.getPlayerID(), inp, pressed));
		}
	}

}
