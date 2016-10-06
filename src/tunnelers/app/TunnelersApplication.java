package tunnelers.app;

import generic.Impulser.Impulser;
import tunnelers.Settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.Game.Chat.Chat;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.app.menu.MainMenuScene;
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
		this.settings = Settings.getInstance("config/settings.cfg");
		this.imp = new Impulser(settings.getTickRate());
		
		Chat chat = new Chat(settings.getChatMessageCapacity());
		NetWorks networks = new NetWorks();
		ControlSchemeManager csmgr = settings.getControlSchemeManager();
		Engine e = new Engine(GameContainer.mockContainer(csmgr, "KAREL"), networks, chat);
		
		TunnelersStage stage = new TunnelersStage(e, csmgr);
		stage.setOnHidden((WindowEvent event) -> {
			e.exit();
			this.imp.stopRun();
		});
		
		this.imp.addHook((event) -> {
			e.update(event.getTick()); 
			stage.update(event.getTick());
		});

		stage.setResizable(false);
		stage.setScene(MainMenuScene.getInstance());
		
		this.currentStage = stage;
		
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
