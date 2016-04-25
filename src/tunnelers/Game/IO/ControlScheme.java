package tunnelers.Game.IO;

import javafx.scene.input.KeyCode;

/**
 *
 * @author Stepan
 */
public abstract class ControlScheme {

	public static class Keyboard extends AControlScheme {

		private final KeyMap keyMap;

		public Keyboard(KeyMap keyMap) {
			this.keyMap = keyMap;
		}

		@Override
		public ControlInput getInput(KeyCode kc) {
			ControlInput input = this.keyMap.getInput(kc);
			System.out.format("getInput %s - %s", kc.toString(), input.toString());
			return input;
		}

	}

	/**
	 * possible extension
	 */
	public static abstract class Joystick extends AControlScheme {
		
	}
}
