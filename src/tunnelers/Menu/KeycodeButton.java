package tunnelers.Menu;

import javafx.scene.control.Button;
import tunnelers.Game.IO.Input;

/**
 *
 * @author Stepan
 */
public class KeycodeButton extends Button {
	Input input;
	byte playerId;

	public KeycodeButton(Input input, byte playerId, String text) {
		super(text);
		this.input = input;
		this.playerId = playerId;
	}

	public Input getInput() {
		return input;
	}

	public byte getPlayerId() {
		return playerId;
	}
	
	
			
}
