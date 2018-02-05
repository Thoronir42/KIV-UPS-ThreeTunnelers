package tunnelers.app.views.lobby;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.render.colors.FxDefaultColorScheme;
import tunnelers.app.views.components.chat.SimpleChatControl;
import tunnelers.core.chat.Chat;
import tunnelers.core.player.Player;
import tunnelers.core.settings.Settings;

public class LobbyScene extends ATunnelersScene {
	private final Chat chat;

	private final SimpleChatControl chatView;
	private final Label caption;
	private final PlayerListView playerListView;

	private final Button btn_ready;

	public LobbyScene(Settings settings, Chat chat, FxDefaultColorScheme colors, int capacity) {
		super(new BorderPane(), settings.getWindowWidth(), settings.getWindowHeight(), "Příprava");
		BorderPane content = (BorderPane) this.content;

		content.setBackground(new Background(new BackgroundFill(new Color(0.11, 0.17, 0.69, 0.2), CornerRadii.EMPTY, Insets.EMPTY)));

		this.caption = new Label("");
		this.chatView = new SimpleChatControl(colors.getPlayerColorManager(), true);
		this.playerListView = new PlayerListView(colors.getPlayerColorManager(), capacity);
		this.chat = chat;
		this.btn_ready = new Button();

		this.caption.prefWidthProperty().bind(this.widthProperty());
		this.caption.setAlignment(Pos.TOP_RIGHT);

		this.playerListView.setPrefWidth(200);
		this.playerListView.setAlignment(Pos.CENTER);

		content.setTop(this.caption);

		content.setCenter(buildCenter(this));
		content.setLeft(this.playerListView);

		this.setLocalClientReady(false);
	}

	public void setLocalClientReady(boolean ready) {
		if (!ready) {
			btn_ready.setText("Tak jdem na to");
			btn_ready.setOnAction((evt) -> this.getEngine().setReady(true));
		} else {
			btn_ready.setText("Vydržte chviličku");
			btn_ready.setOnAction((evt) -> this.getEngine().setReady(false));
		}
	}

	public void updateChatbox() {
		Platform.runLater(() -> {
			this.chatView.setContent(chat.iterator());
			System.out.println("Chat updated");
		});
	}

	public void setPlayers(Player[] players) {
		System.out.println("Setting players to lobby list view");
		for (int i = 0; i < players.length; i++) {
			this.playerListView.renderPlayer(i, players[i]);
		}
	}


	private static GridPane buildCenter(LobbyScene scene) {
		GridPane center = new GridPane();
		center.setHgap(4);
		center.setVgap(20);
		center.setAlignment(Pos.CENTER);

		SimpleChatControl chat = scene.chatView;
		chat.setPrefSize(400, 280);

		chat.setOnMessageSend(event -> scene.getEngine().sendPlainText(event.getMessage()));
		center.add(chat, 0, 0, 2, 1);

		center.add(scene.btn_ready, 1, 1);

		Button btnBAck = new Button("Opustit místnost");
		btnBAck.setOnAction(event -> scene.getEngine().leaveRoom());
		center.add(btnBAck, 1, 2);

		return center;
	}
}
