package tunnelers.Menu;

import tunnelers.ATunnelersStage;
import tunnelers.Settings;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class MenuStage extends ATunnelersStage {

	private static MenuStage instance;

	private NetWorks returnNW;

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

	public NetWorks getReturnNetworks() {
		return this.returnNW;
	}

	;
    
    public void gotoLobby(NetWorks nw) {
		try {
			this.returnValue = ATunnelersStage.CHANGE_TO_GAME;
			this.returnNW = nw;
			this.close();
		} catch (IllegalStateException e) {
			System.err.println(e.getMessage());
			this.changeScene(ServerListScene.class);
		}
	}
}
