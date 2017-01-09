package tunnelers.app.views.serverList.GameRoomView;

import tunnelers.app.views.serverList.GameRoomDifficulty;
import tunnelers.core.gameRoom.IGameRoomInfo;

/**
 *
 * @author Stepan
 */
public interface IGameRoomTreeViewItem extends IGameRoomInfo{
	GameRoomDifficulty getDifficultyView();

	// Tree Table view items
	public String getTitle();
	public String getOccupancy();

	public String describeFlags();
	
	public boolean isGameRoom();
}
