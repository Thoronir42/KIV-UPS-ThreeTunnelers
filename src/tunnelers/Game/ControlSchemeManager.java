package tunnelers.Game;

import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.Game.IO.ControlScheme;
import tunnelers.Game.IO.InputAction;
import tunnelers.Game.IO.KeyMap;
import tunnelers.Game.IO.ControlInput;

/**
 *
 * @author Stepan
 */
public class ControlSchemeManager {

	public static final byte KEYBOARD_PRIMARY = 0, KEYBOARD_SECONDARY = 1;
	protected static final byte[] KEYBOARD_LAYOUTS = {KEYBOARD_PRIMARY};

	public static byte[] getKeyboardLayoutIDs() {
		return new byte[]{KEYBOARD_PRIMARY, KEYBOARD_SECONDARY};
	}

	public static InputAction[] getEditableInputs() {
		return new InputAction[]{
			InputAction.movUp, InputAction.movDown,
			InputAction.movLeft, InputAction.movRight,
			InputAction.actShoot,
		};
	}
//
	private final KeyMap keyMap;

	private final ControlScheme.Keyboard[] keyboardSchemes;

	public ControlSchemeManager() {
		this.keyMap = new KeyMap(this);
		byte[] keyboardLayoutIDs = ControlSchemeManager.getKeyboardLayoutIDs();
		
		this.keyboardSchemes = new ControlScheme.Keyboard[keyboardLayoutIDs.length];
		for(int i = 0; i < keyboardLayoutIDs.length; i++){
			this.keyboardSchemes[i] = new ControlScheme.Keyboard(this.keyMap);
			this.keyMap.setSchemeDefault(keyboardLayoutIDs[i]);
		}
	}
	
	public AControlScheme[] getAllSchemes(){
		AControlScheme[] schemes = new AControlScheme[this.keyboardSchemes.length];
		for(int i = 0; i < schemes.length; i++){
			schemes[i] = this.keyboardSchemes[i];
		}
		
		return schemes;
	}
	
	public ControlScheme.Keyboard getKeyboardScheme(byte sIndex){
		return this.keyboardSchemes[sIndex];
	}

	ControlInput getPlayerInputByKeyPress(KeyCode code) {
		ControlInput pi = this.keyMap.getInput(code);
		return pi;
	}

	public KeyCode getKeyCode(ControlInput plrInput) {
		return this.keyMap.findKey(plrInput);
	}
	
	public ControlInput replaceKeyInput(KeyCode kc, ControlInput plrInput){
		//System.out.format("Putting %s to %s.", plrInput, kc);
		ControlInput oldOccurence = this.keyMap.set(kc, plrInput);
		//System.out.format(" Previously there was %s.\n", oldOccurence);
		return oldOccurence;
	}
	
			
}
