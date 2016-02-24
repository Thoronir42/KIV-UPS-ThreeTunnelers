package tunnelers.Game;

import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.Game.IO.ControlScheme;
import tunnelers.Game.IO.Input;
import tunnelers.Game.IO.KeyMap;
import tunnelers.Game.IO.PlrInput;

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
	
	public ControlScheme.Keyboard getKeyboardScheme(byte sIndex){
		return this.keyboardSchemes[sIndex];
	}

	public byte getPlayerIdFromScheme(byte controlSchemeId) {
		return 1;
	}

	PlrInput getPlayerInputByKeyPress(KeyCode code) {
		PlrInput pi = this.keyMap.getInput(code);
		return pi;
	}

	public KeyCode getKeyCode(PlrInput plrInput) {
		return this.keyMap.getKey(plrInput);
	}
	
	public PlrInput replaceKeyInput(KeyCode kc, PlrInput plrInput){
		return this.keyMap.set(kc, plrInput);
	}
	
			
}
