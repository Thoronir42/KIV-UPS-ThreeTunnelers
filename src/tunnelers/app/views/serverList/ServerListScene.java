package tunnelers.app.views.serverList;

import tunnelers.app.views.serverList.GameRoomView.GRTVItem;
import generic.RNG;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tunnelers.app.views.serverList.GameRoomView.GameRoomTreeTableView;
import tunnelers.core.settings.Settings;
import tunnelers.app.ATunnelersScene;
import tunnelers.core.settings.NameManager;

/**
 *
 * @author Stepan
 */
public class ServerListScene extends ATunnelersScene {

	public static ServerListScene getInstance() {
		BorderPane content = new BorderPane();

		ServerListScene scene = new ServerListScene(content, settings.getWindowWidth(), settings.getWindowHeight());
		addComponents(content, scene, settings);

		return scene;
	}

	private static void addComponents(BorderPane root, ServerListScene scene, Settings settings) {
		scene.serverList = GameRoomTreeTableView.createInstance();
		scene.serverList.setOnMouseClicked((MouseEvent e) -> {
			scene.serverListClicked(e);
		});

		scene.but_getLobbies = new Button("Obnovit seznam her");
		scene.but_getLobbies.setOnAction((ActionEvent event) -> {
			scene.refreshServerList();
		});

		GridPane center = new GridPane();
		center.setAlignment(Pos.CENTER);

		Button but_goBack = new Button("Zpět..");
		but_goBack.setOnAction((ActionEvent event) -> {
			scene.getStage().prevScene();
		});

//		Label hugeWarning = new Label("Pozor, následující pohledy nejsou součástí demonstrace");
//		hugeWarning.setTextFill(Color.WHITE);
//		hugeWarning.setFont(new Font(28));
//		hugeWarning.setTextAlignment(TextAlignment.CENTER);
//
//		HBox top = new HBox(hugeWarning);
//		top.setAlignment(Pos.CENTER);

		center.add(createTopBar(scene, settings), 0, 0);
		center.add(scene.serverList, 0, 1);
		center.add(but_goBack, 0, 2);

		root.setCenter(center);
		root.setBottom(createBottomBar(scene));

		scene.refreshServerList();
		scene.alert("Čekání na akci");

		but_goBack.requestFocus();
	}

	private static HBox createTopBar(ServerListScene scene, Settings settings) {
		HBox top = new HBox(20);
		top.setPadding(new Insets(6));
		top.setStyle("-fx-background-color: #A8A8A8");

		top.setAlignment(Pos.CENTER);

		scene.topButtons = new HBox();
		scene.topLabels = new HBox(4);
		scene.topLabels.setAlignment(Pos.CENTER);

		scene.tf_localName = new TextField();
		scene.tf_localName.textProperty().bindBidirectional(scene.names.CurrentName);

		Label lblName = new Label("Přezdívka:"),
				lblServer = new Label(String.format("%s:%d", settings.getServerAddress(), settings.getServerPort()));

		lblName.setCursor(Cursor.HAND);
		lblName.setOnMouseClicked(e -> {
			scene.tf_localName.setText(scene.names.generateNext());
		});

		scene.topLabels.getChildren().addAll(lblName, scene.tf_localName, lblServer);

		scene.topButtons.getChildren().add(scene.but_getLobbies);

		top.getChildren().addAll(scene.topLabels, scene.topButtons);

		return top;
	}

	private static HBox createBottomBar(ServerListScene scene) {
		HBox bottom = new HBox();
		bottom.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		bottom.setAlignment(Pos.CENTER);

		bottom.setPadding(new Insets(4));

		scene.lbl_conInfo = new Label();

		scene.lbl_conInfo.setFont(new Font(18));

		bottom.getChildren().add(scene.lbl_conInfo);

		return bottom;
	}

	protected TextField tf_localName;
	protected Button but_getLobbies,
			but_join;

	protected Label lbl_conInfo;

	protected GameRoomTreeTableView serverList;

	protected HBox topButtons,
			topLabels;

	private final NameManager names;

	public ServerListScene(Parent root, double width, double height) {
		super(root, width, height, "Výpis serverů");

		this.names = new NameManager(420);
	}

	private void refreshServerList() {
		serverList.clearItems();

		int n = RNG.getRandInt(10) + 3;
		String[] lobbies = new String[n];
		for (byte i = 0; i < n; i++) {
			int players = RNG.getRandInt(Settings.MAX_PLAYERS) + 1;
			byte flags = 0;
			if (i % 2 == 0) {
				flags |= GameRoom.FLAG_RUNNING;
			}
			if (i % 3 == 1) {
				flags |= GameRoom.FLAG_SPECTATABLE;
			}
			if (players == Settings.MAX_PLAYERS) {
				flags |= GameRoom.FLAG_FULL;
			}
			String s = String.format("%02X%02X%02X%02X", i, Settings.MAX_PLAYERS, players, flags);
			lobbies[i] = s;
		}
		parseAndInsertLobbies(lobbies);
	}

	private void parseAndInsertLobbies(String[] lobbies) {
		for (String l : lobbies) {
			GameRoom gr = GameRoom.fromString(l);
			if (gr == null) {
				continue;
			}
			serverList.add(gr);
		}
	}

	private void serverListClicked(MouseEvent e) {
		GRTVItem selected = this.serverList.getSelectedItem();

		if (selected == null || !(selected instanceof GameRoom)) {
			return;
		}
		if (e.getClickCount() == 2) {
			String name = tf_localName.getText();
			if (name.length() == 0) {
				name = tf_localName.getPromptText();
			}
			this.getEngine().joinGame((GameRoom) selected);
		}
	}

	public void alert(String message) {
		this.lbl_conInfo.setText(message);
	}
}
