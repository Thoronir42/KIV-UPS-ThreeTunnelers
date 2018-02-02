package tunnelers.app.views.components.roomListing;

import tunnelers.app.views.serverList.GameMode;
import tunnelers.core.gameRoom.IGameRoomInfo;

public interface IGameRoomListItem extends IGameRoomInfo {
	GameMode getGameModeView();

	// Tree Table view items
	String getTitle();

	String getOccupancy();

	String describeFlags();
}
