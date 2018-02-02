package tunnelers.app.controls;

import javafx.event.Event;
import javafx.event.EventType;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;

public class InputEvent extends Event {
	private static final EventType<InputEvent> TYPE = new EventType<>("Input");

	private final Controls controls;
	private final InputAction input;
	private final boolean pressed;


	InputEvent(Controls controls, InputAction input, boolean pressed) {
		super(TYPE);
		this.controls = controls;
		this.input = input;
		this.pressed = pressed;
	}

	public int getControlsId() {
		return this.controls.getID();
	}

	public InputAction getInput() {
		return input;
	}

	public boolean isPressed() {
		return pressed;
	}
}
