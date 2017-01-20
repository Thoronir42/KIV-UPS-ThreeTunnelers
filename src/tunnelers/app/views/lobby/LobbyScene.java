package tunnelers.app.views.lobby;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.render.colors.FxDefaultColorScheme;
import tunnelers.app.views.components.chat.SimpleChat;
import tunnelers.core.chat.Chat;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class LobbyScene extends ATunnelersScene {

	private static LobbyScene instance;

	public static LobbyScene getInstance(Chat chat, FxDefaultColorScheme colors, int capacity) throws IllegalStateException {
		if (instance == null) {
			instance = createInstance(chat, colors, capacity);
		}

		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	private static LobbyScene createInstance(Chat chat, FxDefaultColorScheme colors, int capacity) {
		BorderPane content = new BorderPane();

		content.setBackground(new Background(new BackgroundFill(new Color(0.11, 0.17, 0.69, 0.2), CornerRadii.EMPTY, Insets.EMPTY)));

		LobbyScene scene = new LobbyScene(content, settings.getWindowWidth(), settings.getWindowHeight(), chat, colors, capacity);
		
		scene.caption.prefWidthProperty().bind(scene.widthProperty());
		scene.caption.setAlignment(Pos.TOP_RIGHT);
		
		
		scene.playerListView.setPrefWidth(200);
		scene.playerListView.setAlignment(Pos.CENTER);
		
		content.setTop(scene.caption);
		
		
		content.setCenter(buildCenter(scene));
		content.setLeft(scene.playerListView);

		return scene;
	}

	private static GridPane buildCenter(LobbyScene scene) {
		GridPane center = new GridPane();
		center.setHgap(4);
		center.setVgap(20);
		center.setAlignment(Pos.CENTER);

		SimpleChat chat = scene.chatView;
		chat.setPrefSize(400, 280);

		chat.setOnMessageSend(event -> {
			scene.flashDisplay(event.getMessage());
			scene.getEngine().sendPlainText(event.getMessage());
		});
		center.add(chat, 0, 0, 2, 1);

		center.add(scene.btn_ready, 1, 1);
		
		Button btnBAck = new Button("Odejít do menu");
		btnBAck.setOnAction((ActionEvent event) -> {
			scene.getEngine().leaveRoom();
		});
		center.add(btnBAck, 1, 2);

		return center;
	}

	private final Chat chat;
	
	protected final SimpleChat chatView;
	private final Label caption;
	private final PlayerListView playerListView;
	
	private final Button btn_ready;

	public LobbyScene(Region root, double width, double height, Chat chat, FxDefaultColorScheme colors, int capacity) {
		super(root, width, height, "Join Game");
		this.caption = new Label("GAME ROM 6");
		this.chatView = new SimpleChat(colors.getPlayerColorManager(), true);
		this.playerListView = new PlayerListView(colors.getPlayerColorManager(), capacity);
		
		this.chat = chat;
		
		this.btn_ready = new Button();
		this.setLocalClientReady(false);
	}
	
	public void setLocalClientReady(boolean newValue){
		if(!newValue){
			btn_ready.setText("Jsem ready");
			btn_ready.setOnAction((evt) -> {
				this.getEngine().setReady(true);
			});
		} else {
			btn_ready.setText("Nejsem ready");
			btn_ready.setOnAction((evt) -> {
				this.getEngine().setReady(false);
			});
		}
	}

	public void updateChatbox() {
		Platform.runLater(() -> {
			this.chatView.setContent(chat.iterator());
			System.out.println("Chat updated");
		});
	}
	
	public void setPlayers(Player[] players){
		System.out.println("Setting players to lobby list view");
		for(int i = 0; i < players.length; i++){
			this.playerListView.renderPlayer(i, players[i]);
		}
	}
}
