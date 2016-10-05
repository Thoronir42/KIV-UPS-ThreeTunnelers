package tunnelers.Game.Chat;

import tunnelers.core.chat.IChatParticipant;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class ServerMessenger implements IChatParticipant{

	private static final ServerMessenger instance;
	
	static{
		instance = new ServerMessenger();
	}

	public static ServerMessenger getInstance(){
		return instance;
	}
	
	
	
	public static final String NAME = "SRV";
	public static final Color COLOR = Color.MAROON;

	
	private ServerMessenger(){
		
	};
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Color getColor() {
		return COLOR;
	}

	

}
