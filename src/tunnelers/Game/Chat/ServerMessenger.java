package tunnelers.Game.Chat;

import tunnelers.core.chat.IChatParticipant;

/**
 *
 * @author Stepan
 */
public class ServerMessenger implements IChatParticipant{

	private static final ServerMessenger INSTANCE;
	
	static{
		INSTANCE = new ServerMessenger();
	}

	public static ServerMessenger getInstance(){
		return INSTANCE;
	}
	
	
	
	public static final String NAME = "SRV";
	public static final String COLOR = "800000";

	
	private ServerMessenger(){
		
	};
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getHexColor() {
		return COLOR;
	}

	

}
