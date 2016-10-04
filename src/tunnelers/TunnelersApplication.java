package tunnelers;

import generic.Impulser.Impulser;
import tunnelers.Settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.app.menu.MainMenuScene;

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
		TunnelersApplication app = this;
		this.imp = new Impulser(settings.getTickRate());
		
		this.imp.addHook((event) -> { app.update(event.getTick()); });
		
		TunnelersStage stage = new TunnelersStage();
		stage.setOnHidden((WindowEvent event) -> {
			app.imp.stopRun();
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
		getStage().update(tick);
	}

	private TunnelersStage getStage() {
		return this.currentStage;
	}
}
