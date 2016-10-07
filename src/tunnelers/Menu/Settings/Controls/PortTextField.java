package tunnelers.Menu.Settings.Controls;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 *
 * @author Stepan
 */
public class PortTextField extends TextField{ 
	public final SimpleIntegerProperty Port;
	
	public void setPort(int port){
		this.Port.set(port);
	}
	
	public int getPort(){
		return this.Port.get();
	}

	public PortTextField() {
		this(generic.Port.TUNNELER_DEFAULT_PORT);
	}

	public PortTextField(int serverPort) {
		this.Port = createPortProperty();
		this.Port.set(serverPort);

		this.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			if (!newValue.matches("\\d*")) {
				setText(newValue.replaceAll("[^\\d]", ""));
				return;
			}
			if(newValue.length() < 1){
				setText(generic.Port.MIN_PORT + "");
				return;
			}
			
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
			if (newValue.intValue() > generic.Port.MAX_PORT) {
				port.set(generic.Port.MAX_PORT);
				return;
			}
			if (newValue.intValue() < generic.Port.MIN_PORT) {
				port.set((oldValue.intValue() < generic.Port.MIN_PORT) ? generic.Port.MIN_PORT : oldValue.intValue());
				return;
			}
			setText(newValue.intValue() + "");
		});

		return port;
	}
}
