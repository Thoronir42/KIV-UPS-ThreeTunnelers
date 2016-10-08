package tunnelers.app;

import tunnelers.core.settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;
import tunnelers.core.chat.Chat;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.app.views.warzone.PlayScene;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.AssetsRenderer;
import tunnelers.app.render.FxRenderer;
import tunnelers.app.render.MapRenderer;
import tunnelers.app.render.colors.AColorScheme;
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
	protected final Assets assets;
	

	public final void update(long tick) {
		
	}

	public TunnelersStage(Engine engine, ControlSchemeManager controlSchemeManager, AColorScheme colorScheme, Assets assets) {
		super();
		
		this.engine = engine;
		this.assets = assets;
		
		MapRenderer mapRenderer = new MapRenderer(colorScheme, engine.getContainer().getWarzone().getMap());
		AssetsRenderer assetsRenderer = new AssetsRenderer(colorScheme, assets, engine.getPlayers());
		
		this.renderer = new FxRenderer(engine, colorScheme, mapRenderer, assetsRenderer);
		this.controlSchemeManager = controlSchemeManager;
	}
	
	protected void beginGame() {
		PlayScene scene = PlayScene.getInstance(engine, controlSchemeManager, this.renderer);
		scene.initLayout(engine.getContainer().getPlayerCount());
		
		this.changeScene(scene);
	}
	
	public void prevScene() {
		ATunnelersScene scene = (ATunnelersScene) this.getScene();
		this.changeScene(scene.getPrevScene());
	}

	protected void changeScene(ATunnelersScene scene) {
		//this.hide();
		
		this.setScene(scene);
		this.renderer.setGraphicsContext(scene.getGraphicsContext());
		this.setTitle(String.format("%s %s %s", SETTINGS.getGameName(), SETTINGS.getTitleSeparator(), scene.getName()));
		
		//this.show();
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
