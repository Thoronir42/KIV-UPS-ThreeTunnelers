package tunnelers.core.player.controls;


public abstract class AControlsManager {

	public static InputAction[] getEditableInputs() {
		return new InputAction[]{
				InputAction.movUp, InputAction.movDown,
				InputAction.movLeft, InputAction.movRight,
				InputAction.actShoot,};
	}

	public abstract Controls[] getAllSchemes();

	public abstract Controls getScheme(byte id);

}
