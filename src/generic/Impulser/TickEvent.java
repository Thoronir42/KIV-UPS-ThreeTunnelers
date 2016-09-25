package generic.Impulser;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author Stepan
 */
public class TickEvent extends Event{
	
	private final static EventType TYPE = new EventType("tick");
	
	private final long tick;
	
	public TickEvent(long tick) {
		super(TYPE);
		
		this.tick = tick;
	}
	
	public long getTick(){
		return this.tick;
	}
}