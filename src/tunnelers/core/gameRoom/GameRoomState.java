package tunnelers.core.gameRoom;

/**
 *
 * @author Skoro
 */
public enum GameRoomState {
	Idle(0), Lobby(1), BattleStarting(2), Battle(3), Summarization(4);

	private final byte byteValue;

	private GameRoomState(int byteValue) {
		this((byte) byteValue);
	}

	private GameRoomState(byte byteValue) {
		this.byteValue = byteValue;
	}

	public int byteValue() {
		return this.byteValue;
	}

	public static GameRoomState getByValue(byte value) {
		switch (value) {
			case 0:
				return Idle;
			case 1:
				return Lobby;
			case 2:
				return BattleStarting;
			case 3:
				return Battle;
			case 4:
				return Summarization;
		}
		return null;
	}
}
