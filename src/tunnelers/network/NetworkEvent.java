package tunnelers.network;

import javafx.event.Event;
import javafx.event.EventType;
import tunnelers.network.command.Command;

/**
 *
 * @author Stepan
 */
public class NetworkEvent extends Event{
	
	private static final EventType TYPE = new EventType("network");
	
	private final Command command;
	
	public NetworkEvent(Command command){
		super(TYPE);
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}
}
