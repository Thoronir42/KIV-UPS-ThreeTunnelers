package tunnelers.app.views.settings;

import tunnelers.app.views.settings.controls.PortTextField;
import tunnelers.app.views.settings.controls.KeyConfigPane;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.app.views.menu.MainMenuScene;
import tunnelers.app.views.settings.controls.IpTextfield;
import tunnelers.core.settings.Settings;
import tunnelers.app.ATunnelersScene;

/**
 *
 * @author Stepan
 */
public class SettingsScene extends ATunnelersScene {

	private static final double GRID_SPACING = 4;
	private static final double RESOLVE_BUTTON_PREF_WIDTH = 160,
			RESOLVE_BUTTON_PREF_HEIGHT = 40;

	public static SettingsScene getInstance() {
		GridPane root = new GridPane();
		root.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));

		SettingsScene scene = new SettingsScene(root, settings.getWindowWidth(), settings.getWindowHeight());
		addComponents(root, scene, settings);

		return scene;
	}

	private static void addComponents(GridPane root, SettingsScene scene, Settings settings) {
		root.setAlignment(Pos.CENTER);
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		root.add(makeServerSettingPane(scene, settings), 0, 0);
		root.add(KeyConfigPane.create(scene.controlSchemeManager), 0, 1);
		root.add(makeResolveButtonRack(scene), 0, 2);
	}

	private static GridPane makeServerSettingPane(SettingsScene scene, Settings settings) {
		GridPane root = new GridPane();
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		scene.btn_testServer = new Button("Test serveru");
		scene.btn_testServer.setOnAction((ActionEvent e) -> {
			scene.testServer();
		});
		scene.btn_testServer.setDisable(true);
		
		Button btn_serverDefaults = new Button("Reset");
		btn_serverDefaults.setOnAction((ActionEvent e) -> {
			scene.tf_adress.setText(Settings.DEFAULT_SERVER_ADDRESS);
			scene.tf_port.setText("" + Settings.DEFAULT_PORT);
		});
		Label lblAdr = new Label("Adresa:"),
				lblPort = new Label("Port:");

		scene.tf_adress = new IpTextfield(settings.getServerAddress());
		scene.tf_port = new PortTextField(settings.getServerPort());

		root.add(lblAdr, 0, 0);
		root.add(scene.tf_adress, 1, 0);
		root.add(lblPort, 0, 1);
		root.add(scene.tf_port, 1, 1);
		root.add(new VBox(scene.btn_testServer, btn_serverDefaults), 2, 0, 1, 2);

		return root;
	}

	private static HBox makeResolveButtonRack(SettingsScene scene) {
		Button btn_back = new Button("Zpět");
		btn_back.setOnAction((ActionEvent event) -> {
			scene.goBack();
		});
		Button btn_saveChanges = new Button("Uložit nastavení");
		btn_saveChanges.setOnAction((ActionEvent e) -> {
			scene.saveSettings();
		});

		btn_saveChanges.setPrefSize(RESOLVE_BUTTON_PREF_WIDTH, RESOLVE_BUTTON_PREF_HEIGHT);
		btn_back.setPrefSize(RESOLVE_BUTTON_PREF_WIDTH, RESOLVE_BUTTON_PREF_HEIGHT);

		HBox buttonRack = new HBox(6, btn_saveChanges, btn_back);
		buttonRack.setAlignment(Pos.CENTER);
		buttonRack.setPadding(new Insets(16));
		return buttonRack;
	}

	protected IpTextfield tf_adress;
	protected PortTextField tf_port;

	protected Button btn_testServer;

	private final ControlSchemeManager controlSchemeManager;

	public SettingsScene(Parent root, double width, double height) {
		super(root, width, height, "Nastavení");
		this.controlSchemeManager = settings.getControlSchemeManager();
	}

	private boolean testServer() {
		String address = tf_adress.getText();
		int port = tf_port.Port.get();
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
			String address = this.tf_adress.getText();
			InetAddress.getAllByName(address);
			int port = tf_port.Port.get();

			settings.setServerAddress(address);
			settings.setServerPort(port);
		} catch (UnknownHostException | NumberFormatException e) {
			System.err.format("%s : %s\n", e.getClass().getSimpleName(), e.getMessage());
		}
	}

	@Override
	public Class getPrevScene() {
		return MainMenuScene.class;
	}

}
