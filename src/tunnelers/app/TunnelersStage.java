package tunnelers.app;

import tunnelers.Settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;
import tunnelers.Game.Chat.Chat;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.PlayScene;
import tunnelers.app.render.FxRenderer;
import tunnelers.core.engine.Engine;

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
	protected final Engine engine;
	

	public final void update(long tick) {
		this.renderer.render();
	}

	public TunnelersStage(Engine engine, ControlSchemeManager controlSchemeManager) {
		super();
		
		this.engine = engine;
		this.renderer = new FxRenderer(engine);
		this.controlSchemeManager = controlSchemeManager;
	}
	
	protected void beginGame() {
		PlayScene scene = PlayScene.getInstance(engine, controlSchemeManager);
		scene.setCanvasLayout(engine.getContainer());
		
		this.changeScene(scene);
	}
	
	public void prevScene() {
		ATunnelersScene scene = (ATunnelersScene) this.getScene();
		this.changeScene(scene.getPrevScene());
	}

	protected void changeScene(ATunnelersScene scene) {
		this.setScene(scene);
		this.setTitle(String.format("%s %s %s", SETTINGS.getGameName(), SETTINGS.getTitleSeparator(), scene.getName()));
		
		this.hide();
		this.show();
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

	public ControlSchemeManager getControls() {
		return this.controlSchemeManager;
	}

}
