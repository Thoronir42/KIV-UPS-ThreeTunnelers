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
import tunnelers.core.gameRoom.GameRoomFacade;
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
			createButton("TEST-do lobby", (ActionEvent event) -> {
				scene.getEngine().joinGame(new GameRoomFacade((short) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0));
			}),
			createButton("Nastavení", (ActionEvent event) -> {
				scene.getStage().showScene(IView.Scene.Settings);
			}),
			createButton("Ukončit", (ActionEvent event) -> {
				scene.getStage().close();
			}),};

		ServerSelectControl serverSelect = new ServerSelectControl(8, new NameManager(420));
		serverSelect.setHostname(settings.getServerAddress());
		serverSelect.setPort(settings.getServerPort());
		serverSelect.setName("Karel");
		serverSelect.setOnConnectAction((e) -> {
			scene.getEngine().connect(serverSelect.getName(), serverSelect.getHostname(), serverSelect.getPort());
		});

		serverSelect.setStyle("-fx-background-color: rgba(245,245,245,0.95);"
				+ " -fx-border-size: 2px;"
				+ " -fx-border-style: solid;"
				+ " -fx-border-color: black;"
				+ " -fx-padding: 20px;");

		content.add(serverSelect, 0, 0);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].prefWidthProperty().bind(serverSelect.widthProperty());
			content.add(buttons[i], 0, 1 + i);
		}

		TextField txt_flash = new TextField();
		txt_flash.setPromptText("Zpráva k zobrazení");
		Button but_flashDisplay = new Button("Zobrazit");
		Button but_flashClear = new Button("Schovat");

		but_flashDisplay.setOnAction(e -> {
			if (!"".equals(txt_flash.getText().trim())) {
				scene.flashDisplay(txt_flash.getText());
				txt_flash.setText("");
			}
		});
		but_flashClear.setOnAction(e -> {
			scene.flashClear();
		});

		GridPane flasher = new GridPane();
		flasher.add(txt_flash, 0, 0, 2, 1);
		flasher.add(but_flashDisplay, 0, 1);
		flasher.add(but_flashClear, 1, 1);

		content.add(flasher, 1, 0, 1, 3);

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
}
