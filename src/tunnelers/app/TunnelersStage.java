package tunnelers.app;

import tunnelers.Settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import tunnelers.Game.Chat.Chat;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.app.render.FxRenderer;
import tunnelers.core.engine.Engine;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class TunnelersStage extends Stage {

	protected static final Settings SETTINGS = Settings.getInstance();
	
	protected final ControlSchemeManager controlSchemeManager;
	protected final FxRenderer renderer;

	protected ATunnelersScene currentScene;

	protected Chat chat;
	protected Engine engine;
	

	public final void update(long tick) {
		this.renderer.render();
	}

	public TunnelersStage(FxRenderer renderer) {
		super();
		
		this.renderer = renderer;
		this.controlSchemeManager = SETTINGS.getControlSchemeManager();
	}
	
	public void prevScene() {
		ATunnelersScene scene = (ATunnelersScene) this.getScene();
		this.changeScene(scene.getPrevScene());
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

	protected ATunnelersScene classToInstance(Class scene) {
		if (scene == null) {
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
