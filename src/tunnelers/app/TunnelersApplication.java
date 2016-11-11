package tunnelers.app;

import tunnelers.app.views.TunnelersStage;
import generic.Impulser.Impulser;
import tunnelers.core.settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import temp.Mock;
import tunnelers.core.chat.Chat;
import tunnelers.app.controls.ControlsManager;
import tunnelers.app.assets.Assets;
import tunnelers.app.controls.InputEvent;
import tunnelers.app.render.colors.DefaultColorScheme;
import tunnelers.app.render.colors.PlayerColors;
import tunnelers.core.gameRoom.GameContainer;
import tunnelers.core.engine.Engine;
import tunnelers.network.NetAdapter;
import temp.MapGenerator;
import tunnelers.app.views.IView;

/**
 *
 * @author Stepan
 */
public final class TunnelersApplication extends Application {

	public static final int VERSION = 00101;

	TunnelersStage currentStage;

	Impulser imp;

	@Override
	public void start(Stage primaryStage) {
		Settings settings = Settings.getInstance();
		settings.addConfigFile(System.getProperty("user.dir") + "\\config\\settings.cfg");
		settings.init();

		Assets assets = new Assets();
		assets.init();

		imp = new Impulser(settings.getTickRate());

		Chat chat = new Chat(settings.getChatMessageCapacity());
		NetAdapter networks = new NetAdapter();
		ControlsManager csmgr = new ControlsManager();

		DefaultColorScheme colorScheme = new DefaultColorScheme(new PlayerColors());
		colorScheme.setRandomizer((int x, int y) -> {
			return ((int) Math.abs(Math.sin((x + 2) * 7) * 6 + Math.cos(y * 21) * 6));
		});

		GameContainer container = Mock.gameContainer(csmgr, "KAREL", colorScheme.playerColors().size());
		container.initWarzone(MapGenerator.mockMap(20, 12, 8, container.getPlayerCount()));

		Engine e = new Engine(VERSION, networks, chat);
		e.setContainer(container);

		csmgr.setOnInputChanged((InputEvent event) -> {
			e.handleInput(event.getInput(), event.getPlayerId(), event.isPressed());
		});

		currentStage = new TunnelersStage(e, csmgr, colorScheme, assets);
		e.setView(currentStage);

		currentStage.setOnHidden((WindowEvent event) -> {
			e.exit();
			this.imp.stopRun();
		});

		this.imp.addHook((event) -> {
			e.update(event.getTick());
			currentStage.update(event.getTick());
		});

		currentStage.setResizable(false);
		currentStage.showScene(IView.Scene.MainMenu);

		this.currentStage.show();
		this.imp.start();
		networks.start();

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
