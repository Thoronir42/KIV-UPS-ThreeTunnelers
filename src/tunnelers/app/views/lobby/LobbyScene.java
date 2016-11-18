package tunnelers.app.views.lobby;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
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

	public static LobbyScene getInstance(Chat chat, AColorScheme colors) throws IllegalStateException {
		if (instance == null) {
			instance = createInstance(chat, colors);
		}

		return instance;
	}

	public static void clearInstance() {
		instance = null;
	}

	private static LobbyScene createInstance(Chat chat, AColorScheme colors) {
		GridPane content = new GridPane();
		content.setHgap(4);
		content.setVgap(20);
		content.setAlignment(Pos.CENTER);

		content.setBackground(new Background(new BackgroundFill(new Color(0.11, 0.17, 0.69, 0.2), CornerRadii.EMPTY, Insets.EMPTY)));

		LobbyScene scene = new LobbyScene(content, settings.getWindowWidth(), settings.getWindowHeight(), chat);
		addComponents(content, scene, colors);

		return scene;
	}

	private static void addComponents(GridPane root, LobbyScene scene, AColorScheme colors) {
		SimpleChat chat = scene.chatView = new SimpleChat(colors.playerColors());
		chat.box().setPrefSize(400, 260);

		chat.setOnMessageSend(event -> {
			scene.flashDisplay(event.getMessage());
			scene.getEngine().sendPlainText(event.getMessage());
		});

		root.add(chat.box(), 0, 0, 2, 1);
		root.add(chat.input(), 0, 1);

		Button but_send = new Button("Smazat flash");
		but_send.setOnAction((ActionEvent event) -> {
			scene.flashClear();
			//scene.chatView.sendMessage();
		});
		root.add(but_send, 1, 1);

		Button but_start = new Button("Vyzkoušet");
		but_start.setOnAction((ActionEvent event) -> {
			scene.getEngine().beginGame();
		});
		root.add(but_start, 1, 2);

		Button but_back = new Button("Odejít do menu");
		but_back.setOnAction((ActionEvent event) -> {
			scene.getStage().prevScene();
		});
		root.add(but_back, 1, 3);
	}

	protected SimpleChat chatView;
	private final Chat chat;

	public LobbyScene(Parent root, double width, double height, Chat chat) {
		super(root, width, height, "Join Game");
		this.chat = chat;
	}

	public void updateChatbox() {
		Platform.runLater(() -> {
			this.chatView.setContent(chat.iterator());
			System.out.println("Chat updated");
		});
	}
}
