package tunnelers.app.views.serverList;

import tunnelers.app.views.serverList.GameRoomView.GameRoomViewWrapper;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import tunnelers.app.views.serverList.GameRoomView.GameRoomTreeTableView;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.views.serverList.GameRoomView.IGameRoomTreeViewItem;
import tunnelers.core.gameRoom.IGameRoomInfo;

/**
 *
 * @author Stepan
 */
public class GameRoomListScene extends ATunnelersScene {

	public static GameRoomListScene getInstance(String host) {
		BorderPane content = new BorderPane();

		GameRoomListScene scene = new GameRoomListScene(content, settings.getWindowWidth(), settings.getWindowHeight());
		addComponents(content, scene, host);

		return scene;
	}

	private static void addComponents(BorderPane root, GameRoomListScene scene, String host) {
		GridPane center = new GridPane();
		center.setAlignment(Pos.CENTER);
		
		scene.gameRoomList = new GameRoomTreeTableView();
		scene.gameRoomList.setOnMouseClicked((MouseEvent e) -> {
			scene.gameRoomListClicked(e);
		});
		
		scene.gameRoomList.setPrefWidth(640);

		scene.but_getLobbies = new Button("Obnovit seznam her");
		scene.but_getLobbies.setOnAction((ActionEvent event) -> {
			scene.refreshServerList();
		});

		

		Button but_goBack = new Button("Zpět..");
		but_goBack.setOnAction((ActionEvent event) -> {
			scene.getEngine().disconnect();
		});

//		Label hugeWarning = new Label("Pozor, následující pohledy nejsou součástí demonstrace");
//		hugeWarning.setTextFill(Color.WHITE);
//		hugeWarning.setFont(new Font(28));
//		hugeWarning.setTextAlignment(TextAlignment.CENTER);
//
//		HBox top = new HBox(hugeWarning);
//		top.setAlignment(Pos.CENTER);
		center.add(createTopBar(scene, host), 0, 0);
		center.add(scene.gameRoomList, 0, 1);
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

		topButtons.getChildren().add(scene.but_getLobbies);

		top.getChildren().addAll(topLabels, topButtons);

		return top;
	}

	protected Button but_getLobbies,
			but_join;

	protected GameRoomTreeTableView gameRoomList;

	public GameRoomListScene(Parent root, double width, double height) {
		super(root, width, height, "Výpis serverů");
	}

	private void refreshServerList() {
		gameRoomList.clearItems();
		this.getEngine().refreshServerList();
	}

	public void appendGameRooms(IGameRoomInfo[] gameRooms) {
		for (IGameRoomInfo gri : gameRooms) {
			if (gri == null) {
				continue;
			}
			gameRoomList.add(new GameRoomViewWrapper(gri));
		}
	}

	private void gameRoomListClicked(MouseEvent e) {
		IGameRoomTreeViewItem selected = this.gameRoomList.getSelectedItem();

		if (selected == null || !selected.isGameRoom()) {
			return;
		}
		if (e.getClickCount() == 2) {
			this.getEngine().joinGame(selected);
		}
	}
}
