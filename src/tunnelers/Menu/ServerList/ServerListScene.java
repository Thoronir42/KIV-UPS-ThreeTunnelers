package tunnelers.Menu.ServerList;

import tunnelers.Menu.ServerList.GameRoomView.GRTVItem;
import generic.BackPasser;
import java.io.IOException;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.text.TextAlignment;
import tunnelers.GameKickstarter;
import tunnelers.Menu.AMenuScene;
import tunnelers.Menu.MainMenuScene;
import tunnelers.Menu.ServerList.GameRoomView.GameRoomTreeTableView;
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
		Settings settings = Settings.getInstance();

		ServerListScene scene = new ServerListScene(root, settings.getWidth(), settings.getHeight());
		scene.lobbyPasser = new BackPasser<String[]>() {
			@Override
			public void run() {
				scene.parseAndInsertLobbies(this.get());
			}
		};

		addComponents(root, scene, settings);
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
			scene.goBack();
		});

		Label hugeWarning = new Label("Pozor, následující pohledy nejsou součástí demonstrace");
		hugeWarning.setTextFill(Color.WHITE);
		hugeWarning.setFont(new Font(28));
		hugeWarning.setTextAlignment(TextAlignment.CENTER);
		
		HBox top = new HBox(hugeWarning);
		top.setAlignment(Pos.CENTER);
		
		root.setTop(top);
		
		center.add(createTopBar(scene, settings), 0, 0);
		center.add(scene.serverList, 0, 1);
		center.add(but_goBack, 0, 2);

		root.setCenter(center);
		root.setBottom(createBottomBar(scene));

		scene.refreshServerList();
		
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
		scene.tf_localName.setPromptText(Settings.nameGenerator.next());

		Label lblName = new Label("Přezdívka:"),
				lblServer = new Label(String.format("%s:%d", settings.getServerAddress(), settings.getServerPort()));

		lblName.setCursor(Cursor.HAND);
		lblName.setOnMouseClicked(e -> {
			scene.tf_localName.setPromptText(Settings.nameGenerator.next());
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
		
		scene.lbl_conInfo = new Label();
		scene.SceneStatus.set(Status.Waiting);
		
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
	
	protected final SimpleObjectProperty<Status> SceneStatus;

	public ServerListScene(Parent root, double width, double height) {
		super(root, width, height, "Výpis serverů");
		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.BLUEVIOLET.hashCode()));
		
		this.SceneStatus = new SimpleObjectProperty<>();
		this.SceneStatus.addListener((listener, o, n) -> {
			this.lbl_conInfo.setText(n.label);
		});
	}

	@Override
	public Class getPrevScene() {
		return MainMenuScene.class;
	}

	private void refreshServerList() {
		serverList.clearItems();
		
		int n = Settings.getRandInt(10) + 3;
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
			//System.out.format("ID=%d, MP=%d, CP=%d, F=%d\t%s%n", i, Settings.MAX_PLAYERS, players, flags, s);
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
			serverList.add(gr);
		}
	}

	private void connectToGame(GameRoom gr) {
		if(gr.Full.get()){
			SceneStatus.set(Status.GameFull);
			return;
		}	
		
		try {
			String name = tf_localName.getText();
			if(name.length() == 0){
				name = tf_localName.getPromptText();
			}
			SceneStatus.set(Status.Connecting);
			
			GameKickstarter kickstarter = new GameKickstarter(NetWorks.createInstance(), name);
			this.getStage().kickstartLobby(kickstarter);
			/*
			String address = settings.getServerAddress(),
					clientName = tf_localName.getText();
			int port = settings.getServerPort();
			System.out.format("Connecting to: %s:%d%n", address, port);

			NetWorks nw = NetWorks.connectTo(address, port, clientName);
			nw.toString();
			if (nw.canConnect()) {
				this.getStage().kickstartLobby(kickstarter);
			} else {
				this.lbl_conInfo.setText(nw.getStatusLabel());
			}
			*/

		} catch (IOException ex) {
			System.err.println("Connect failed: " + ex);
		}
	}

	private void serverListClicked(MouseEvent e) {
		GRTVItem selected = this.serverList.getSelectedItem();

		if (selected == null || !(selected instanceof GameRoom)) {
			return;
		}
		if (e.getClickCount() == 2) {
			this.connectToGame((GameRoom) selected);
		}
	}
	
	protected enum Status{
		Waiting("Čekání na akci"),
		Connecting("Probíhá připojování"),
		GameFull("Hra je již plná");
		
		
		private final String label;
		
		private Status(String label){
			this.label = label;
		}
		
	}
}
