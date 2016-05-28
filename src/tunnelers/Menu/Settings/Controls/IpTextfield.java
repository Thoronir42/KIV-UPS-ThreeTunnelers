package tunnelers.Menu.Settings.Controls;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

/**
 *
 * @author Stepan
 */
public class IpTextfield extends TextField{
	
	public IpTextfield(){
		this("");
	}

	public IpTextfield(String serverAddress) {
		this.setTextFormatter(makeFilter());
		setText(serverAddress);
	}
	
	private TextFormatter<?> makeFilter(){
		String regex = makePartialIPRegex();
        final UnaryOperator<Change> ipAddressFilter = c -> {
            String text = c.getControlNewText();
            if  (text.matches(regex)) {
                return c ;
            } else {
                return null ;
            }
        };
        return new TextFormatter<>(ipAddressFilter);
	}
	
    private String makePartialIPRegex() {
        String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))" ;
        String subsequentPartialBlock = "(\\."+partialBlock+")" ;
        String ipAddress = partialBlock+"?"+subsequentPartialBlock+"{0,3}";
        return "^"+ipAddress ;
    }
	
	public InetAddress getAddress(){
		try{
			return InetAddress.getByName(this.getText());
		} catch(UnknownHostException ex){
			return null;
		}
	}
}
