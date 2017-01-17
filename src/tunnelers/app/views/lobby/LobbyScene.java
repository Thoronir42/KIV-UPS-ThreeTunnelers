package tunnelers.app.views.lobby;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
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
		chat.box().setPrefSize(400, 260);

		chat.setOnMessageSend(event -> {
			scene.flashDisplay(event.getMessage());
			scene.getEngine().sendPlainText(event.getMessage());
		});

		center.add(chat.box(), 0, 0, 2, 1);
		center.add(chat.input(), 0, 1);

		Button but_send = new Button("Smazat flash");
		but_send.setOnAction((ActionEvent event) -> {
			scene.flashClear();
			//scene.chatView.sendMessage();
		});
		center.add(but_send, 1, 1);

		Button but_start = new Button("Vyzkoušet");
		but_start.setOnAction((ActionEvent event) -> {
			scene.getEngine().beginGame();
		});
		center.add(but_start, 1, 2);

		Button but_back = new Button("Odejít do menu");
		but_back.setOnAction((ActionEvent event) -> {
			scene.getStage().prevScene();
		});
		center.add(but_back, 1, 3);

		return center;
	}

	private final Chat chat;
	
	protected final SimpleChat chatView;
	private final Label caption;
	private final PlayerListView playerListView;

	public LobbyScene(Parent root, double width, double height, Chat chat, FxDefaultColorScheme colors, int capacity) {
		super(root, width, height, "Join Game");
		this.caption = new Label("GAME ROM 6");
		this.chatView = new SimpleChat(colors.getPlayerColorManager());
		this.playerListView = new PlayerListView(colors.getPlayerColorManager(), capacity);
		
		this.chat = chat;
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
