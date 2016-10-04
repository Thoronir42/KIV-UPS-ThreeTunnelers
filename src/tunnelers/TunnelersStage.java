package tunnelers;

import tunnelers.Settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Stepan
 */
public final class TunnelersStage extends Stage {

	protected static final Settings SETTINGS = Settings.getInstance();

	public void update(long tick){
		
	}

	public TunnelersStage() {
		
	}

	protected void changeScene(ATunnelersScene scene) {
		this.setScene(scene);
		this.setTitle(String.format("%s %s %s", SETTINGS.getGameName(), SETTINGS.getTitleSeparator(), scene.getName()));
		
		scene.setOnKeyPressed((KeyEvent event) -> {
			scene.handleKeyPressed(event.getCode());
		});
	}

	public final void changeScene(Class reqScene) {
		ATunnelersScene scene = classToInstance(reqScene);
		if (scene == null) {
			return;
		}
		changeScene(scene);
	}
	
	protected void prevScene() {
		ATunnelersScene scene = (ATunnelersScene) this.getScene();
		this.changeScene(scene.getPrevScene());
	}

	protected ATunnelersScene classToInstance(Class scene) {
		if (scene == null) {
			return null;
		}
		try {
			return invokeInstance(scene);
		} catch (IllegalAccessException | NoSuchMethodException |
				IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.err.format("Couldn't get instance of new scene: %s=%s\n", e.getClass().getSimpleName(), e.getMessage());
		}
		return null;
	}

	protected ATunnelersScene invokeInstance(Class scene) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (ATunnelersScene) scene.getDeclaredMethod("getInstance").invoke(null);
	}

}
