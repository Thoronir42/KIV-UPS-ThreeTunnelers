package tunnelers.core.gameRoom;

public class GameRoomFacade implements IGameRoomInfo {

	private final short id;
	private final byte maxPlayers;
	private final byte curPlayers;
	private final byte flags;
	private final byte difficulty;

	public GameRoomFacade(short id, byte maxPlayers, byte curPlayers, byte difficulty, byte flags) {
		this.id = id;
		this.maxPlayers = maxPlayers;
		this.curPlayers = curPlayers;
		this.flags = flags;
		this.difficulty = difficulty;
	}

	@Override
	public short getId() {
		return id;
	}

	@Override
	public byte getMaxPlayers() {
		return maxPlayers;
	}

	@Override
	public byte getCurrentPlayers() {
		return curPlayers;
	}

	@Override
	public byte getFlags() {
		return flags;
	}

	@Override
	public byte getDifficulty() {
		return difficulty;
	}

	@Override
	public boolean isFull() {
		return this.curPlayers >= this.maxPlayers;
	}

	@Override
	public boolean isSpectacable() {
		return (this.flags & FLAG_SPECTATABLE) > 0;
	}

	@Override
	public boolean isRunning() {
		return (this.flags & FLAG_SPECTATABLE) > 0;
	}

}
