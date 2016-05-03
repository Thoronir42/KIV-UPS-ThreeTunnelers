package tunnelers;

import generic.BackPasser;
import tunnelers.Menu.MenuStage;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.Game.GameStage;
import tunnelers.Menu.MainMenuScene;

/**
 *
 * @author Stepan
 */
public final class TunnelersApplication extends Application {

	ATunnelersStage currentStage;
	Settings settings;
	Impulser imp;

	@Override
	public void start(Stage primaryStage) {
		this.settings = Settings.getInstance();
		Settings.getInstance().loadConfigFile("config/settings.cfg");
		TunnelersApplication app = this;
		this.imp = new Impulser(new BackPasser<Long>() {
			@Override
			public void run() {
				app.update(this.get());
			}
		});
		this.changeStage(MenuStage.getInstance());
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

	private ATunnelersStage getStage() {
		return this.currentStage;
	}

	private void changeStage(ATunnelersStage stage) {
		stage.setOnCloseRequest((WindowEvent event) -> {
			stage.exit();
		});
		stage.setOnHidden((WindowEvent event) -> {
			stageHidden(stage.getReturnCode());
		});

		stage.setResizable(false);

		this.currentStage = stage;
		this.currentStage.show();
	}

	private void stageHidden(int stageReturn) {
		ATunnelersStage newStage;
		switch (stageReturn) {
			case ATunnelersStage.CLOSE:
				this.imp.stopRun();
				System.exit(0);
				break;
			case ATunnelersStage.CHANGE_TO_GAME:
				MenuStage oldStage = (MenuStage) this.getStage();
				newStage = new GameStage(oldStage.getReturnNetworks());
				this.changeStage(newStage);
				break;
			case ATunnelersStage.CHANGE_TO_MENU:
				newStage = MenuStage.getInstance();
				newStage.changeScene(MainMenuScene.class);
				this.changeStage(newStage);
				break;

		}
	}
}