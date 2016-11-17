package tunnelers.core.player.controls;

import java.util.Arrays;

/**
 *
 * @author Stepan
 */
public class AControlsManager {

	public static InputAction[] getEditableInputs() {
		return new InputAction[]{
			InputAction.movUp, InputAction.movDown,
			InputAction.movLeft, InputAction.movRight,
			InputAction.actShoot,};
	}

	protected final Controls[] keyboardSchemes;
	
	public AControlsManager(){
		this.keyboardSchemes = new Controls[2];
	}

	public Controls[] getAllSchemes() {
		return Arrays.copyOf(this.keyboardSchemes, this.keyboardSchemes.length);
	}

	public Controls getKeyboardScheme(byte sIndex) {
		return this.keyboardSchemes[sIndex];
	}

}
