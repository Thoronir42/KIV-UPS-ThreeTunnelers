package tunnelers.core.gameRoom;

/**
 *
 * @author Skoro
 */
public enum GameRoomState {
	Idle(0), Lobby(1), BattleStarting(2), Battle(3), Summarization(4);

	private final int intValue;

	private GameRoomState(int intValue) {
		this.intValue = intValue;
	}
	
	public int intValue(){
		return this.intValue;
	}
}
