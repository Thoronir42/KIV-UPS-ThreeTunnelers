package tunnelers.Menu;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class MainMenuScene extends AMenuScene {

	static MainMenuScene instance;

	public static MainMenuScene getInstance() {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	private static MainMenuScene createInstance() {
		GridPane root = new GridPane();
		root.setHgap(4);
		root.setVgap(8);

		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.BLUEVIOLET.hashCode()));
		MainMenuScene scene = new MainMenuScene(root, settings.getWidth(), settings.getHeight());

		Button[] buttons = new Button[]{
			createButton(scene, "hella"),
			createButton(scene, "joinGame"),
			createButton(scene, "settings"),
			createButton(scene, "exit"),};

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setPrefHeight(42);
			buttons[i].setPrefWidth(140);
			root.add(buttons[i], 0, i);
		}
		root.setAlignment(Pos.CENTER);
		return scene;

	}

	public MainMenuScene(Parent root, double width, double height) {
		super(root, width, height, "Main Menu");
	}

	@Override
	public Class getPrevScene() {
		return null;
	}

	private static Button createButton(MainMenuScene scene, String name) {
		Button btn;
		switch (name) {
			default:
				return null;
			case "hella":
				btn = new Button("WUBA LUBA DUB DUB");
				btn.setOnAction((ActionEvent event) -> {
					try {
						scene.getStage().gotoLobby(NetWorks.createInstance());
					} catch (IOException ex) {
						System.err.println(ex.getLocalizedMessage());
					}
				});
				break;
			case "joinGame":
				btn = new Button("Seznam serverů");
				btn.setOnAction((ActionEvent event) -> {
					scene.getStage().changeScene(ServerListScene.class);
				});
				break;
			case "settings":
				btn = new Button("Settings");
				btn.setOnAction((ActionEvent event) -> {
					scene.getStage().changeScene(SettingsScene.class);
				});
				break;
			case "exit":
				btn = new Button("Exit");
				btn.setOnAction((ActionEvent event) -> {
					scene.getStage().exit();
				});
				break;
		}
		return btn;
	}

}
