package tunnelers.app;

import tunnelers.core.settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;
import tunnelers.core.chat.Chat;
import tunnelers.app.controls.ControlsManager;
import tunnelers.app.views.warzone.PlayScene;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.AssetsRenderer;
import tunnelers.app.render.FxRenderHelper;
import tunnelers.app.render.MapRenderer;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.engine.Engine;

/**
 *
 * @author Stepan
 */
public class TunnelersStage extends Stage {

	protected static final Settings SETTINGS = Settings.getInstance();
	
	protected final ControlsManager controlsManager;
	protected final FxRenderHelper renderer;

	protected ATunnelersScene currentScene;

	protected Chat chat;
	protected final Engine engine;
	protected final Assets assets;
	

	public final void update(long tick) {
		if(this.currentScene != null){
			this.currentScene.drawScene();
		}
	}

	public TunnelersStage(Engine engine, ControlsManager controlsManager, AColorScheme colorScheme, Assets assets) {
		super();
		
		this.engine = engine;
		this.assets = assets;
		
		MapRenderer mapRenderer = new MapRenderer(colorScheme, engine.getContainer().getWarzone().getMap());
		AssetsRenderer assetsRenderer = new AssetsRenderer(colorScheme, assets, engine.getPlayers());
		
		this.renderer = new FxRenderHelper(engine, colorScheme, mapRenderer, assetsRenderer);
		this.controlsManager = controlsManager;
	}
	
	protected void joinLobby(String clientName){
		
	}
	
	protected void beginGame() {
		PlayScene scene = PlayScene.getInstance(controlsManager);
		scene.initLayout(engine.getContainer().getPlayerCount(), this.renderer);
		
		this.changeScene(scene);
	}
	
	public void prevScene() {
		ATunnelersScene scene = (ATunnelersScene) this.getScene();
		this.changeScene(scene.getPrevScene());
	}

	protected void changeScene(ATunnelersScene scene) {
		this.setScene(this.currentScene = scene);
		this.renderer.setGraphicsContext(scene.getGraphicsContext());
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
		try {
			return (ATunnelersScene) scene.getDeclaredMethod("getInstance").invoke(null);
		} catch (IllegalAccessException | NoSuchMethodException |
				IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.err.format("Couldn't get instance of new scene: %s=%s\n", e.getClass().getSimpleName(), e.getMessage());
		}
		return null;
	}

	public ControlsManager getControls() {
		return this.controlsManager;
	}

}
