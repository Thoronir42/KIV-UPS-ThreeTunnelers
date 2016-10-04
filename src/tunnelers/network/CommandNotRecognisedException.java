package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class CommandNotRecognisedException extends NetworksException{
	
	private final String data;
	
	public CommandNotRecognisedException(String data) {
		super("Received command was not recognised");
		this.data = data;
	}

	public String getData() {
		return data;
	}
}
