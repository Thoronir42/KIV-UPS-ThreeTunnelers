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
import tunnelers.app.render.colors.DefaultColorScheme;
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

		FxControlsManager csmgr = new FxControlsManager(1);

		DefaultColorScheme colorScheme = new DefaultColorScheme(new FxPlayerColorManager());
		colorScheme.setRandomizer((int x, int y) -> {
			return ((int) Math.abs(Math.sin((x + 2) * 7) * 6 + Math.cos(y * 21) * 6));
		});
		
		Engine e = new Engine(VERSION, csmgr, settings);

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
