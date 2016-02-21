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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

	protected TextField tf_adress,
			tf_port;

	protected Button btn_testServer;

	public SettingsScene(Parent root, double width, double height) {
		super(root, width, height, "Nastavení");
	}

	private void addComponents(GridPane root) {
		root.setAlignment(Pos.CENTER);
		root.add(this.makeServerSettingPane(), 0, 0);
		root.add(this.makeKeyConfigPane(), 0, 1);
		root.add(this.makeButtonRack(), 0, 2);
	}

	private GridPane makeServerSettingPane() {
		GridPane root = new GridPane();
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
		byte[] PlayerIDs = new byte[]{KeyMap.PLAYER_PRIMARY, KeyMap.PLAYER_SECONDARY};
		
		KeyMap keyMap = this.settings.getKeyMap();

		Input[] inputs = Input.values();
		for (int v = 0; v < inputs.length; v++) {
			Label lbl = new Label(inputs[v].getLabel());
			root.add(lbl, 0, v);
		}

		for (int i = 0; i < PlayerIDs.length; i++) {
			byte pid = PlayerIDs[i];
			for (int v = 0; v < inputs.length; v++) {
				KeyCode kc = keyMap.getKey(new PlrInput(pid, inputs[v]));
				Button b = new Button(kc.getName());
				root.add(b, i + 1, v);
			}
		}
		return root;
	}

	private HBox makeButtonRack(){
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

	@Override
	public Class getPrevScene() {
		return MainMenuScene.class;
	}

}
