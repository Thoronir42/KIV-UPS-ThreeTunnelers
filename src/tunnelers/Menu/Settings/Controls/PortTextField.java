package tunnelers.Menu.Settings.Controls;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class PortTextField extends TextField {

	public static final int MAX_PORT = 65535;

	public final SimpleIntegerProperty Port;

	public PortTextField() {
		this(Settings.DEFAULT_PORT);
	}

	public PortTextField(int serverPort) {
		this.setText("" + serverPort);
		this.Port = createPortProperty();
		this.Port.set(serverPort);

		this.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (!newValue.matches("\\d*")) {
				setText(newValue.replaceAll("[^\\d]", ""));
				return;
			}
			if(newValue.length() < 1){
				setText("0");
				return;
			}
			
			System.out.println("Text set: " + newValue);
			try {
				int n = Integer.parseInt(newValue);
				Port.set(n);
			} catch (NumberFormatException ex) {
				System.err.println("Format exception: " + ex);
				setText(Port.get() + "");
			}
		});
	}

	private SimpleIntegerProperty createPortProperty() {
		SimpleIntegerProperty port = new SimpleIntegerProperty();

		port.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			System.out.println("Settings port: " + newValue);
			if (newValue.intValue() > MAX_PORT) {
				port.set(MAX_PORT);
				return;
			}
			if (newValue.intValue() < 0) {
				port.set(0);
				return;
			}
			setText(newValue.intValue() + "");
		});

		return port;
	}
}
