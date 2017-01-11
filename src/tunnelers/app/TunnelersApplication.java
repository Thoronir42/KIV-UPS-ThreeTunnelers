package tunnelers.app;

import tunnelers.core.view.IView;
import generic.Impulser.Impulser;
import tunnelers.core.settings.Settings;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.app.assets.Assets;
import tunnelers.app.controls.InputEvent;
import tunnelers.app.render.colors.FxDefaultColorScheme;
import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.core.engine.Engine;

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

		this.imp = new Impulser(settings.getTickRate());
		
		Engine e = new Engine(VERSION, settings);

		currentStage = new TunnelersStage(e, assets, 1);
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

		this.imp.start();
		e.start();
		this.currentStage.show();
		
		
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
