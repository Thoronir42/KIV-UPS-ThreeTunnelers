package tunnelers.Game;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;

	public LobbyScene(Parent root, double width, double height) {
		super(root, width, height, "Join Game");
	}

	private static void addComponents(GridPane root, LobbyScene scene) {
		Node next = scene.ta_chatBox = new TextArea();
		scene.ta_chatBox.setWrapText(true);
		scene.ta_chatBox.setPrefColumnCount(40);
		scene.ta_chatBox.setPrefRowCount(10);
		next.setDisable(true);

		root.add(next, 0, 0);

		next = scene.tf_chatIn = new TextField();
		root.add(next, 0, 1);
		next.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				scene.handleKeyPressed(KeyCode.ENTER);
			}
		});

		next = new Button("Send");
		((Button) next).setOnAction((ActionEvent event) -> {
			scene.getStage().getNetworks().sendMessage(scene.tf_chatIn.getText());
		});
		root.add(next, 1, 1);

		next = new Button("Try the PlayScene");
		((Button) next).setOnAction((ActionEvent event) -> {
			scene.getStage().beginGame();
		});
		root.add(next, 1, 1);

		next = new Button("Exit to menu");
		((Button) next).setOnAction((ActionEvent event) -> {
			scene.getStage().exit();
		});
		root.add(next, 1, 2);
	}

	public void handleKeyPressed(KeyCode code) {
		switch (code) {
			case ENTER:
				this.getStage().getNetworks().sendMessage(this.tf_chatIn.getText());
				this.tf_chatIn.setText("");
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
	
}
