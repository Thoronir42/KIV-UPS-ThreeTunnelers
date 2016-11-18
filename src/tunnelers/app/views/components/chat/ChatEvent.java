package tunnelers.app.views.components.chat;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author Stepan
 */
public class ChatEvent extends Event {

	private static final EventType TYPE_NEW_MESSAGE = new EventType("ChatEvent");

	private final String message;

	public ChatEvent(String message) {
		super(TYPE_NEW_MESSAGE);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
