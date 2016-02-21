package tunnelers.Menu;

import generic.BackPasser;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import tunnelers.Settings;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class ServerListScene extends AMenuScene {

	BackPasser<String[]> lobbyPasser;

	public static ServerListScene getInstance() {
		BorderPane root = new BorderPane();
		ServerListScene scene = new ServerListScene(root, settings.getWidth(), settings.getHeight());
		scene.lobbyPasser = new BackPasser<String[]>() {
			@Override
			public void run() {
				scene.parseAndInsertLobbies(this.get());
			}
		};
		return scene;
	}

	protected TextField tf_clientName;
	protected Button but_getLobbies,
			but_join;
	protected Label lbl_conInfo;

	protected ListView<GameRoom> serverList;
	protected ObservableList serverListItems;

	protected HBox topButtons,
			topLabels;

	public ServerListScene(Parent root, double width, double height) {
		super(root, width, height, "Join Game");
		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.BLUEVIOLET.hashCode()));

		serverListItems = FXCollections.observableArrayList();

		addComponents((BorderPane) root);
	}

	@Override
	public Class getPrevScene() {
		return MainMenuScene.class;
	}

	private void addComponents(BorderPane root) {
		HBox top = new HBox(4);
		this.topButtons = new HBox();
		this.topLabels = new HBox();
		top.setStyle("-fx-background-color: #A8A8A8");

		HBox bottom = new HBox();
		HBox bottomLabel = new HBox();
		bottomLabel.setAlignment(Pos.CENTER);
		this.tf_clientName = new TextField("Faggot");
		Label lblName = new Label("Přezdívka:"),
				lblServer = new Label(String.format("%s:%d", settings.getServerAddress(), settings.getServerPort()));

		serverList = new ListView<>(serverListItems);
		serverList.setOnMouseClicked((MouseEvent e) -> {
			if (e.getClickCount() == 2) {
				this.connectToGame(serverList.getSelectionModel().getSelectedItem());
			}
		});
		for (byte i = 0; i < 32; i++) {
			serverListItems.add(new GameRoom(i));
		}
		lbl_conInfo = new Label();
		lbl_conInfo.setText("Waiting...");

		but_getLobbies = new Button("Výpis místností");
		but_getLobbies.setOnAction((ActionEvent event) -> {
			this.refreshServerList();
		});

		Button but_goBack = new Button("Zpět..");
		but_goBack.setOnAction((ActionEvent event) -> {
			this.getStage().prevScene();
		});

		topLabels.getChildren().add(lblServer);
		topLabels.getChildren().add(lblName);
		topLabels.getChildren().add(tf_clientName);

		top.getChildren().add(topLabels);
		topButtons.getChildren().add(but_getLobbies);
		top.getChildren().add(topButtons);

		bottomLabel.getChildren().add(lbl_conInfo);

		bottom.getChildren().add(but_goBack);
		bottom.getChildren().add(bottomLabel);

		root.setTop(top);
		root.setCenter(serverList);
		root.setBottom(bottom);
	}

	private void refreshServerList() {
		serverListItems.clear();
		int n = Settings.getRandInt(12);
		String[] lobbies = new String[n];
		for (byte i = 0; i < n; i++) {
			int players = Settings.getRandInt(Settings.MAX_PLAYERS) + 1;
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
			/*String s = Integer.toHexString(i) + Integer.toHexString(Settings.MAX_PLAYERS) +
			 Integer.toHexString(players) + Integer.toHexString(flags);*/
			System.out.format("ID=%d, MP=%d, CP=%d, F=%d\t%s%n", i, Settings.MAX_PLAYERS, players, flags, s);
			lobbies[i] = s;
		}
		parseAndInsertLobbies(lobbies);

		NetWorks.fetchLobbies(lobbyPasser);
	}

	private void parseAndInsertLobbies(String[] lobbies) {
		for (String l : lobbies) {
			GameRoom gr = GameRoom.fromString(l);
			if (gr == null) {
				continue;
			}
			serverListItems.add(gr);
		}
	}

	private void connectToGame(GameRoom gr) {
		System.out.println("Connection attempt to " + gr + " stopped.");
		if (true) {
			return;
		}
		try {
			String address = settings.getServerAddress(),
					clientName = tf_clientName.getText();
			int port = settings.getServerPort();
			System.out.format("Connecting to: %s:%d%n", address, port);

			NetWorks nw = NetWorks.connectTo(address, port, clientName);
			if (nw.canConnect()) {
				this.getStage().gotoLobby(nw);
			} else {
				this.lbl_conInfo.setText(nw.getStatusLabel());
			}
		} catch (IOException | InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}
}
