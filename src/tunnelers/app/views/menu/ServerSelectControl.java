package tunnelers.app.views.menu;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.app.views.StyleHelper;
import tunnelers.app.views.components.inputs.PortTextField;

/**
 *
 * @author Skoro
 */
public final class ServerSelectControl extends GridPane {

	private final NameManager names;
	private final TextField tf_hostname, tf_username;
	private final PortTextField tf_port;
	private final CheckBox cb_startAnew;

	private final Button btn_connect;

	private EventHandler<ServerSelectEvent> onSelected;

	public ServerSelectControl(int spacing, NameManager names) {
		this.names = names;

		this.setHgap(spacing);
		this.setVgap(spacing);

		DoubleProperty prefLabelWidth = new SimpleDoubleProperty(60);
		DoubleProperty prefControlWidth = new SimpleDoubleProperty(172);

		this.tf_hostname = new TextField();
		this.tf_port = new PortTextField();
		this.tf_username = new TextField();
		this.btn_connect = new Button("Připojit se!");
		StyleHelper.inject(btn_connect);
		this.cb_startAnew = new CheckBox("Zaćít nanovo");
		this.cb_startAnew.setTooltip(new Tooltip("Je možné, že si Vás server pamatuje"
				+ " díky uloženému klíči. Pokud se rozhodnete začít nanovo, nebude"
				+ " možné stávající klíč znovu použít."));

		StyleHelper.inject(cb_startAnew);
				
		Label lbl_hostname = new Label("Adresa");
		Label lbl_port = new Label("Port");
		Label lbl_name = new Label("Jméno");

		tf_hostname.prefWidthProperty().bind(prefControlWidth);
		tf_port.prefWidthProperty().bind(prefControlWidth);
		tf_username.prefWidthProperty().bind(prefControlWidth);

		lbl_hostname.setAlignment(Pos.CENTER_RIGHT);
		lbl_port.setAlignment(Pos.CENTER_RIGHT);
		lbl_name.setAlignment(Pos.CENTER_RIGHT);

		lbl_hostname.prefWidthProperty().bind(prefLabelWidth);
		lbl_port.prefWidthProperty().bind(prefLabelWidth);
		lbl_name.prefWidthProperty().bind(prefLabelWidth);

		btn_connect.prefWidthProperty().bind(prefControlWidth.add(prefLabelWidth));
		btn_connect.setOnAction(e -> {
			if (onSelected == null) {
				return;
			}
			onSelected.handle(new ServerSelectEvent(this.getHostname(), this.getPort(), this.getUsername(), !this.cb_startAnew.isSelected()));
		});

		lbl_name.setCursor(Cursor.HAND);
		lbl_name.setStyle("-fx-underline: true;");
		lbl_name.setTextFill(Color.DODGERBLUE);
		lbl_name.setOnMouseClicked(e -> {
			this.setName(this.names.generateNext());
		});
		lbl_name.getOnMouseClicked().handle(null);
		
		this.addColumn(0, lbl_hostname, lbl_port, lbl_name);
		this.addColumn(1, this.tf_hostname, this.tf_port, this.tf_username);

		this.add(btn_connect, 0, 3, 2, 1);
		this.add(this.cb_startAnew, 0, 4, 2, 1);

	}

	public void setHostname(String hostname) {
		this.tf_hostname.setText(hostname);
	}

	public String getHostname() {
		return this.tf_hostname.getText();
	}

	public void setPort(int port) {
		this.tf_port.setPort(port);
	}

	public int getPort() {
		return this.tf_port.getPort();
	}

	public void setName(String name) {
		this.tf_username.setText(name);
	}

	public String getUsername() {
		return this.tf_username.getText();
	}

	public void setOnSelected(EventHandler<ServerSelectEvent> eventHandler) {
		this.onSelected = eventHandler;
	}
}
