package tunnelers.Menu;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import tunnelers.Settings;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class ServerListScene extends AMenuScene{
    
    public static ServerListScene getInstance(){
        BorderPane root = new BorderPane();
        ServerListScene scene = new ServerListScene(root, settings.getWidth(), settings.getHeight());
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
		
		serverListItems = FXCollections.observableArrayList();;
		
		addComponents((BorderPane)root);
    }
    
    @Override
    public Class getPrevScene() {
        return MainMenuScene.class;
    }
    
    private void addComponents(BorderPane root){
        HBox top = new HBox(4);
		this.topButtons = new HBox();
		this.topLabels = new HBox();
		
		HBox bottom = new HBox();
		HBox bottomLabel = new HBox();
		bottomLabel.setAlignment(Pos.CENTER);
		this.tf_clientName = new TextField("Faggot");
		Label lblName = new Label("Přezdívka:");
		
		serverList = new ListView<>(serverListItems);
		for(byte i = 0; i < 32; i++){
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
    
    private void refreshServerList(){
        serverListItems.clear();
		int n = Settings.getRandInt(12);
		for(byte i = 0; i < 3; i++){
			serverListItems.add(new GameRoom(i));
		}
    }
	
	private void connectToGame(){
		try{
            String address = settings.getServerAddress(),
                   clientName = tf_clientName.getText();
            int port = settings.getServerPort();
            System.out.format("Connecting to: %s:%d%n", address, port);

            NetWorks nw = NetWorks.connectTo(address, port, clientName);
            if(nw.canConnect()){
                this.getStage().gotoLobby(nw);
            } else {
                this.lbl_conInfo.setText(nw.getStatusLabel());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
	}
}
