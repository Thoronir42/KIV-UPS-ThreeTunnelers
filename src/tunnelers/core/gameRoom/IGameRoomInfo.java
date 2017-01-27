package tunnelers.core.gameRoom;


public interface IGameRoomInfo {

	public static final byte FLAG_RUNNING = Byte.parseByte("00000001", 2),
			FLAG_SPECTATABLE = Byte.parseByte("00000010", 2);
	
	public static  final byte GAMEMODE_UNKNOWN = 0,
			GAMEMODE_FFA = 1;
	
	public short getId();
	
	public byte getCurrentPlayers();
	public byte getMaxPlayers();
	public byte getFlags();
	public byte getGameMode();
	
	public boolean isSpectacable();
	public boolean isRunning();
	public boolean isFull();
}
