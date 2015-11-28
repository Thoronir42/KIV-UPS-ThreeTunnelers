package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class LeadCommand {
	public static final short[] CMD_RANGE = {0, 9};
	public static boolean commandBelongs(short t){
		return t > CMD_RANGE[0] && t < CMD_RANGE[1];
	}
	
	public static class Approve extends NetCommand{
		public final static short CMD_NUM = 1;
		public Approve(byte message_id){
			super(message_id, CMD_NUM);
		}
		
		public Approve(byte message_id, int type){
			super(message_id, CMD_NUM, new Object[]{type});
		}
	}
	
	public static class Deny extends NetCommand{
		public final static short CMD_NUM = 2;
		public Deny(byte message_id){
			super(message_id, CMD_NUM);
		}
		
		public Deny(byte message_id, int type){
			super(message_id, CMD_NUM, new Object[]{type});
		}
	}
	
	public static class StillThere extends NetCommand{
		public final static short CMD_NUM = 3;
		
		public StillThere(int deaf_time){
			super(CMD_NUM, new Object[]{deaf_time});
		}
	}
	
	public static class WrongRoom extends NetCommand{
		public final static short CMD_NUM = 4;
		
		public WrongRoom(byte correctRoom){
			super(CMD_NUM, new Object[]{correctRoom});
		}
	}
}
