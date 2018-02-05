package tunnelers.app.views.settings;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.app.views.components.keybinding.KeyConfigPane;
import tunnelers.core.engine.IView;
import tunnelers.core.settings.Settings;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SettingsScene extends ATunnelersScene {

	private static final double RESOLVE_BUTTON_PREF_WIDTH = 160,
			RESOLVE_BUTTON_PREF_HEIGHT = 40;

	private static final double GRID_SPACING = 4;

	private final ServerSelectSetterControl serverSelectSetterControl;
	private final FxControlsManager controlSchemeManager;

	private final Settings settings;

	public SettingsScene(Settings settings, FxControlsManager controls) {
		super(new GridPane(), settings.getWindowWidth(), settings.getWindowHeight(), "Nastavení");
		GridPane content = new GridPane();
		content.setBackground(new Background(new BackgroundFill(new Color(0.42, 0.87, 0.93, 0.25), CornerRadii.EMPTY, Insets.EMPTY)));

		this.settings = settings;
		this.controlSchemeManager = controls;
		this.serverSelectSetterControl = makeServerSettingPane(this, settings);
		addComponents(content, this);
	}

	private boolean testServer(String address, int port) {
//		NetWorks nw = this.getNetworks();
//		
//		if (nw.serverPresent(address, port)) {
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}

	private void saveSettings() {
		try {
			String hostname = this.serverSelectSetterControl.getAddress();
			InetAddress.getByName(hostname);
			int port = this.serverSelectSetterControl.getPort();

			settings.setServerAddress(hostname);
			settings.setServerPort(port);

			this.flashDisplay(String.format("Adresa serveru %s:%d byla uložena.", hostname, port));
		} catch (UnknownHostException e) {
			System.err.format("Adresa serveru nemohla být ověřena");
		}
	}


	private static void addComponents(GridPane root, SettingsScene scene) {
		root.setAlignment(Pos.CENTER);
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		root.add(scene.serverSelectSetterControl, 0, 0);
		root.add(KeyConfigPane.create(scene.controlSchemeManager), 0, 1);
		root.add(makeResolveButtonRack(scene), 0, 2);
	}

	private static ServerSelectSetterControl makeServerSettingPane(SettingsScene scene, Settings settings) {
		ServerSelectSetterControl control = new ServerSelectSetterControl(GRID_SPACING);
		control.setOnTestAction((event) -> scene.testServer(control.getAddress(), control.getPort()));
		control.setOnSetDefaultsAction((event) -> {
			// todo: use actual default values
			control.setAddress(settings.getServerAddress());
			control.setPort(settings.getServerPort());
		});

		control.setStyle("-fx-background-color: rgba(245,245,245,0.95);"
				+ " -fx-border-size: 2px;"
				+ " -fx-border-style: solid;"
				+ " -fx-border-color: black;"
				+ " -fx-padding: 20px;");

		control.setDisable(true);

		return control;
	}

	private static HBox makeResolveButtonRack(SettingsScene scene) {
		Button btn_back = new Button("Zpět");
		btn_back.setOnAction((ActionEvent event) -> scene.getStage().showScene(IView.Scene.MainMenu));
		Button btn_saveChanges = new Button("Uložit nastavení");
		btn_saveChanges.setOnAction((ActionEvent e) -> scene.saveSettings());

		btn_saveChanges.setPrefSize(RESOLVE_BUTTON_PREF_WIDTH, RESOLVE_BUTTON_PREF_HEIGHT);
		btn_back.setPrefSize(RESOLVE_BUTTON_PREF_WIDTH, RESOLVE_BUTTON_PREF_HEIGHT);

		HBox buttonRack = new HBox(6, btn_saveChanges, btn_back);
		buttonRack.setAlignment(Pos.CENTER);
		buttonRack.setPadding(new Insets(16));
		return buttonRack;
	}
}
