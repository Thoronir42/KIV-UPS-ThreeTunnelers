package tunnelers.core.engine;

import tunnelers.core.gameRoom.GameRoom;

public class EngineDebugManipulator {

	private final Engine e;

	public EngineDebugManipulator(Engine e) {
		this.e = e;
	}

	public void setGameRoom(GameRoom room) {
		this.e.currentGameRoom = room;
	}
}
