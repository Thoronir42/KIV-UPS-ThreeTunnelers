package tunnelers.app.controls;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.player.controls.ControlInput;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;

import java.util.Arrays;

public class FxControlsManager extends AControlsManager {

	private EventHandler<InputEvent> onInputChanged;

	private final FxKeyMap keyMap;
	private final Controls[] keyboardSchemes;

	public FxControlsManager(int enabledSchemes) {
		this.keyboardSchemes = new Controls[enabledSchemes];
		this.keyMap = new FxKeyMap(this);
		for (byte i = 0; i < enabledSchemes; i++) {
			this.keyboardSchemes[i] = new Controls(i);
			this.keyMap.setSchemeDefault(i);
		}
	}

	public byte[] getKeyboardLayoutIDs() {
		// TODO: optimize
		byte[] ids = new byte[this.keyboardSchemes.length];
		for (int i = 0; i < this.keyboardSchemes.length; i++) {
			ids[i] = this.keyboardSchemes[i].getID();
		}
		return ids;
	}

	@Override
	public Controls[] getAllSchemes() {
		return Arrays.copyOf(this.keyboardSchemes, this.keyboardSchemes.length);
	}

	@Override
	public Controls getScheme(byte id) {
		return this.getKeyboardScheme(id);
	}

	public Controls getKeyboardScheme(byte sIndex) {
		return this.keyboardSchemes[sIndex];
	}

	public ControlInput getControlInputByKey(KeyCode code) {
		return this.keyMap.getInput(code);
	}

	public KeyCode getKeyCode(ControlInput plrInput) {
		return this.keyMap.findKey(plrInput);
	}

	public ControlInput replaceKeyInput(KeyCode kc, ControlInput plrInput) {
		return this.keyMap.set(kc, plrInput);
	}

	public void setOnInputChanged(EventHandler<InputEvent> onInputChanged) {
		this.onInputChanged = onInputChanged;
	}

	public void keyPressSet(KeyCode kc, boolean pressed) {
		ControlInput pi = this.getControlInputByKey(kc);
		if (pi == null) {
			return;
		}
		Controls controls = pi.getControlScheme();
		InputAction inp = pi.getInput();

		if (this.onInputChanged != null) {
			this.onInputChanged.handle(new InputEvent(controls, inp, pressed));
		}
	}

}
