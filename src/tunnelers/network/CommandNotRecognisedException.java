package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class CommandNotRecognisedException extends NetworksException{
	
	private final String data;
	
	public CommandNotRecognisedException(String data) {
		this(data, "Received command was not recognised");
	}
	
	public CommandNotRecognisedException(String data, String message){
		super(message);
		this.data = data;
	}

	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + data;
	}
	
	
}
