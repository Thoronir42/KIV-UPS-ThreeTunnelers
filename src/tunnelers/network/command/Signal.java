package tunnelers.network.command;

/**
 *
 * @author Skoro
 */
public class Signal {

	private Type type;
	private String message;

	public Signal(Type type) {
		this(type, "");
	}

	public Signal(Type type, String message) {
		this.type = type;
		this.message = message;
	}

	public Type getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public static enum Type {
		ConnectingTimedOut,
		ConnectionEstabilished,
		ConnectionReset,
		ConnectingError,
		
		
	}
}
