package tunnelers.app.views.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tunnelers.app.views.settings.controls.PortTextField;

/**
 *
 * @author Skoro
 */
public class ServerSelectControl extends GridPane{
	
	private final NameManager names;
	private final TextField tf_hostname, tf_name;
	private final PortTextField tf_port;
	
	private final Button but_connect;
	
	public ServerSelectControl(long namesGeneratorSeed){
		this.names = new NameManager(namesGeneratorSeed);
		
		this.tf_hostname = new TextField();
		this.tf_port = new PortTextField();
		this.tf_name = new TextField();
		this.but_connect = new Button("Připojit se!");
		
		Label lbl_name = new Label("Jméno");
		lbl_name.setCursor(Cursor.HAND);
		lbl_name.setOnMouseClicked(e -> {
			this.setName(this.names.generateNext());
		});
		
		
		this.addColumn(0, new Label("Adresa"), new Label("Port"), lbl_name);
		this.addColumn(1, this.tf_hostname, this.tf_port, this.tf_name);
		
		this.add(but_connect, 0, 3, 2, 1);
	}
	
	public void setHostname(String hostname){
		this.tf_hostname.setText(hostname);
	}
	
	public String getHostname(){
		return this.tf_hostname.getText();
	}
	
	public void setPort(int port){
		this.tf_port.setPort(port);
	}
	
	public int getPort(){
		return this.tf_port.getPort();
	}
	
	public void setName(String name){
		this.tf_name.setText(name);
	}
	
	public String getName(){
		return this.tf_name.getText();
	}
	
	public void setOnConnectAction(EventHandler<ActionEvent> eventHandler){
		this.but_connect.setOnAction(eventHandler);
	}
}
