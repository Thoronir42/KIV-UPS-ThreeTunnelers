package tunnelers.app;

import generic.Impulser.Impulser;
import tunnelers.core.settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.core.chat.Chat;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.app.views.menu.MainMenuScene;
import tunnelers.core.GameContainer;
import tunnelers.core.engine.Engine;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public final class TunnelersApplication extends Application {

	TunnelersStage currentStage;
	Settings settings;
	Impulser imp;

	@Override
	public void start(Stage primaryStage) {
		settings = Settings.getInstance();
		settings.addConfigFile("config/settings.cfg");
		settings.init();
		
		imp = new Impulser(settings.getTickRate());
		
		Chat chat = new Chat(settings.getChatMessageCapacity());
		NetWorks networks = new NetWorks();
		ControlSchemeManager csmgr = settings.getControlSchemeManager();
		Engine e = new Engine(GameContainer.mockContainer(csmgr, "KAREL"), networks, chat);
		
		currentStage = new TunnelersStage(e, csmgr);
		
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

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public void update(long tick) {
		this.currentStage.update(tick);
	}
}
