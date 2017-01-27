package tunnelers.app.views.components.roomListing;

import tunnelers.app.views.serverList.GameMode;
import tunnelers.core.gameRoom.IGameRoomInfo;

/**
 *
 * @author Stepan
 */
public interface IGameRoomListItem extends IGameRoomInfo{
	GameMode getGameModeView();

	// Tree Table view items
	public String getTitle();
	public String getOccupancy();

	public String describeFlags();
}
