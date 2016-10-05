package tunnelers.app;

import generic.Impulser.Impulser;
import tunnelers.Settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.Game.Chat.Chat;
import tunnelers.app.game.EngineRenderer;
import tunnelers.app.menu.MainMenuScene;
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
		Engine e = new Engine(null, networks, chat);
		
		EngineRenderer er = new EngineRenderer(e);
		
		TunnelersStage stage = new TunnelersStage(e, er);
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
