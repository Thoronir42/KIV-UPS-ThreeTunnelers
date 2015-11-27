package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class MessageCommand{
	
	public static class Plain extends NetCommand{
		public final static short CMD_NUM = 100;
		public Plain(int playerID, String text) {
			super(CMD_NUM, new Object[]{playerID, text});
		}
		
		public int getPlayerId(){
			return 0;
		}
		public String getMessageText(){
			return (String)this.params[1];
		}
	}
	
	public static class RCON extends NetCommand{
		public final static short CMD_NUM = 105;
		public RCON(int playerID, String command) {
			super(CMD_NUM, new Object[]{playerID, command});
		}
	}
	
}
