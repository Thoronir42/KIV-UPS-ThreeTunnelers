package tunnelers.app.views.components.gameRoomTreeView;

import tunnelers.app.views.serverList.GameMode;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.app.views.components.roomListing.IGameRoomListItem;

/**
 *
 * @author Stepan
 */
public class GameRoomViewWrapper implements IGameRoomListItem {

	private final Type type;

	private short roomID;
	private byte curPlayers;
	private byte maxPlayers;
	private byte flags;
	private GameMode gameMode;

	public GameRoomViewWrapper(IGameRoomInfo gri) {
		this.roomID = gri.getId();
		this.curPlayers = gri.getCurrentPlayers();
		this.maxPlayers = gri.getMaxPlayers();
		this.flags = gri.getFlags();
		this.setGameMode(gri.getGameMode());

		this.type = Type.GameRoom;
	}

	public GameRoomViewWrapper(GameMode difficulty) {
		this.setGameMode(difficulty);
		this.type = Type.GameMode;
	}

	@Override
	public String toString() {
		switch (this.type) {
			case GameMode:
				return this.gameMode.toString();
			case GameRoom:
				return String.format("GameRoom#%d {PlrMax=%d, PlrCur=%d, flags=%d}", roomID, maxPlayers, curPlayers, flags);
			default:
				return "???";
		}
	}

	private void setGameMode(GameMode difficulty) {
		if(difficulty == null){
			throw new IllegalArgumentException();
		}
		this.gameMode = difficulty;
	}

	private void setGameMode(byte difficulty) {
		switch (difficulty) {
			case GAMEMODE_FFA:
				this.setGameMode(GameMode.FFA);
				break;
			default:
				this.setGameMode(GameMode.Unspecified);
				break;
		}
	}

	@Override
	public GameMode getGameModeView() {
		return this.gameMode;
	}

	@Override
	public String getOccupancy() {
		switch (this.type) {
			case GameRoom:
				return String.format("%d / %d", this.curPlayers, this.maxPlayers);
			default:
				return "";
		}

	}

	@Override
	public String getTitle() {
		switch (this.type) {
			case GameRoom:
				return "Místnost #" + this.roomID;
			case GameMode:
				return this.gameMode.toString();
			default:
				return "???";
		}
	}

	@Override
	public String describeFlags() {
		if (this.type != Type.GameRoom) {
			return "";
		}

		boolean first = true;
		String result = "";
		if (this.isFull()) {
			result += "Plná";
			first = false;
		}
		if (this.isRunning()) {
			if (!first) {
				result += ", ";
			}
			result += "Právě běží";
			first = false;
		}
		if (this.isSpectacable()) {
			if (!first) {
				result += ", ";
			}
			result += "Je pozorovatelná";
		}

		return result;
	}

	@Override
	public short getId() {
		return this.roomID;
	}

	@Override
	public byte getFlags() {
		return this.flags;
	}

	@Override
	public byte getCurrentPlayers() {
		return this.curPlayers;
	}

	@Override
	public byte getMaxPlayers() {
		return this.maxPlayers;
	}

	@Override
	public byte getGameMode() {
		return (byte) this.gameMode.intValue();
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
		return (this.flags & FLAG_RUNNING) > 0;
	}
	
	public boolean isGame(){
		return this.type == Type.GameRoom;
	}

	private enum Type {
		GameRoom, GameMode
	};
	
	
}
