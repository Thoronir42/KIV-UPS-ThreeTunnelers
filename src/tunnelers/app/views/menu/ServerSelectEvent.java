package tunnelers.app.views.menu;

import javafx.event.Event;
import javafx.event.EventType;

public class ServerSelectEvent extends Event {
	private static final EventType<Event> EVENT_TYPE = new EventType<>("Server selected");


	private final String hostname;
	private final int port;
	private final String username;
	private final boolean useReconnect;

	public ServerSelectEvent(String hostname, int port, String username, boolean useReconnect) {
		super(EVENT_TYPE);

		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.useReconnect = useReconnect;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public boolean useReconnect() {
		return this.useReconnect;
	}

}
