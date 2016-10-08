package tunnelers.app.views.lobby;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import tunnelers.core.settings.Settings;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.TunnelersStage;

/**
 *
 * @author Stepan
 */
public class LobbyScene extends ATunnelersScene {

	private static LobbyScene instance;

	public static LobbyScene getInstance(boolean createNew) throws IllegalStateException {
		if (createNew || instance == null) {
			instance = createInstance();
		}
		return instance;
	}
	
	private static LobbyScene createInstance() {
		GridPane content = new GridPane();
		content.setHgap(4);
		content.setVgap(20);
		content.setAlignment(Pos.CENTER);

		content.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));
		
		Canvas canvas = new Canvas();
		StackPane root = new StackPane(canvas, content);
		
		LobbyScene scene = new LobbyScene(root, settings.getWindowWidth(), settings.getWindowHeight());
		scene.canvas = canvas;

		addComponents(content, scene);

		return scene;
	}

	private static void addComponents(GridPane root, LobbyScene scene) {
		WebView he_chatBox = new WebView();
		he_chatBox.setPrefSize(400, 260);
		
		scene.wv_chatBox = he_chatBox;
		root.add(scene.wv_chatBox, 0, 0, 2, 1);
		
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
			//scene.getStage().beginGame();
		});
		root.add(but_start, 1, 2);

		Button but_back = new Button("Odejít do menu");
		but_back.setOnAction((ActionEvent event) -> {
			scene.getStage().prevScene();
		});
		root.add(but_back, 1, 3);
	}
	
	protected WebView wv_chatBox;
	protected TextField tf_chatIn;

	public LobbyScene(Parent root, double width, double height) {
		super(root, width, height, "Join Game");
	}
	
	@Override
	public void handleKeyPressed(KeyCode code) {
		switch (code) {
			case ENTER:
				sendChatMessage();
				break;
		}
	}

	public void updateChatbox() {
		TunnelersStage stage = this.getStage();
		//this.wv_chatBox.getEngine().loadContent(stage.getGamechat().getHtml());
	}
	
	public void drawScene() {
		//
	}
	
	protected void sendChatMessage(String message){
		if(message.length() > 0){
			//super.sendChatMessage(message);
		}
		this.tf_chatIn.setText("");
	}

	private void sendChatMessage() {
		this.sendChatMessage(tf_chatIn.getText());
	}

	@Override
	public Class getPrevScene() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
