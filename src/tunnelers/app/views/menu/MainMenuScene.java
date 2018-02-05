package tunnelers.app.views.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.views.StyleHelper;
import tunnelers.app.views.debug.DebugScene;
import tunnelers.app.views.debug.FlashTester;
import tunnelers.core.engine.IView;
import tunnelers.core.settings.Settings;

public class MainMenuScene extends ATunnelersScene {

	private static final double BTN_PREF_WIDTH = 180,
			BTN_PREF_HEIGHT = 42;

	private final Settings settings;

	private final ServerSelectControl serverSelect;

	public MainMenuScene(Settings settings) {
		super(new GridPane(), settings.getWindowWidth(), settings.getWindowHeight(), "Hlavní menu");
		this.settings = settings;
		GridPane content = (GridPane) this.content;
		content.setHgap(4);
		content.setVgap(8);
		content.setAlignment(Pos.CENTER);
		content.setBackground(new Background(new BackgroundFill(new Color(0.23, 0.74, .23, 0.21), CornerRadii.EMPTY, Insets.EMPTY)));

		this.serverSelect = createServerSelect(this, settings);

		Button[] buttons = new Button[]{
				createButton("Nastavení", (ActionEvent event) -> this.getStage().showScene(IView.Scene.Settings)),
				createButton("Ukončit", (ActionEvent event) -> this.getStage().close()),};

		content.add(this.serverSelect, 0, 0);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].prefWidthProperty().bind(this.serverSelect.widthProperty());
			content.add(buttons[i], 0, 1 + i);
		}

//		content.add(new FlashTester(this), 1, 0, 1, 3);

	}

	public void setConnectEnabled(boolean value) {
		serverSelect.setDisable(!value);
		serverSelect.setCursor(value ? Cursor.DEFAULT : Cursor.WAIT);
	}

	@Override
	public void handleKeyPressed(KeyCode code) {
		switch (code) {
			case ENTER:
				serverSelect.submit();
				break;
			default:
				super.handleKeyPressed(code);
		}
	}


	private static ServerSelectControl createServerSelect(MainMenuScene scene, Settings settings) {
		ServerSelectControl serverSelectControl = new ServerSelectControl(8, new NameManager(System.currentTimeMillis()));
		serverSelectControl.setHostname(settings.getServerAddress());
		serverSelectControl.setPort(settings.getServerPort());

		serverSelectControl.setStyle("-fx-background-color: rgba(245,245,245,0.95);"
				+ " -fx-border-size: 2px;"
				+ " -fx-border-style: solid;"
				+ " -fx-border-color: black;"
				+ " -fx-padding: 20px;");

		serverSelectControl.setOnSelected((ServerSelectEvent e) -> {
					String hostname = e.getHostname();
					int port = e.getPort();
					if (hostname.equals("debug")) {
						switch (port) {
							case 1:
								scene.getStage().showSceneNow(DebugScene.Assets);
								return;
						}
					}
					scene.getEngine().connect(e.getUsername(), hostname, port, e.useReconnect());
				}
		);

		return serverSelectControl;
	}

	private static Button createButton(String caption, EventHandler<ActionEvent> callback) {
		Button btn = new Button(caption);
		StyleHelper.inject(btn);
		btn.setOnAction(callback);
		btn.setPrefSize(BTN_PREF_WIDTH, BTN_PREF_HEIGHT);
		return btn;
	}
}
