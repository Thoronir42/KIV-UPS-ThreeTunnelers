package tunnelers.app.controls;

import tunnelers.core.player.controls.ControlInput;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;

/**
 *
 * @author Stepan
 */
public class FxControlsManager extends AControlsManager{

	private EventHandler<InputEvent> onInputChanged;

	public byte[] getKeyboardLayoutIDs() {
		// TODO: optimize
		byte[] ids = new byte[this.keyboardSchemes.length];
		for (int i = 0; i < this.keyboardSchemes.length; i++) {
			ids[i] = this.keyboardSchemes[i].getID();
		}
		return ids;
	}
//
	private final FxKeyMap keyMap;

	public FxControlsManager() {
		super();
		this.keyMap = new FxKeyMap(this);
		for (byte i = 0; i < 2; i++) {
			this.keyboardSchemes[i] = new Controls(i);
			this.keyMap.setSchemeDefault(i);
		}
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
		Controls controlSchemeId = pi.getControlScheme();
		InputAction inp = pi.getInput();

		if (this.onInputChanged != null) {
			this.onInputChanged.handle(new InputEvent(controlSchemeId.getPlayerID(), inp, pressed));
		}
	}

}
