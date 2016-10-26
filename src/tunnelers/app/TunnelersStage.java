package tunnelers.app;

import tunnelers.core.settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;
import tunnelers.app.controls.ControlsManager;
import tunnelers.app.views.warzone.PlayScene;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.AssetsRenderer;
import tunnelers.app.render.FxRenderHelper;
import tunnelers.app.render.MapRenderer;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.views.lobby.LobbyScene;
import tunnelers.app.views.serverList.GameRoom;
import tunnelers.app.views.settings.SettingsScene;
import tunnelers.core.engine.Engine;
import tunnelers.core.engine.EngineStage;

/**
 *
 * @author Stepan
 */
public class TunnelersStage extends Stage {

	public static final String GAME_NAME = "Three Tunnelers",
			TITLE_SEPARATOR = "|";

	protected static final Settings SETTINGS = Settings.getInstance();

	protected final ControlsManager controlsManager;
	protected final FxRenderHelper renderer;

	protected ATunnelersScene currentScene;

	protected final Engine engine;
	protected final Assets assets;

	public final void update(long tick) {
		if (this.currentScene != null) {
			this.currentScene.drawScene(tick);
		}
	}

	public TunnelersStage(Engine engine, ControlsManager controlsManager, AColorScheme colorScheme, Assets assets) {
		super();

		this.engine = engine;
		this.assets = assets;
		
		MapRenderer mapRenderer = new MapRenderer(colorScheme, engine.getContainer().getWarzone().getMap());
		AssetsRenderer assetsRenderer = new AssetsRenderer(colorScheme, assets, engine.getContainer().getPlayers());

		this.renderer = new FxRenderHelper(engine, colorScheme, mapRenderer, assetsRenderer);
		this.controlsManager = controlsManager;
		
	}

	public void joinLobby(String clientName, GameRoom gr) {
		this.changeScene(LobbyScene.getInstance(this.engine.getChat(), this.renderer.getColorScheme()));
	}

	public void beginGame() {
		PlayScene scene = PlayScene.getInstance(controlsManager);
		scene.initLayout(engine.getContainer().getPlayerCount(), this.renderer);

		this.changeScene(scene);
		this.engine.setStage(EngineStage.Warzone);
	}

	public void prevScene() {
		ATunnelersScene scene = (ATunnelersScene) this.getScene();
		this.changeScene(scene.getPrevScene());
	}

	protected void changeScene(ATunnelersScene scene) {
		if (scene == null) {
			return;
		}

		this.setScene(this.currentScene = scene);
		this.renderer.setGraphicsContext(scene.getGraphicsContext());
		this.setTitle(String.format("%s %s %s", GAME_NAME, TITLE_SEPARATOR, scene.getName()));
	}

	public final void changeScene(Class reqScene) {
		changeScene(classToInstance(reqScene));
	}

	private ATunnelersScene classToInstance(Class scene) {
		if (scene == null) {
			return null;
		}
		if (scene == SettingsScene.class) {
			return SettingsScene.getInstance(controlsManager);
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

	public boolean connect(String name, String addr, int port) {
		if (this.engine.connect(name, addr, port)) {
			this.joinLobby("", null);
			return true;
		}
		else System.out.println("Nepripojeno");
		return false;
	}

	public void updateChat() {
		if(this.currentScene instanceof LobbyScene){
			LobbyScene l = (LobbyScene)this.currentScene;
			l.updateChatbox();
		}
	}

}
