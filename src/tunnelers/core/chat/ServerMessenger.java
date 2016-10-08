package tunnelers.core.chat;

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

	
	private ServerMessenger(){
		
	};
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getColor() {
		return IChatParticipant.SYSTEM_ID;
	}
}
