package tunnelers.app;

import tunnelers.Settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;
import tunnelers.Game.Chat.Chat;
import tunnelers.Game.PlayScene;
import tunnelers.model.Engine;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public final class TunnelersStage extends Stage {

	protected static final Settings SETTINGS = Settings.getInstance();
	
	protected ATunnelersScene currentScene;

	protected final NetWorks networks;
	protected Chat chat;
	protected Engine engine;
	

	public final void update(long tick) {
		if (tick % 4 == 0 && currentScene instanceof PlayScene) {
			this.engine.update(tick);
			currentScene.drawScene();
		}
	}

	public TunnelersStage() {
		super();
		
		this.networks = new NetWorks("Karel"); // TODO: spojit s nestavenym jmenem
		
		this.setOnHidden((event) -> {
			this.networks.close();
		});
	}
	
	public void prevScene() {
		ATunnelersScene scene = (ATunnelersScene) this.getScene();
		this.changeScene(scene.getPrevScene());
	}

	protected void changeScene(ATunnelersScene scene) {
		currentScene = scene;
		this.setScene(scene);
		this.setTitle(String.format("%s %s %s", SETTINGS.getGameName(), SETTINGS.getTitleSeparator(), scene.getName()));
	}

	public final void changeScene(Class reqScene) {
		ATunnelersScene scene = classToInstance(reqScene);
		if (scene == null) {
			return;
		}
		changeScene(scene);
	}

	protected ATunnelersScene classToInstance(Class scene) {
		if (scene == null) {
			return null;
		}
		if (!ATunnelersScene.class.isAssignableFrom(scene)) {
			System.out.format("%s not assignable from %s\n", scene.getSimpleName(), ATunnelersScene.class.getSimpleName());
			return null;
		}
		try {
			return (ATunnelersScene) scene.getDeclaredMethod("getInstance").invoke(null);
		} catch (IllegalAccessException | NoSuchMethodException |
				IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.err.format("Couldn't get instance of new scene: %s=%s\n", e.getClass().getSimpleName(), e.getMessage());
		}
		return null;
	}

}
