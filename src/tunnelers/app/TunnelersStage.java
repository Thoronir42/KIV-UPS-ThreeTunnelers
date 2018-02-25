package tunnelers.app;

import javafx.application.Platform;
import javafx.stage.Stage;
import tunnelers.app.assets.Assets;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.app.render.FxRenderContainer;
import tunnelers.app.render.colors.FxDefaultColorScheme;
import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.app.views.debug.AssetDebugScene;
import tunnelers.app.views.debug.DebugScene;
import tunnelers.app.views.lobby.LobbyScene;
import tunnelers.app.views.menu.MainMenuScene;
import tunnelers.app.views.serverList.GameRoomListScene;
import tunnelers.app.views.settings.SettingsScene;
import tunnelers.app.views.warzone.WarZoneScene;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.engine.EngineUserInterface;
import tunnelers.core.engine.IView;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.settings.Settings;

public final class TunnelersStage extends Stage implements IView, IFlasher {

	private static final String GAME_NAME = "Three Tunnelers",
			TITLE_SEPARATOR = "|";

	private final FxControlsManager controlsManager;
	private final FxRenderContainer renderer;

	protected ATunnelersScene currentScene;
	private final FxDefaultColorScheme colorScheme;

	protected final EngineUserInterface engine;
	protected final Assets assets;
	private final Settings settings;

	public final void update(long tick) {
		if (this.currentScene != null) {
			this.currentScene.update(tick);
		}
	}

	public TunnelersStage(EngineUserInterface engine, Assets assets, Settings settings) {
		super();

		this.engine = engine;
		this.assets = assets;
		this.settings = settings;

		colorScheme = new FxDefaultColorScheme(new FxPlayerColorManager());
		colorScheme.setRandomizer((int x, int y) -> ((int) Math.abs(Math.sin((x + 2) * 7) * 6 + Math.cos(y * 21) * 6)));

		this.controlsManager = new FxControlsManager(settings.getControlSchemesCount());
		this.controlsManager.setOnInputChanged((event) ->
				this.engine.handleInput(event.getInput(), event.getControlsId(), event.isPressed())
		);

		this.renderer = new FxRenderContainer(engine, colorScheme, assets);
	}

	private void changeScene(ATunnelersScene scene) {
		if (scene == null) {
			return;
		}

		this.setScene(this.currentScene = scene);
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
	public void setGameData(Map map, Player[] players) {
		this.renderer.prepareGame(map, players);
	}

	/**
	 * JavaFX thread transition wrapper
	 *
	 * @param scene Identifier of scecne to be shown
	 */
	@Override
	public void showScene(Scene scene) {
		Platform.runLater(() -> this.showSceneNow(scene));
	}

	public void showSceneNow(Scene scene) {
		GameRoom gr;
		Runnable afterChange = null;
		ATunnelersScene newScene;
		switch (scene) {
			case MainMenu:
				newScene = new MainMenuScene(settings);
				break;

			case Settings:
				newScene = new SettingsScene(settings, controlsManager);
				break;

			case GameRoomList:
				newScene = new GameRoomListScene(settings, engine.getHostLocator());
				afterChange = engine::refreshServerList;
				break;

			case Lobby:
				gr = this.engine.getGameRoom();
				if (gr == null) {
					throw new IllegalStateException("Can't show Lobby scene, GameRoom is null");
				}
				newScene = new LobbyScene(settings, gr.getChat(), this.renderer.getColorScheme(), gr.getCapacity());
				afterChange = () -> {
					updateChat();
					updateClients();
				};
				break;

			case Warzone:
				newScene = new WarZoneScene(settings, controlsManager)
						.initLayout(engine.getGameRoom().getPlayerCount(), this.renderer);
				newScene.flashClear(true);
				break;

			default:
				System.err.println("Error switching scene to " + scene.toString());
				return;
		}
		newScene.setAfterFX(renderer.getAfterFX());
		this.changeScene(newScene);
		if (afterChange != null) {
			afterChange.run();
		}
	}

	public void showSceneNow(DebugScene scene) {
		ATunnelersScene newScene = null;
		switch (scene) {
			case Assets:
				newScene = new AssetDebugScene(settings, assets);
				break;
		}

		if (newScene == null) {
			System.err.println("Could not switch scene to " + scene);
			return;
		}

		this.changeScene(newScene);
	}

	@Override
	public void alert(String message) {
		Platform.runLater(() -> this.currentScene.flashDisplay(message));
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
	public void updatePlayers(Player[] players) {
		Platform.runLater(() -> {
			if (!(this.currentScene instanceof LobbyScene)) {
				System.err.println("Can't update player list, current scene is " + this.currentScene.getClass());
				return;
			}

			((LobbyScene) this.currentScene).setPlayers(players);
		});
	}

	@Override
	public void updateClients() {
		// todo: add client update logic?
	}

	@Override
	public void setConnectEnabled(boolean value) {
		if (!(this.currentScene instanceof MainMenuScene)) {
			return;
		}
		((MainMenuScene) this.currentScene).setConnectEnabled(value);
	}

	@Override
	public void setLocalReadyState(boolean ready) {
		if (this.currentScene instanceof LobbyScene) {
			Platform.runLater(() -> ((LobbyScene) this.currentScene).setLocalClientReady(ready));

		}
	}

	@Override
	public void flashDisplay(String message) {
		this.currentScene.flashDisplay(message);
	}

	@Override
	public void flashClear() {
		this.currentScene.flashClear();
	}

	@Override
	public void exit() {
//		Platform.exit();
	}

}
