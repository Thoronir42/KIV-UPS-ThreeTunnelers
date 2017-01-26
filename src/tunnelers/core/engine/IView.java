package tunnelers.core.engine;

import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;

/**
 *
 * @author Stepan
 */
public interface IView {

	

	public static enum Scene{
		MainMenu, Settings, GameRoomList, Lobby, Warzone
	}
	
	public void showScene(Scene scene);
	
	public void alert(String message);
	
	public void updateChat();
	
	public AControlsManager getControlsManager();
	public PlayerColorManager getPlayerColorManager();
	
	public void appendGameRoomsToList(IGameRoomInfo[] rooms);
	
	public void setConnectEnabled(boolean value);
	
	public void setGameData(Map map, Player[] players);
	
	public void updateClients();
	public void updatePlayers();
	
	public void setLocalReadyState(boolean b);
	
	public void update(long currentTick);
	public void exit();
}
