package tunnelers.app.views.serverList.GameRoomView;

import tunnelers.app.views.serverList.GameRoomDifficulty;
import tunnelers.core.gameRoom.IGameRoomInfo;

/**
 *
 * @author Stepan
 */
public class GameRoomViewWrapper implements IGameRoomTreeViewItem {

	private Type type;

	private short roomID;
	private byte curPlayers;
	private byte maxPlayers;
	private byte flags;
	private GameRoomDifficulty difficulty;

	public GameRoomViewWrapper(IGameRoomInfo gri) {
		this.roomID = gri.getId();
		this.curPlayers = gri.getCurrentPlayers();
		this.maxPlayers = gri.getMaxPlayers();
		this.flags = gri.getFlags();
		this.setDifficulty(gri.getDifficulty());

		this.type = Type.GameRoom;
	}

	public GameRoomViewWrapper(GameRoomDifficulty difficulty) {
		this.setDifficulty(difficulty);
		this.type = Type.Difficulty;
	}

	@Override
	public String toString() {
		switch (this.type) {
			case Difficulty:
				return this.difficulty.toString();
			case GameRoom:
				return String.format("GameRoom#%d {PlrMax=%d, PlrCur=%d, flags=%d}", roomID, maxPlayers, curPlayers, flags);
			default:
				return "???";
		}
	}

	private void setDifficulty(GameRoomDifficulty difficulty) {
		if(difficulty == null){
			throw new IllegalArgumentException();
		}
		this.difficulty = difficulty;
	}

	private void setDifficulty(byte difficulty) {
		switch (difficulty) {
			case DIFFICULTY_EASY:
				this.setDifficulty(GameRoomDifficulty.Easy);
				break;
			case DIFFICULTY_MEDIUM:
				this.setDifficulty(GameRoomDifficulty.Medium);
				break;
			case DIFFICULTY_HARD:
				this.setDifficulty(GameRoomDifficulty.Hard);
				break;
			default:
				this.setDifficulty(GameRoomDifficulty.Unspecified);
				break;
		}
	}

	@Override
	public GameRoomDifficulty getDifficultyView() {
		return this.difficulty;
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
			case Difficulty:
				return this.difficulty.toString();
			default:
				return "???";
		}
	}

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
	public byte getDifficulty() {
		return (byte) this.difficulty.intValue();
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
	
	public boolean isGameRoom(){
		return this.type == Type.GameRoom;
	}

	private enum Type {
		GameRoom, Difficulty
	};
	
	
}
