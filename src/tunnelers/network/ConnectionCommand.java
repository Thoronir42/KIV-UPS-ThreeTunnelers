package tunnelers.network;

/**
 *
 * @author Stepan
 */
public abstract class ConnectionCommand{

    protected static final char AREA_LETTER = 'L';
    protected char getAreaLetter(){
        return AREA_LETTER;
    }
	
	public static class Join extends NetCommand{
		public final static String CMD_TYPE = "UTHERE";
		public Join(int attemptNumber){
			super(AREA_LETTER, CMD_TYPE, new Object[]{attemptNumber});
		}
	}
	public static class Disconnect extends NetCommand{
		public final static String CMD_TYPE = "IMUSTGO";
		public Disconnect(){
			super(AREA_LETTER, CMD_TYPE, new Object[]{});
		}
	}
	public static class Kick extends NetCommand{
		public final static String CMD_TYPE = "SEEYA";
		public Kick(int playerID, String reason){
			super(AREA_LETTER, CMD_TYPE, new Object[]{playerID, reason});
		}
	}
	public static class ChangeColor extends NetCommand{
		public final static String CMD_TYPE = "IMBLU";
		public ChangeColor(int colorID){
			super(AREA_LETTER, CMD_TYPE, new Object[]{colorID});
		}
	}
	public static class Iam extends NetCommand{
		public final static String CMD_TYPE = "IAM";
		public Iam(String name){
			super(AREA_LETTER, CMD_TYPE, new Object[]{name});
		}
	}
	public static class WhoIs extends NetCommand{
		public final static String CMD_TYPE = "WHOS";
		public WhoIs(int id){
			super(AREA_LETTER, CMD_TYPE, new Object[]{id});
		}
	}
	public static class Start extends NetCommand{
		public final static String CMD_TYPE = "LETSGO";
		public Start(){
			super(AREA_LETTER, CMD_TYPE, new Object[]{});
		}
	}
	
	public static abstract class Recievable{
		public static class WhoAreYou extends NetCommand{
			public final static String CMD_TYPE = "WHOU";
			public WhoAreYou(){
				super(AREA_LETTER, CMD_TYPE, new Object[]{});
			}
		}
		public static class PlayerJoined extends NetCommand{
			public final static String CMD_TYPE = "OTHRHI";
			public PlayerJoined(int id, String name){
				super(AREA_LETTER, CMD_TYPE, new Object[]{id, name});
			}
		}
		public static class PlayerDisconnected extends NetCommand{
			public final static String CMD_TYPE = "OTHRBAI";
			public PlayerDisconnected(int playerID, String reason){
				super(AREA_LETTER, CMD_TYPE, new Object[]{playerID, reason});
			}
		}
		public static class PlayerIs extends NetCommand{
			public final static String CMD_TYPE = "OTHRIS";
			public PlayerIs(int id, String name){
				super(AREA_LETTER, CMD_TYPE, new Object[]{id, name});
			}
		}
		public static class GameStarted extends NetCommand{
			public final static String CMD_TYPE = "ITSON";
			public GameStarted(){
				super(AREA_LETTER, CMD_TYPE, new Object[]{});
			}
		}
	}
}
