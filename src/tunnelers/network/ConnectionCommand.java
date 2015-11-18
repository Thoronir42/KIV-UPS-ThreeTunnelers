package tunnelers.network;

/**
 *
 * @author Stepan
 */
public abstract class ConnectionCommand{
    public static final char ASPECT_LETTER = 'C';
	
	public static class Join extends NetCommand{
		public final static String CMD_TYPE = "UTHERE";
		public Join(int attemptNumber){
			super(ASPECT_LETTER, CMD_TYPE, new Object[]{attemptNumber});
		}
	}
	public static class Disconnect extends NetCommand{
		public final static String CMD_TYPE = "IMUSTGO";
		public Disconnect(){
			super(ASPECT_LETTER, CMD_TYPE);
		}
	}
	public static class Iam extends NetCommand{
		public final static String CMD_TYPE = "IAM";
		public Iam(String name){
			super(ASPECT_LETTER, CMD_TYPE, new Object[]{name});
		}
	}
	public static class WhoIs extends NetCommand{
		public final static String CMD_TYPE = "WHOS";
		public WhoIs(int id){
			super(ASPECT_LETTER, CMD_TYPE, new Object[]{id});
		}
	}
	
	public static abstract class Recievable{
		public static class WhoAreYou extends NetCommand{
			public final static String CMD_TYPE = "WHOU";
			public WhoAreYou(){
				super(ASPECT_LETTER, CMD_TYPE);
			}
		}
		public static class PlayerJoined extends NetCommand{
			public final static String CMD_TYPE = "OTHRHI";
			public PlayerJoined(int id){
				super(ASPECT_LETTER, CMD_TYPE, new Object[]{id});
			}
		}
		public static class PlayerDisconnected extends NetCommand{
			public final static String CMD_TYPE = "OTHRBAI";
			public PlayerDisconnected(int playerID, String reason){
				super(ASPECT_LETTER, CMD_TYPE, new Object[]{playerID, reason});
			}
		}
		public static class PlayerIs extends NetCommand{
			public final static String CMD_TYPE = "OTHRIS";
			public PlayerIs(int id, String name){
				super(ASPECT_LETTER, CMD_TYPE, new Object[]{id, name});
			}
		}
		public static class GameStarted extends NetCommand{
			public final static String CMD_TYPE = "ITSON";
			public GameStarted(){
				super(ASPECT_LETTER, CMD_TYPE);
			}
		}
		public static class ServerReady extends NetCommand{
			public final static String CMD_TYPE = "COME";
			public ServerReady(){
				super(ASPECT_LETTER, CMD_TYPE);
			}
		}
		public static class ServerFull extends NetCommand{
			public final static String CMD_TYPE = "DONZO";
			public ServerFull(){
				super(ASPECT_LETTER, CMD_TYPE);
			}
		}
	}
}
