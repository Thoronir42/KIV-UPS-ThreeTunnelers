package tunnelers.Menu.Settings;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Menu.AMenuScene;
import tunnelers.Menu.MainMenuScene;
import tunnelers.Settings;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class SettingsScene extends AMenuScene {

	private static final double GRID_SPACING = 4;
	private static final double RESOLVE_BUTTON_PREF_WIDTH = 160,
			RESOLVE_BUTTON_PREF_HEIGHT = 40;

	public static SettingsScene getInstance() {
		GridPane root = new GridPane();
		root.setStyle("-fx-background-color: #cedace");

		Settings settings = Settings.getInstance();

		SettingsScene scene = new SettingsScene(root, settings.getWidth(), settings.getHeight());
		addComponents(root, scene, settings);

		return scene;
	}

	private static void addComponents(GridPane root, SettingsScene scene, Settings settings) {
		root.setAlignment(Pos.CENTER);
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		root.add(makeServerSettingPane(scene, settings), 0, 0);
		root.add(makeKeyConfigPane(scene), 0, 1);
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
		
		Button btn_serverDefaults = new Button("Reset");
		btn_serverDefaults.setOnAction((ActionEvent e) -> {
			scene.tf_adress.setText(Settings.DEFAULT_SERVER_ADDRESS);
			scene.tf_port.setText("" + Settings.DEFAULT_PORT);
		});
		Label lblAdr = new Label("Adresa:"),
				lblPort = new Label("Port:");

		scene.tf_adress = new TextField(settings.getServerAddress());
		scene.tf_port = new TextField(String.valueOf(settings.getServerPort()));

		root.add(lblAdr, 0, 0);
		root.add(scene.tf_adress, 1, 0);
		root.add(lblPort, 0, 1);
		root.add(scene.tf_port, 1, 1);
		root.add(new VBox(scene.btn_testServer, btn_serverDefaults), 2, 0, 1, 2);

		return root;
	}

	private static KeyConfigPane makeKeyConfigPane(SettingsScene scene) {
		KeyConfigPane root = KeyConfigPane.create(scene.controlSchemeManager);
		
		return root;
	}

	private static HBox makeResolveButtonRack(SettingsScene scene) {
		Button btn_back = new Button("Zpět");
		btn_back.setOnAction((ActionEvent event) -> {
			scene.getStage().changeScene(MainMenuScene.class);
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

	protected TextField tf_adress,
			tf_port;

	protected Button btn_testServer;

	private final ControlSchemeManager controlSchemeManager;

	public SettingsScene(Parent root, double width, double height) {
		super(root, width, height, "Nastavení");
		this.controlSchemeManager = settings.getControlSchemeManager();
	}

	private void testServer() {
		String address = tf_adress.getText();
		int port = Integer.parseInt(tf_port.getText());
		if (NetWorks.serverPresent(address, port)) {

		} else {

		}

	}

	private void saveSettings() {
		try {
			String address = this.tf_adress.getText();
			InetAddress.getAllByName(address);
			int port = Integer.parseInt(this.tf_port.getText());

			settings.setServerAddress(address);
			settings.setServerPort(port);
		} catch (UnknownHostException | NumberFormatException e) {
			System.err.format("%s : %s\n", e.getClass().getSimpleName(), e.getMessage());
			return;
		}
	}

	@Override
	public Class getPrevScene() {
		return MainMenuScene.class;
	}

}