package tunnelers.app;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.app.assets.Assets;
import tunnelers.core.engine.Engine;
import tunnelers.core.engine.IView;
import tunnelers.core.settings.Settings;
import tunnelers.network.Networks;
import tunnelers.network.adapter.tcp.TcpAdapter;
import tunnelers.network.codec.NoCodec;

public final class TunnelersApplication extends Application {

	@Override
	public void start(Stage primaryStage) {
		Settings settings = Settings.getInstance();
		String userDir = System.getProperty("user.dir");

		settings.addConfigFile(userDir + "\\config\\settings.cfg");
		settings.init();

		Assets assets = new Assets();
//		Assets assets = new Assets(userDir + "\\resources\\");

		assets.init();

		Networks networks = new Networks(new TcpAdapter(), new NoCodec());
		Engine e = new Engine(settings, networks);
		networks.setHandler(e);

		TunnelersStage currentStage = new TunnelersStage(e.userInterface(), assets, settings);
		e.setView(currentStage);

		currentStage.setOnHidden((WindowEvent event) -> e.exit());

		e.start();
		currentStage.setResizable(false);
		currentStage.show();

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
