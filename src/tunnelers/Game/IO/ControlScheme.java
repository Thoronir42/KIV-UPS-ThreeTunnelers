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
		public PlrInput getInput(KeyCode kc) {
			return this.keyMap.getInput(kc);
		}

	}
}
