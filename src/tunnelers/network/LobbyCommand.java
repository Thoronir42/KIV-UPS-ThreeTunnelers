package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class LobbyCommand {
	public static final short[] CMD_RANGE = {110, 199};
	public static boolean commandBelongs(short t){
		return t > CMD_RANGE[0] && t < CMD_RANGE[1];
	}
	
	public static class Iam extends NetCommand{
		public final static short CMD_NUM = 112;
		public Iam(String name){
			super(CMD_NUM, new Object[]{name});
		}
	}
	
	public static class ChangeColor extends NetCommand{
		public final static short CMD_NUM = 113;
		public ChangeColor(int colorID){
			super(CMD_NUM, new Object[]{colorID});
		}
	}
	
	public static class YouLead extends NetCommand{
		public static final short CMD_NUM = 120;
		public YouLead(int playerId){
			super(CMD_NUM, new Object[]{playerId});
		}
	}
			
	public static class Start extends NetCommand{
		public final static short CMD_NUM = 130;
		public Start(){
			super(CMD_NUM);
		}
	}
	
	public static class IsThisOk extends NetCommand{
		public static final short CMD_NUM = 132;
		public IsThisOk(int xblocks, int yblocks){
			super(CMD_NUM, new Object[]{xblocks, yblocks});
		}
	}
	
	public static class Kick extends NetCommand{
		public final static short CMD_NUM = 149;
		public Kick(int playerID, String reason){
			super(CMD_NUM, new Object[]{playerID, reason});
		}
	}
	
	//			###		Server commands		###
	
	public static class PlayerJoined extends NetCommand{
		public final static short CMD_NUM = 150;
		public PlayerJoined(int id){
			super(CMD_NUM, new Object[]{id});
		}
	}
	public static class PlayerReturned extends NetCommand{
		public final static short CMD_NUM = 151;
		public PlayerReturned(int id){
			super(CMD_NUM, new Object[]{id});
		}
	}
	public static class PlayerIs extends NetCommand{
		public final static short CMD_NUM = 152;
		public PlayerIs(int id, String name){
			super(CMD_NUM, new Object[]{id, name});
		}
	}
	public static class PlayerRecolored extends NetCommand{
		public final static short CMD_NUM = 153;
		public PlayerRecolored(int playerId, int colorId){
			super(CMD_NUM, new Object[]{playerId, colorId});
		}
	}
	public static class NewLeader extends NetCommand{
		public final static short CMD_NUM = 160;
		public NewLeader(int playerId){
			super(CMD_NUM, new Object[]{playerId});
		}
	}
	
	public static class GameStarts extends NetCommand{
		public final static short CMD_NUM = 170;
		public GameStarts(int chunkSize, int xChunks, int yChunks){
			super(CMD_NUM, new Object[]{chunkSize, xChunks, yChunks});
		}
	}
	
	public static class GameStarted extends NetCommand{
		public final static short CMD_NUM = 171;
		public GameStarted(){
			super(CMD_NUM);
		}
	}
	public static class PlayerDisconnected extends NetCommand{
		public final static short CMD_NUM = 199;
		public PlayerDisconnected(int playerID, String reason){
			super(CMD_NUM, new Object[]{playerID, reason});
		}
	}
}
