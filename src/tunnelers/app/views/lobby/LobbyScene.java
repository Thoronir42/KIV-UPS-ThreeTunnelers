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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.views.components.chat.SimpleChat;
import tunnelers.core.chat.Chat;

/**
 *
 * @author Stepan
 */
public class LobbyScene extends ATunnelersScene {

	private static LobbyScene instance;

	public static LobbyScene getInstance(Chat chat, AColorScheme colors, int capacity) throws IllegalStateException {
		if (instance == null) {
			instance = createInstance(chat, colors, capacity);
		}

		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	private static LobbyScene createInstance(Chat chat, AColorScheme colors, int capacity) {
		BorderPane content = new BorderPane();

		content.setBackground(new Background(new BackgroundFill(new Color(0.11, 0.17, 0.69, 0.2), CornerRadii.EMPTY, Insets.EMPTY)));

		LobbyScene scene = new LobbyScene(content, settings.getWindowWidth(), settings.getWindowHeight(), chat, colors, capacity);
		content.setTop(scene.caption);
		scene.caption.setAlignment(Pos.TOP_RIGHT);
		content.setCenter(buildCenter(scene));
		content.setLeft(buildSidePlayerList(scene, 190));

		return scene;
	}

	private static GridPane buildCenter(LobbyScene scene) {
		GridPane center = new GridPane();
		center.setHgap(4);
		center.setVgap(20);
		center.setAlignment(Pos.CENTER);

		SimpleChat chat = scene.chatView = new SimpleChat(scene.colors.playerColors());
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

	private static VBox buildSidePlayerList(LobbyScene scene, int prefWidth) {
		VBox listing = new VBox(4);
		listing.setPrefWidth(prefWidth);
		listing.setAlignment(Pos.CENTER);
		
		for (int i = 0; i < scene.playerViews.length; i++) {
			PlayerView view = scene.playerViews[i] = new PlayerView();
			view.set("Empty " + i, scene.colors.playerColors().get(i).color());
			
			listing.getChildren().add(view);
		}
		return listing;
	}

	protected SimpleChat chatView;
	private final Label caption;
	private final Chat chat;
	private final AColorScheme colors;
	private final PlayerView[] playerViews;

	public LobbyScene(Parent root, double width, double height, Chat chat, AColorScheme colors, int capacity) {
		super(root, width, height, "Join Game");
		this.caption = new Label("GAME ROM 6");
		this.chat = chat;
		this.colors = colors;
		this.playerViews = new PlayerView[capacity];
	}

	public void updateChatbox() {
		Platform.runLater(() -> {
			this.chatView.setContent(chat.iterator());
			System.out.println("Chat updated");
		});
	}
}
