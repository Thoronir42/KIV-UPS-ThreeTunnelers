package tunnelers.core.gameRoom;


public interface IGameRoomInfo {

	byte FLAG_RUNNING = Byte.parseByte("00000001", 2),
			FLAG_SPECTATABLE = Byte.parseByte("00000010", 2);

	// todo: separe gamemode
	byte GAMEMODE_UNKNOWN = 0,
			GAMEMODE_FFA = 1;

	short getId();

	byte getCurrentPlayers();

	byte getMaxPlayers();

	byte getFlags();

	byte getGameMode();

	boolean isSpectacable();

	boolean isRunning();

	boolean isFull();
}
