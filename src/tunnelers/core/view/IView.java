package tunnelers.core.view;

import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public interface IView {
	public static enum Scene{
		MainMenu, Settings, ServerList, Lobby, Game
	}
	
	public void showScene(Scene scene);
	
	public void alert(String message);
	
	public void updateChat();
	
	public IColorScheme getColorScheme();
	
	public void prepareGame(Map map, Player[] players);
}
