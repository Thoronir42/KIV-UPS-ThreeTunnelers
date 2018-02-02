package tunnelers.core.engine;

import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;

public interface IView {

	enum Scene {
		MainMenu, Settings, GameRoomList, Lobby, Warzone
	}

	void showScene(Scene scene);

	void alert(String message);

	void updateChat();

	AControlsManager getControlsManager();

	PlayerColorManager getPlayerColorManager();

	void appendGameRoomsToList(IGameRoomInfo[] rooms);

	void setConnectEnabled(boolean value);

	void setGameData(Map map, Player[] players);

	void updateClients();

	void updatePlayers();

	void setLocalReadyState(boolean b);

	void update(long currentTick);

	void exit();
}
