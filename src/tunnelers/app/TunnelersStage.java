package tunnelers.app;

import tunnelers.common.IUpdatable;
import java.util.Arrays;
import tunnelers.core.view.IView;
import java.util.HashMap;
import tunnelers.core.settings.Settings;
import javafx.application.Platform;
import javafx.stage.Stage;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.app.views.warzone.PlayScene;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.FxRenderHelper;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.views.lobby.LobbyScene;
import tunnelers.app.views.menu.MainMenuScene;
import tunnelers.app.views.serverList.ServerListScene;
import tunnelers.app.views.settings.SettingsScene;
import tunnelers.core.engine.Engine;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class TunnelersStage extends Stage implements IView, IUpdatable {

	private final HashMap<Class, IView.Scene> ROUTER;

	public static final String GAME_NAME = "Three Tunnelers",
			TITLE_SEPARATOR = "|";

	protected static final Settings SETTINGS = Settings.getInstance();

	protected final FxControlsManager controlsManager;
	protected final FxRenderHelper renderer;

	protected ATunnelersScene currentScene;
	protected final AColorScheme colorScheme;

	protected final Engine engine;
	protected final Assets assets;

	@Override
	public final void update(long tick) {
		if (this.currentScene != null) {
			this.currentScene.update(tick);
		}
	}

	public TunnelersStage(Engine engine, FxControlsManager controlsManager, AColorScheme colorScheme, Assets assets) {
		super();

		this.engine = engine;
		this.assets = assets;

		this.renderer = new FxRenderHelper(engine, colorScheme, assets);
		this.colorScheme = colorScheme;
		this.controlsManager = controlsManager;

		this.ROUTER = this.getRouter();
	}

	private HashMap<Class, IView.Scene> getRouter() {
		HashMap<Class, IView.Scene> r = new HashMap<>();

		r.put(SettingsScene.class, IView.Scene.MainMenu);
		r.put(ServerListScene.class, IView.Scene.MainMenu);
		r.put(LobbyScene.class, IView.Scene.ServerList);

		return r;
	}

	public void prevScene() {
		Scene scene = this.ROUTER.getOrDefault(this.getScene().getClass(), null);
		this.showScene(scene);
	}

	protected void changeScene(ATunnelersScene scene) {
		if (scene == null) {
			return;
		}

		this.setScene(this.currentScene = scene);
		this.renderer.setGraphicsContext(scene.getGraphicsContext());
		this.setTitle(String.format("%s %s %s", GAME_NAME, TITLE_SEPARATOR, scene.getName()));
	}

	public FxControlsManager getControls() {
		return this.controlsManager;
	}

	@Override
	public void updateChat() {
		System.out.println("Updating chat on" + this.currentScene);
		if (this.currentScene instanceof LobbyScene) {
			LobbyScene l = (LobbyScene) this.currentScene;
			l.updateChatbox();
		}
	}

	@Override
	public void prepareGame(Map map, Player[] players) {
		this.renderer.getMapRenderer().setMap(map);
		this.renderer.getAssetsRenderer().initAssets(Arrays.asList(players));
	}

	@Override
	public void showScene(Scene scene) {
		Platform.runLater(() -> {
			switch (scene) {
				case MainMenu:
					this.changeScene(MainMenuScene.getInstance());
					break;
				case Settings:
					this.changeScene(SettingsScene.getInstance(controlsManager));
					break;
				case ServerList:
					this.changeScene(ServerListScene.getInstance());
					break;
				case Lobby:
					this.changeScene(LobbyScene.getInstance(this.engine.getChat(), this.renderer.getColorScheme(), engine.getContainer().getCapacity()));
					break;
				case Game:
					PlayScene sc = PlayScene.getInstance(controlsManager);
					sc.initLayout(engine.getContainer().getPlayerCount(), this.renderer);
					this.changeScene(sc);
					break;
			}
		});
	}

	@Override
	public void alert(String message) {
		this.currentScene.flash.display(message);
	}

	@Override
	public AColorScheme getColorScheme() {
		return colorScheme;
	}
}
