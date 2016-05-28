package tunnelers.Menu.ServerList;

import tunnelers.Menu.ServerList.GameRoomView.GRTVItem;

/**
 *
 * @author Stepan
 */
public enum RoomDifficulty implements GRTVItem{
	Easy("Lehká", 1),
	Medium("Střední", 2),
	Hard("Těžká", 3),
	Unspecified("Nespecifikována", 0);

	private final String label;
	private final int value;

	private RoomDifficulty(String label, int value){
		this.label = label;
		this.value = value;	
	}

	@Override
	public String toString(){
		return "Obtížnost " + label;
	}

	public int intValue(){
		return value;
	}

	@Override
	public RoomDifficulty getDIfficulty() {
		return this;
	}
	
	@Override
	public String getTitle() {
		return this.toString();
	}

	@Override
	public String getOccupancy() {
		return "";
	}

	@Override
	public String getFlags() {
		return "";
	}
	
	
	
	

}
