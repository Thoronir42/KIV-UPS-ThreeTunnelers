package tunnelers.Menu;

import javafx.scene.control.Button;
import tunnelers.Game.IO.InputAction;

/**
 *
 * @author Stepan
 */
public class KeycodeButton extends Button {
	InputAction input;
	byte playerId;

	public KeycodeButton(InputAction input, byte playerId, String text) {
		super(text);
		this.input = input;
		this.playerId = playerId;
	}

	public InputAction getInput() {
		return input;
	}

	public byte getPlayerId() {
		return playerId;
	}
	
	
			
}
