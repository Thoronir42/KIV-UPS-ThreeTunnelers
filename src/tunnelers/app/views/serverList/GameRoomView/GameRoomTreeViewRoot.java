package tunnelers.app.views.serverList.GameRoomView;

import tunnelers.app.views.serverList.GameRoomDifficulty;

/**
 *
 * @author Skoro
 */
public class GameRoomTreeViewRoot implements IGameRoomTreeViewItem{
	private final static String TITLE = "Seznam aktivních her";
	
	@Override
	public GameRoomDifficulty getDifficultyView() {
		return null;
	}
	
	@Override
	public String toString(){
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
	public byte getDifficulty() {
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
	
	@Override
	public boolean isGameRoom(){
		return false;
	}
}
