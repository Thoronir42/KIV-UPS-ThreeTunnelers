package tunnelers.Menu.Settings;

import javafx.scene.input.KeyCode;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.IO.InputAction;
import tunnelers.Game.IO.ControlInput;

/**
 *
 * @author Stepan
 */
class KeyTableRow{
	static int inputColumnOffset = 0;
	
	InputAction inputAction;
	final ControlInput[] inputs;
	ControlSchemeManager controlSchemeManager;
	
	public KeyTableRow(InputAction input, byte[] keyboardLayoutIds, ControlSchemeManager csm){
		this.inputAction = input;
		this.controlSchemeManager = csm;
		
		this.inputs = new ControlInput[keyboardLayoutIds.length];
		for (byte i = 0; i < keyboardLayoutIds.length; i++) {
			inputs[i] = new ControlInput(controlSchemeManager.getKeyboardScheme(i), input);
		}
		
	}
	
	public ControlInput getControlInput(int i){
		return this.inputs[i - inputColumnOffset];
	}

	KeyCode getKeyCode(int i) {
		return this.controlSchemeManager.getKeyCode(this.inputs[i - inputColumnOffset]);
	}
	
}
