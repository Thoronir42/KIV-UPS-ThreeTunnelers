package tunnelers.Menu;

import tunnelers.Menu.ServerList.ServerListScene;
import tunnelers.Menu.Settings.SettingsScene;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.Settings.Settings;

/**
 *
 * @author Stepan
 */
public class MainMenuScene extends AMenuScene {

	private static final int 
			BTN_TYPE_JOIN_GAME = 1,
			BTN_TYPE_SETTINGS = 2,
			BTN_TYPE_EXIT = 3;
	
	private static final double BTN_PREF_WIDTH = 180,
			BTN_PREF_HEIGHT = 42;

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
		root.setAlignment(Pos.CENTER);

		root.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));

		Settings settings = Settings.getInstance();

		MainMenuScene scene = new MainMenuScene(root, settings.getWindowWidth(), settings.getWindowHeight());

		Button[] buttons = new Button[]{
			createButton(scene, BTN_TYPE_JOIN_GAME),
			createButton(scene, BTN_TYPE_SETTINGS),
			createButton(scene, BTN_TYPE_EXIT),};

		for (int i = 0; i < buttons.length; i++) {
			root.add(buttons[i], 0, i);
		}
		return scene;

	}

	private static Button createButton(MainMenuScene scene, int type) {
		Button btn;
		switch (type) {
			default:
				return null;
			case BTN_TYPE_JOIN_GAME:
				btn = new Button("Seznam serverů");
				btn.setOnAction((ActionEvent event) -> {
					scene.getStage().changeScene(ServerListScene.class);
				});
				break;
			case BTN_TYPE_SETTINGS:
				btn = new Button("Nastavení");
				btn.setOnAction((ActionEvent event) -> {
					scene.getStage().changeScene(SettingsScene.class);
				});
				break;
			case BTN_TYPE_EXIT:
				btn = new Button("Ukončit");
				btn.setOnAction((ActionEvent event) -> {
					scene.getStage().exit();
				});
				break;
		}

		btn.setPrefSize(BTN_PREF_WIDTH, BTN_PREF_HEIGHT);
		return btn;
	}

	public MainMenuScene(Parent root, double width, double height) {
		super(root, width, height, "Hlavní menu");
	}

	@Override
	public Class getPrevScene() {
		return null;
	}

}
