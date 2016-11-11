package tunnelers.app.views;

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
}
