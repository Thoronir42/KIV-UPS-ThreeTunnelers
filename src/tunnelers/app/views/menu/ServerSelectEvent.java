package tunnelers.app.views.menu;

import javafx.event.Event;
import javafx.event.EventType;

public class ServerSelectEvent extends Event{
	private static final EventType EVENT_TYPE = new EventType("Server selected");
	
	
	private final String hostname;
	private final int port;
	private final String username;
	
	public ServerSelectEvent(String hostname, int port, String username) {
		super(EVENT_TYPE);
		
		this.hostname = hostname;
		this.port = port;
		this.username = username;
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
	
}
