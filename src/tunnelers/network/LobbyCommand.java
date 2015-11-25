package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class LobbyCommand {
	public static final char ASPECT_LETTER = 'L';
	
	
	public static class Start extends NetCommand{
		public final static String CMD_TYPE = "LETSGO";
		public Start(){
			super(ASPECT_LETTER, CMD_TYPE);
		}
	}
	public static class Kick extends NetCommand{
		public final static String CMD_TYPE = "SEEYA";
		public Kick(int playerID, String reason){
			super(ASPECT_LETTER, CMD_TYPE, new Object[]{playerID, reason});
		}
	}
	public static class ChangeColor extends NetCommand{
		public final static String CMD_TYPE = "IMBLU";
		public ChangeColor(int colorID){
			super(ASPECT_LETTER, CMD_TYPE, new Object[]{colorID});
		}
	}
	
	public static abstract class Receivable{
		public static class GameStarted extends NetCommand{
			public final static String CMD_TYPE = "ITSON";
			public GameStarted(){
				super(ASPECT_LETTER, CMD_TYPE, null);
			}
		}
	}
}
