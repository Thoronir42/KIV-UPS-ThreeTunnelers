package tunnelers.app;

import generic.Impulser.Impulser;
import tunnelers.Settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;

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
		
		this.currentStage = new TunnelersStage();
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
