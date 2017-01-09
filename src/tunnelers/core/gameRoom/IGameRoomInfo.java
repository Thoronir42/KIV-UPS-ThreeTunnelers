package tunnelers.core.gameRoom;


public interface IGameRoomInfo {

	public static final byte FLAG_RUNNING = Byte.parseByte("00000001", 2),
			FLAG_SPECTATABLE = Byte.parseByte("00000010", 2);
	
	public static  final byte DIFFICULTY_UNKNOWN = 0,
			DIFFICULTY_EASY = 1,
			DIFFICULTY_MEDIUM = 2,
			DIFFICULTY_HARD = 3;
	
	public short getId();
	
	public byte getCurrentPlayers();
	public byte getMaxPlayers();
	public byte getFlags();
	public byte getDifficulty();
	
	public boolean isSpectacable();
	public boolean isRunning();
	public boolean isFull();
}
