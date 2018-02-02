package tunnelers.app.views.settings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tunnelers.app.views.components.inputs.PortTextField;

public class ServerSelectSetterControl extends GridPane {

	private final TextField tf_adress;
	private final PortTextField tf_port;

	private final Button btn_testServer;
	private final Button btn_setDefaults;

	ServerSelectSetterControl(double spacing) {
		this.setVgap(spacing);
		this.setHgap(spacing);

		this.btn_testServer = new Button("Test");
		this.btn_testServer.setPrefWidth(80);

		this.btn_testServer.setDisable(true);

		this.btn_setDefaults = new Button("Reset");
		this.btn_setDefaults.setPrefWidth(80);
		Label lblAdr = new Label("Adresa");
		Label lblPort = new Label("Port");
		lblPort.prefWidthProperty().bind(lblAdr.widthProperty());

		lblAdr.setAlignment(Pos.CENTER_RIGHT);
		lblPort.setAlignment(Pos.CENTER_RIGHT);


		this.tf_adress = new TextField();
		this.tf_port = new PortTextField();
		this.addColumn(0, lblAdr, lblPort);
		this.addColumn(1, this.tf_adress, this.tf_port);
		this.addColumn(3, this.btn_setDefaults, this.btn_testServer);
	}

	public void setOnTestAction(EventHandler<ActionEvent> onTestAction) {
		this.btn_testServer.setOnAction(onTestAction);
	}

	public void setOnSetDefaultsAction(EventHandler<ActionEvent> onTestAction) {
		this.btn_setDefaults.setOnAction(onTestAction);
	}

	public String getAddress() {
		return tf_adress.getText();
	}

	public void setAddress(String address) {
		tf_adress.setText(address);
	}

	public int getPort() {
		return tf_port.getPort();
	}

	public void setPort(int port) {
		tf_port.setPort(port);
	}


}
