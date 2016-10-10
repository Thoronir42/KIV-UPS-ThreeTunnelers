package tunnelers.Game;

import javafx.scene.input.KeyCode;
import tunnelers.core.io.AControls;
import tunnelers.core.io.InputAction;
import tunnelers.Game.IO.KeyMap;
import tunnelers.Game.IO.ControlInput;
import tunnelers.Game.IO.KeyboardControls;

/**
 *
 * @author Stepan
 */
public class ControlSchemeManager {

	public byte[] getKeyboardLayoutIDs() {
		// TODO: optimize
		byte[] ids = new byte[this.keyboardSchemes.length];
		for (int i = 0; i < this.keyboardSchemes.length; i++){
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

	public ControlSchemeManager() {
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

	public ControlInput getPlayerInputByKeyPress(KeyCode code) {
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

}
