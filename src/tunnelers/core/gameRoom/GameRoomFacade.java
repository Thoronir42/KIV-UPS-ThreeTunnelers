package tunnelers.core.gameRoom;

public class GameRoomFacade implements IGameRoomInfo {

	private final short id;
	private final byte maxPlayers;
	private final byte curPlayers;
	private final byte flags;
	private final byte gameMode;

	public GameRoomFacade(short id, byte maxPlayers, byte curPlayers, byte gameMode, byte flags) {
		this.id = id;
		this.maxPlayers = maxPlayers;
		this.curPlayers = curPlayers;
		this.flags = flags;
		this.gameMode = gameMode;
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
	public byte getGameMode() {
		return gameMode;
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

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + this.id;
		hash = 53 * hash + this.maxPlayers;
		hash = 53 * hash + this.curPlayers;
		hash = 53 * hash + this.flags;
		hash = 53 * hash + this.gameMode;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GameRoomFacade other = (GameRoomFacade) obj;
		return this.id == other.id &&
				this.maxPlayers == other.maxPlayers &&
				this.curPlayers == other.curPlayers &&
				this.flags == other.flags &&
				this.gameMode == other.gameMode;
	}
}
