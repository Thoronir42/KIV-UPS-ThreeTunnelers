package tunnelers.Game;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.Settings;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class LobbyScene extends AGameScene {

	private static LobbyScene instance;
	private NetWorks nw;

	public static LobbyScene getInstance(NetWorks nw) throws IllegalStateException {

		if (instance == null) {
			instance = createInstance();
		}
		if (nw != null) {
			instance.nw = nw;
		} else if (instance.nw == null) {
			throw new IllegalStateException("Lobby didn't receive NetWorks container and none was previously set.");
		}
		return instance;
	}

	private static LobbyScene createInstance() {
		GridPane root = new GridPane();
		root.setHgap(4);
		root.setVgap(20);
		root.setAlignment(Pos.CENTER);

		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.BURLYWOOD.hashCode()));
		
		Settings settings = Settings.getInstance();
		LobbyScene scene = new LobbyScene(root, settings.getWidth(), settings.getHeight());

		addComponents(root, scene);

		return scene;

	}

	private static void addComponents(GridPane root, LobbyScene scene) {
		TextArea ta_chatBox = new TextArea();
		
		ta_chatBox.setWrapText(true);
		ta_chatBox.setPrefColumnCount(40);
		ta_chatBox.setPrefRowCount(10);
		ta_chatBox.setDisable(true);
		
		scene.ta_chatBox = ta_chatBox;
		root.add(scene.ta_chatBox, 0, 0);

		
		TextField tf_chatIn = new TextField();
		
		scene.tf_chatIn = tf_chatIn;
		root.add(scene.tf_chatIn, 0, 1);
		tf_chatIn.setOnKeyPressed((KeyEvent event) -> {
			switch(event.getCode()){
				case ENTER:
					scene.sendChatMessage();
					break;
			}
		});

		Button but_send = new Button("Odeslat");
		but_send.setOnAction((ActionEvent event) -> {
			scene.sendChatMessage();
		});
		root.add(but_send, 1, 1);

		
		Button but_start = new Button("Vyzkoušet");
		but_start.setOnAction((ActionEvent event) -> {
			scene.getStage().beginGame();
		});
		root.add(but_start, 1, 2);

		Button but_back = new Button("Odejít do menu");
		but_back.setOnAction((ActionEvent event) -> {
			scene.getStage().exit();
		});
		root.add(but_back, 1, 3);
	}
	
	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;

	public LobbyScene(Parent root, double width, double height) {
		super(root, width, height, "Join Game");
	}
	
	public void handleKeyPressed(KeyCode code) {
		switch (code) {
			case ENTER:
				sendChatMessage();
				break;
		}
	}

	@Override
	public void updateChatbox() {
		GameStage stage = this.getStage();
		this.ta_chatBox.setText(stage.getGamechat().getLog());
	}

	@Override
	public void drawScene() {
		//
	}
	
	@Override
	protected void sendChatMessage(String message){
		super.sendChatMessage(message);
		this.tf_chatIn.setText("");
	}

	private void sendChatMessage() {
		this.sendChatMessage(tf_chatIn.getText());
	}
	
}
