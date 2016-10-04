package tunnelers.network;

import javafx.event.Event;
import javafx.event.EventType;
import tunnelers.network.command.NCG;

/**
 *
 * @author Stepan
 */
public class NetworkEvent extends Event{
	
	private static final EventType TYPE = new EventType("network");
	
	private final NCG.NetCommand command;
	
	public NetworkEvent(NCG.NetCommand command){
		super(TYPE);
		this.command = command;
	}

	public NCG.NetCommand getCommand() {
		return command;
	}
}
