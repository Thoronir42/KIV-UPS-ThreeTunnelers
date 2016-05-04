package tunnelers.Menu.ServerList.GameRoomView;

import tunnelers.Menu.ServerList.RoomDifficulty;

/**
 *
 * @author Stepan
 */
public interface GRTVItem {
	RoomDifficulty getDIfficulty();

	// Tree Table view items
	public String getTitle();
	public String getOccupancy();

	public String getFlags();
}

class GRTVRoot implements GRTVItem{

	private final static String TITLE = "Seznam aktivních her";
	
	@Override
	public RoomDifficulty getDIfficulty() {
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
	public String getFlags(){
		return "";
	}
	
	@Override
	public String getOccupancy() {
		return "";
	}
	
	
	
}
