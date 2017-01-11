package tunnelers.core.gameRoom;

/**
 *
 * @author Skoro
 */
public enum GameRoomState {
	Idle(0), Lobby(1), Battle(2), Summarization(3);

	private final int intValue;

	private GameRoomState(int intValue) {
		this.intValue = intValue;
	}
	
	public int intValue(){
		return this.intValue;
	}
}
