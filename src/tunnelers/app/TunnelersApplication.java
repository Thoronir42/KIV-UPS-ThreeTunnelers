package tunnelers.app;

import tunnelers.core.engine.IView;
import tunnelers.core.settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.app.assets.Assets;
import tunnelers.core.engine.Engine;

/**
 *
 * @author Stepan
 */
public final class TunnelersApplication extends Application {

	public static final int VERSION = 00101;

	TunnelersStage currentStage;

	@Override
	public void start(Stage primaryStage) {
		Settings settings = Settings.getInstance();
		String userDir = System.getProperty("user.dir");
		
		settings.addConfigFile(userDir + "\\config\\settings.cfg");
		settings.init();

		Assets assets = new Assets();
//		Assets assets = new Assets(userDir + "\\resources\\");
		
		assets.init();
		
		Engine e = new Engine(VERSION, settings);

		currentStage = new TunnelersStage(e.intefrace(), assets, 1);
		e.setView(currentStage);

		currentStage.setOnHidden((WindowEvent event) -> {
			e.exit();
		});

		e.start();
		currentStage.setResizable(false);
		this.currentStage.show();
		
		currentStage.showSceneNow(IView.Scene.MainMenu);
		
		currentStage.currentScene.flashClear(true);
		
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
