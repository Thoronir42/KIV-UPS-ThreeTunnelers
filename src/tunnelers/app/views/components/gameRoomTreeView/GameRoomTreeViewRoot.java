package tunnelers.app.views.components.gameRoomTreeView;

import tunnelers.app.views.components.roomListing.IGameRoomListItem;
import tunnelers.app.views.serverList.GameMode;

public class GameRoomTreeViewRoot implements IGameRoomListItem {
	private final static String TITLE = "Seznam aktivních her";

	@Override
	public GameMode getGameModeView() {
		return null;
	}

	@Override
	public String toString() {
		return TITLE;
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public String getOccupancy() {
		return "";
	}

	@Override
	public String describeFlags() {
		return "";
	}

	@Override
	public short getId() {
		return -1;
	}

	@Override
	public byte getCurrentPlayers() {
		return -1;
	}

	@Override
	public byte getMaxPlayers() {
		return -1;
	}

	@Override
	public byte getFlags() {
		return -1;
	}

	@Override
	public byte getGameMode() {
		return -1;
	}

	@Override
	public boolean isSpectacable() {
		return false;
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public boolean isFull() {
		return false;
	}
}
