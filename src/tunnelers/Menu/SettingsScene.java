package tunnelers.Menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.event.ActionEvent;
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

	public static SettingsScene getInstance() {
		GridPane root = new GridPane();
		root.setStyle("-fx-background-color: #cedace");

		Settings settings = Settings.getInstance();

		SettingsScene scene = new SettingsScene(root, settings.getWidth(), settings.getHeight());
		scene.addComponents(root);

		return scene;
	}

	private static final double GRID_SPACING = 4;

	protected TextField tf_adress,
			tf_port;

	Button selectedPinButton;
	Border selectedBorder;

	protected Button btn_testServer;
	protected Button[][] btnMap_input;

	private final ControlSchemeManager controlSchemeManager;

	public SettingsScene(Parent root, double width, double height) {
		super(root, width, height, "Nastavení");
		this.controlSchemeManager = settings.getControlSchemeManager();
		selectedPinButton = null;
	}

	private void addComponents(GridPane root) {
		root.setAlignment(Pos.CENTER);
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		root.add(this.makeServerSettingPane(), 0, 0);
		root.add(this.makeKeyConfigPane(), 0, 1);
		root.add(this.makeButtonRack(), 0, 2);

		root.setOnKeyPressed((KeyEvent event) -> {
			this.handleKeyPressed(event.getCode());
		});
	}

	private GridPane makeServerSettingPane() {
		GridPane root = new GridPane();
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		btn_testServer = new Button("Test serveru");
		btn_testServer.setOnAction((ActionEvent e) -> {
			this.testServer();
		});

		Label lblAdr = new Label("Adresa:"),
				lblPort = new Label("Port:");

		this.tf_adress = new TextField(settings.getServerAddress());
		this.tf_port = new TextField(String.valueOf(settings.getServerPort()));

		root.add(lblAdr, 0, 0);
		root.add(tf_adress, 1, 0);
		root.add(lblPort, 0, 1);
		root.add(tf_port, 1, 1);
		root.add(btn_testServer, 2, 0, 1, 2);

		return root;
	}

	private GridPane makeKeyConfigPane() {
		GridPane root = new GridPane();
		root.setVgap(GRID_SPACING);
		root.setHgap(GRID_SPACING);
		root.setAlignment(Pos.CENTER);

		this.selectedBorder = new Border(new BorderStroke(Color.AZURE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(GRID_SPACING / 2)));

		byte[] ControlSchemeIDs = ControlSchemeManager.getKeyboardLayoutIDs();

		Input[] inputs = Input.values();
		for (int v = 0; v < inputs.length; v++) {
			Label lbl = new Label(inputs[v].getLabel());
			root.add(lbl, 0, v);
		}

		this.btnMap_input = new Button[ControlSchemeIDs.length][inputs.length];

		for (int p = 0; p < ControlSchemeIDs.length; p++) {
			byte cid = ControlSchemeIDs[p];
			AControlScheme controlScheme = this.controlSchemeManager.getKeyboardScheme(cid);
			for (int v = 0; v < inputs.length; v++) {
				KeyCode kc = this.controlSchemeManager.getKeyCode(new PlrInput(controlScheme, inputs[v]));
				Button b = new Button(KeyMap.codeToStr(kc));
				b.setPrefWidth(120);

				b.setOnAction((ActionEvent event) -> {
					selectActionForKeycode(b);
				});

				root.add(b, p + 1, v);
				btnMap_input[p][v] = b;
			}
		}
		return root;
	}

	private HBox makeButtonRack() {
		Button btn_back = new Button("Zpět");
		btn_back.setOnAction((ActionEvent event) -> {
			this.getStage().changeScene(MainMenuScene.class);
		});
		Button btn_saveChanges = new Button("Uložit nastavení");
		btn_saveChanges.setOnAction((ActionEvent e) -> {
			this.saveSettings();
		});
		HBox buttonRack = new HBox(6, btn_saveChanges, btn_back);
		buttonRack.setAlignment(Pos.CENTER);
		return buttonRack;
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
			e.printStackTrace();
			return;
		}
	}

	private void selectActionForKeycode(Button b) {
		b.setBorder(this.selectedBorder);
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
