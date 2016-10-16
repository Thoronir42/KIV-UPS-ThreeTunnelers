package tunnelers.app;

import generic.Impulser.Impulser;
import tunnelers.core.settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.core.chat.Chat;
import tunnelers.app.controls.ControlsManager;
import tunnelers.app.assets.Assets;
import tunnelers.app.controls.InputEvent;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.render.colors.DefaultColorScheme;
import tunnelers.app.render.colors.PlayerColors;
import tunnelers.app.views.menu.MainMenuScene;
import tunnelers.core.GameContainer;
import tunnelers.core.engine.Engine;
import tunnelers.core.engine.NetWorks;
import tunnelers.core.model.map.MapGenerator;

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
		NetWorks networks = new NetWorks();
		ControlsManager csmgr = new ControlsManager();
		
		PlayerColors playerColors = new PlayerColors();
		AColorScheme colorScheme = new DefaultColorScheme(playerColors);
		
		GameContainer container = GameContainer.mockContainer(csmgr, "KAREL", playerColors.size());
		container.initWarzone(MapGenerator.mockMap(24, container.getPlayerCount()));
		
		Engine e = new Engine(VERSION, networks, chat);
		e.setContainer(container);
		
		csmgr.setOnInputChanged((InputEvent event) -> {
			e.handleInput(event.getInput(), event.getPlayerId(), event.isPressed());
		});
		
		currentStage = new TunnelersStage(e, csmgr, colorScheme, assets);
		
		currentStage.setOnHidden((WindowEvent event) -> {
			e.exit();
			this.imp.stopRun();
		});
		
		this.imp.addHook((event) -> {
			e.update(event.getTick()); 
			currentStage.update(event.getTick());
		});

		currentStage.setResizable(false);
		currentStage.changeScene(MainMenuScene.class);
		
		this.currentStage.show();
		this.imp.start();

	}

	public void update(long tick) {
		this.currentStage.update(tick);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
