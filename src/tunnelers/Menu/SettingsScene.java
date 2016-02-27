package tunnelers.Menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.Game.IO.Input;
import tunnelers.Game.IO.KeyMap;
import tunnelers.Game.IO.PlrInput;
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

	private static final Border SELECTED_BORDER = new Border(new BorderStroke(Color.AZURE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(GRID_SPACING / 2)));

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

		root.setOnKeyPressed((KeyEvent event) -> {
			scene.handleKeyPressed(event.getCode());
		});
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
		root.add(new VBox(btn_serverDefaults, scene.btn_testServer), 2, 0, 1, 2);

		return root;
	}

	private static GridPane makeKeyConfigPane(SettingsScene scene) {
		GridPane root = new GridPane();
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		root.setAlignment(Pos.CENTER);

		byte[] ControlSchemeIDs = ControlSchemeManager.getKeyboardLayoutIDs();

		Input[] inputs = Input.values();
		for (int v = 0; v < inputs.length; v++) {
			Label lbl = new Label(inputs[v].getLabel());
			root.add(lbl, 0, v);
		}

		scene.btnMap_input = new Button[ControlSchemeIDs.length][inputs.length];

		for (int p = 0; p < ControlSchemeIDs.length; p++) {
			byte cid = ControlSchemeIDs[p];
			AControlScheme controlScheme = scene.controlSchemeManager.getKeyboardScheme(cid);
			for (int v = 0; v < inputs.length; v++) {
				KeyCode kc = scene.controlSchemeManager.getKeyCode(new PlrInput(controlScheme, inputs[v]));
				Button b = new Button(KeyMap.codeToStr(kc));
				b.setPrefWidth(120);

				b.setOnAction((ActionEvent event) -> {
					scene.selectActionForKeycode(b);
				});

				root.add(b, p + 1, v);
				scene.btnMap_input[p][v] = b;
			}
		}
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

	Button selectedPinButton;

	protected Button btn_testServer;
	protected Button[][] btnMap_input;

	private final ControlSchemeManager controlSchemeManager;

	public SettingsScene(Parent root, double width, double height) {
		super(root, width, height, "Nastavení");
		this.controlSchemeManager = settings.getControlSchemeManager();
		selectedPinButton = null;
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

	private void selectActionForKeycode(Button b) {
		b.setBorder(SELECTED_BORDER);
		if (this.selectedPinButton != null) {
			this.selectedPinButton.setBorder(Border.EMPTY);
		}
		this.selectedPinButton = b;
	}

	@Override
	public void handleKeyPressed(KeyCode kc) {
		if (selectedPinButton == null) {
			super.handleKeyPressed(kc);
		} else if (kc.equals(KeyCode.ESCAPE)) {
			selectedPinButton = null;
			return;
		}

		PlrInput newInput = this.getSelectedPin();
		PlrInput prevPin = this.controlSchemeManager.replaceKeyInput(kc, newInput);

		if (prevPin != null) {
			setButtonLabel(prevPin, null);
		}

		setButtonLabel(newInput, kc);

		selectedPinButton = null;
	}

	private PlrInput getSelectedPin() {
		byte[] layoutIDs = ControlSchemeManager.getKeyboardLayoutIDs();
		Input[] inputs = Input.values();

		for (byte sch = 0; sch < layoutIDs.length; sch++) {
			AControlScheme controlScheme = this.controlSchemeManager.getKeyboardScheme(sch);
			for (int inp = 0; inp < inputs.length; inp++) {
				if (this.btnMap_input[sch][inp].equals(this.selectedPinButton)) {
					return new PlrInput(controlScheme, inputs[inp]);
				}
			}
		}
		return null;
	}

	private void setButtonLabel(PlrInput pin, KeyCode kc) {
		Input in = pin.getInput();
		AControlScheme cid = pin.getControlScheme();

		Button b = this.btnMap_input[cid.getID()][in.intVal()];
		b.setBorder(Border.EMPTY);
		b.setText(KeyMap.codeToStr(kc));
	}

	@Override
	public Class getPrevScene() {
		return MainMenuScene.class;
	}

}
