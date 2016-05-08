package tunnelers.Menu;

import tunnelers.Menu.ServerList.ServerListScene;
import tunnelers.ATunnelersStage;
import tunnelers.GameKickstarter;

/**
 *
 * @author Stepan
 */
public class MenuStage extends ATunnelersStage {

	private static MenuStage instance;

	private GameKickstarter kickstarter;

	private MenuStage() {
		super();
		this.changeScene(MainMenuScene.class);
	}

	public static MenuStage getInstance() {
		if (instance == null) {
			instance = new MenuStage();
		}
		return instance;
	}

	@Override
	public void update(long tick) {
		/*if (tick % Settings.TICK_RATE == 0) {
			System.out.printf("Update count: %d\n", tick / Settings.TICK_RATE);
		}*/
	}

	protected void prevScene() {
		AMenuScene scene = (AMenuScene) this.getScene();
		this.changeScene(scene.getPrevScene());
	}

	public GameKickstarter getKickstarter() {
		return this.kickstarter;
	}

	;
    
    public void kickstartLobby(GameKickstarter kickstarter) {
		try {
			this.returnCode = ATunnelersStage.CHANGE_TO_GAME;
			this.kickstarter = kickstarter;
			this.close();
		} catch (IllegalStateException e) {
			System.err.println(e.getMessage());
			this.changeScene(ServerListScene.class);
		}
	}
}
