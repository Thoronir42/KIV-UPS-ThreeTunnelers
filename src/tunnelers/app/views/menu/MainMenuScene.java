package tunnelers.app.views.menu;

import tunnelers.app.views.serverList.ServerListScene;
import tunnelers.app.views.settings.SettingsScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;

/**
 *
 * @author Stepan
 */
public class MainMenuScene extends ATunnelersScene {

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
		GridPane content = new GridPane();
		content.setHgap(4);
		content.setVgap(8);
		content.setAlignment(Pos.CENTER);

		content.setBackground(new Background(new BackgroundFill(new Color(0.23, 0.74, .23, 0.21), CornerRadii.EMPTY, Insets.EMPTY)));
		
		MainMenuScene scene = new MainMenuScene(content, settings.getWindowWidth(), settings.getWindowHeight());

		Button[] buttons = new Button[]{
			createButton("Seznam serverů", (ActionEvent event) -> {
					scene.getStage().changeScene(ServerListScene.class);
				}),
			createButton("Nastavení", (ActionEvent event) -> {
					scene.getStage().changeScene(SettingsScene.class);
				}),
			createButton("Ukončit", (ActionEvent event) -> {
					scene.getStage().close();
				}),
		};

		for (int i = 0; i < buttons.length; i++) {
			content.add(buttons[i], i, i, 5, 1);
		}
		return scene;

	}

	private static Button createButton(String caption, EventHandler<ActionEvent> callback) {
		Button btn = new Button(caption);
		btn.setOnAction(callback);
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
