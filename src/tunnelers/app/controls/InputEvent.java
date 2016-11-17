package tunnelers.app.controls;

import javafx.event.Event;
import javafx.event.EventType;
import tunnelers.core.player.controls.InputAction;

/**
 *
 * @author Stepan
 */
public class InputEvent extends Event{
	private static final EventType<InputEvent> TYPE = new EventType<>("Input");
	
	private final int playerId;
	private final InputAction input;
	private final boolean pressed;
	
	
	public InputEvent(int playerId, InputAction input, boolean pressed) {
		super(TYPE);
		this.playerId = playerId;
		this.input = input;
		this.pressed = pressed;
	}

	public int getPlayerId() {
		return playerId;
	}

	public InputAction getInput() {
		return input;
	}

	public boolean isPressed() {
		return pressed;
	}
	
	
	
}
