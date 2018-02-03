package ui;

import Mock.GameRoomMock;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.app.TunnelersStage;
import tunnelers.app.assets.Assets;
import tunnelers.core.engine.Engine;
import tunnelers.core.engine.EngineDebugManipulator;
import tunnelers.core.engine.IView;
import tunnelers.core.settings.Settings;

public class WarzoneStageTest extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Settings settings = new Settings().init();
		Assets assets = new Assets();
//		Assets assets = new Assets(userDir + "\\resources\\");

		assets.init();

		Engine e = new Engine(settings);
		EngineDebugManipulator debugManipulator = new EngineDebugManipulator(e);

		TunnelersStage currentStage = new TunnelersStage(e.userInterface(), assets, 1);

		debugManipulator.setGameRoom(GameRoomMock.create(currentStage.getControlsManager(), currentStage.getPlayerColorManager()));

		e.setView(currentStage);

		currentStage.setOnHidden((WindowEvent event) -> e.exit());

		e.start();
		currentStage.setResizable(false);
		currentStage.showSceneNow(IView.Scene.Warzone);
		currentStage.flashClear();

		currentStage.show();
	}
}
