package tunnelers.app.views.serverList;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.views.components.gameRoomTableView.GameRoomTableView;
import tunnelers.app.views.components.gameRoomTreeView.GameRoomViewWrapper;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.settings.Settings;

public class GameRoomListScene extends ATunnelersScene {

	private final Button but_refreshList;
	private final Button but_createRoom;

	private GameRoomTableView gameRooms;

	public GameRoomListScene(Settings settings, String host) {
		super(new BorderPane(), settings.getWindowWidth(), settings.getWindowHeight(), "Výpis serverů");
		BorderPane content = (BorderPane) this.content;

		this.but_refreshList = new Button("Obnovit seznam her");
		this.but_refreshList.setOnAction((ActionEvent event) -> this.refreshServerList());

		this.but_createRoom = new Button("Vytvořit novou místnost");
		this.but_createRoom.setOnAction((event) -> this.getEngine().createRoom());

		addComponents(content, this, host);
	}

	private void refreshServerList() {
		gameRooms.clearItems();
		this.getEngine().refreshServerList();
	}

	public void appendGameRooms(IGameRoomInfo[] gameRooms) {
		for (IGameRoomInfo gri : gameRooms) {
			if (gri == null) {
				continue;
			}
			this.gameRooms.getItems().add(new GameRoomViewWrapper(gri));
		}
	}

	private static void addComponents(BorderPane root, GameRoomListScene scene, String host) {
		GridPane center = new GridPane();
		center.setAlignment(Pos.CENTER);

		scene.gameRooms = new GameRoomTableView();
		scene.gameRooms.setOnRoomSelected((event) -> scene.getEngine().joinRoom(event.getGameRoom()));

		scene.gameRooms.setPrefWidth(640);


		Button but_goBack = new Button("Zpět..");
		but_goBack.setOnAction((ActionEvent event) -> scene.getEngine().disconnect());

//		Label hugeWarning = new Label("Pozor, následující pohledy nejsou součástí demonstrace");
//		hugeWarning.setTextFill(Color.WHITE);
//		hugeWarning.setFont(new Font(28));
//		hugeWarning.setTextAlignment(TextAlignment.CENTER);
//
//		HBox top = new HBox(hugeWarning);
//		top.setAlignment(Pos.CENTER);
		center.add(createTopBar(scene, host), 0, 0);
		center.add(scene.gameRooms, 0, 1);
		center.add(but_goBack, 0, 2);

		root.setCenter(center);

		but_goBack.requestFocus();
	}

	private static HBox createTopBar(GameRoomListScene scene, String host) {
		HBox top = new HBox(20);
		top.setPadding(new Insets(6));
		top.setStyle("-fx-background-color: rgba(172,172,172,0.85);"
				+ "-fx-border-radius: 10 10 0 0;"
				+ "-fx-background-radius: 10 10 0 0;");

		top.setAlignment(Pos.CENTER);

		HBox topButtons = new HBox();
		HBox topLabels = new HBox(4);
		topLabels.setAlignment(Pos.CENTER);

		topLabels.getChildren().addAll(new Label(host));

		topButtons.getChildren().addAll(scene.but_refreshList, scene.but_createRoom);

		top.getChildren().addAll(topLabels, topButtons);

		return top;
	}
}
