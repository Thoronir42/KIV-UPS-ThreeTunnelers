package tunnelers.app.views.components.chat;

import javafx.event.Event;
import javafx.event.EventType;

public class ChatEvent extends Event {

	private static final EventType<Event> TYPE_NEW_MESSAGE = new EventType<>("ChatEvent");

	private final String message;

	ChatEvent(String message) {
		super(TYPE_NEW_MESSAGE);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
