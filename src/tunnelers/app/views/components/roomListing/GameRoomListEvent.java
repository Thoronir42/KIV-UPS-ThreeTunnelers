package tunnelers.app.views.components.roomListing;

import javafx.event.Event;
import javafx.event.EventType;
import tunnelers.core.gameRoom.IGameRoomInfo;

public class GameRoomListEvent extends Event {
	private static final EventType<Event> EVENT_TYPE = new EventType<>("GameRoomSelected");

	private final IGameRoomInfo gameRoom;

	public GameRoomListEvent(IGameRoomInfo gameRoom) {
		super(EVENT_TYPE);

		this.gameRoom = gameRoom;
	}

	public IGameRoomInfo getGameRoom() {
		return gameRoom;
	}
}
