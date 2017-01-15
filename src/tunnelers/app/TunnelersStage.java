package tunnelers.app;

import tunnelers.common.IUpdatable;
import tunnelers.core.engine.IView;
import java.util.HashMap;
import tunnelers.core.settings.Settings;
import javafx.application.Platform;
import javafx.stage.Stage;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.app.views.warzone.PlayScene;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.FxRenderContainer;
import tunnelers.app.render.colors.FxDefaultColorScheme;
import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.app.views.lobby.LobbyScene;
import tunnelers.app.views.menu.MainMenuScene;
import tunnelers.app.views.serverList.GameRoomListScene;
import tunnelers.app.views.settings.SettingsScene;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.engine.EngineUserInterface;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;

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
	protected final FxRenderContainer renderer;

	protected ATunnelersScene currentScene;
	protected final FxDefaultColorScheme colorScheme;

	protected final EngineUserInterface engine;
	protected final Assets assets;

	@Override
	public final void update(long tick) {
		if (this.currentScene != null) {
			this.currentScene.update(tick);
		}
	}

	public TunnelersStage(EngineUserInterface engine, Assets assets, int supportedControlSchemes) {
		super();

		this.engine = engine;
		this.assets = assets;

		colorScheme = new FxDefaultColorScheme(new FxPlayerColorManager());
		colorScheme.setRandomizer((int x, int y) -> {
			return ((int) Math.abs(Math.sin((x + 2) * 7) * 6 + Math.cos(y * 21) * 6));
		});

		this.controlsManager = new FxControlsManager(supportedControlSchemes);
		this.controlsManager.setOnInputChanged((event) -> {
			this.engine.handleInput(event.getInput(), event.getControlsId(), event.isPressed());
		});

		this.renderer = new FxRenderContainer(engine, colorScheme, assets);

		this.ROUTER = this.getRouter();
	}

	private HashMap<Class, IView.Scene> getRouter() {
		HashMap<Class, IView.Scene> r = new HashMap<>();

		r.put(SettingsScene.class, IView.Scene.MainMenu);
		r.put(GameRoomListScene.class, IView.Scene.MainMenu);
		r.put(LobbyScene.class, IView.Scene.GameRoomList);

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
		this.renderer.prepareGame(map, players);
	}

	@Override
	public void showScene(Scene scene) {
		Platform.runLater(() -> {
			GameRoom gr;
			Runnable afterChange = null;
			ATunnelersScene newScene = null;
			switch (scene) {
				case MainMenu:
					newScene = MainMenuScene.getInstance();
					break;
				case Settings:
					newScene = SettingsScene.getInstance(controlsManager);
					break;
				case GameRoomList:
					newScene = GameRoomListScene.getInstance(engine.getHostLocator());
					break;
				case Lobby:
					gr = this.engine.getGameRoom();
					newScene = LobbyScene.getInstance(gr.getChat(), this.renderer.getColorScheme(), gr.getCapacity());
					break;
				case Game:
					PlayScene sc = PlayScene.getInstance(controlsManager);
					sc.initLayout(engine.getGameRoom().getPlayerCount(), this.renderer);
					newScene = sc;
					break;
			}
			if (newScene == null) {
				System.err.println("Error switching scene to " + scene.toString());
				return;
			}
			newScene.setAfterFX(renderer.getAfterFX());
			this.changeScene(newScene);
			if (afterChange != null) {
				afterChange.run();
			}
		});
	}

	@Override
	public void alert(String message) {
		Platform.runLater(() -> {
			this.currentScene.flashDisplay(message);
		});
	}
	
	@Override
	public PlayerColorManager getPlayerColorManager() {
		return colorScheme.getPlayerColorManager();
	}

	@Override
	public AControlsManager getControlsManager() {
		return this.controlsManager;
	}

	@Override
	public void appendGameRoomsToList(IGameRoomInfo[] rooms) {
		if (!(this.currentScene instanceof GameRoomListScene)) {
			System.err.println("Can't append game rooms, wrong scene");
			return;
		}
		((GameRoomListScene) this.currentScene).appendGameRooms(rooms);
	}

	@Override
	public void updatePlayerList(Player[] players) {
		if (!(this.currentScene instanceof LobbyScene)) {
			System.err.println("Can't update player list, wrong scene");
			return;
		}
		((LobbyScene) this.currentScene).setPlayers(players);
	}

	@Override
	public void setConnectEnabled(boolean value) {
		if (!(this.currentScene instanceof MainMenuScene)) {
			return;
		}
		((MainMenuScene) this.currentScene).setConnectEnabled(value);
	}

}
