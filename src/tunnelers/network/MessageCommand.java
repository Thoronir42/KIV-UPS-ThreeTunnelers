package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class MessageCommand{
    public static final char ASPECT_LETTER = 'M';
	
	public static class Plain extends NetCommand{
		public final static String CMD_TYPE = "PT";
		public Plain(int playerID, String text) {
			super(ASPECT_LETTER, CMD_TYPE, new Object[]{playerID, text});
		}
	}
	
	public static class RCON extends NetCommand{
		public final static String CMD_TYPE = "RC";
		public RCON(int playerID, String command) {
			super(ASPECT_LETTER, CMD_TYPE, new Object[]{playerID, command});
		}
	}
	
}
