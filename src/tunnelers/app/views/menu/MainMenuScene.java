package tunnelers.app.views.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;
import tunnelers.core.view.IView;

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
				scene.getEngine().viewServerList();
			}),
			createButton("Nastavení", (ActionEvent event) -> {
				scene.getStage().showScene(IView.Scene.Settings);
			}),
			createButton("Ukončit", (ActionEvent event) -> {
				scene.getStage().close();
			}),};

		for (int i = 0; i < buttons.length; i++) {
			content.add(buttons[i], 0, i);
		}

		scene.tf_addr = new TextField(settings.getServerAddress());
		scene.tf_port = new TextField("" + settings.getServerPort());
		scene.tf_name = new TextField("Karel");

		content.add(scene.tf_addr, 1, 0);
		content.add(scene.tf_port, 2, 0);
		content.add(scene.tf_name, 1, 1, 2, 1);
		content.add(createButton("TMP: Připoj", (e) -> {
			int port = Integer.parseInt(scene.tf_port.getText());
			scene.getEngine().connect(scene.tf_name.getText(), scene.tf_addr.getText(), port);
		}), 1, 2, 2, 1);
		return scene;

	}

	private static Button createButton(String caption, EventHandler<ActionEvent> callback) {
		Button btn = new Button(caption);
		btn.setOnAction(callback);
		btn.setPrefSize(BTN_PREF_WIDTH, BTN_PREF_HEIGHT);
		return btn;
	}

	private TextField tf_addr, tf_port, tf_name;

	public MainMenuScene(Parent root, double width, double height) {
		super(root, width, height, "Hlavní menu");
	}
}
