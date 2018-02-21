package tunnelers.network.command;

public abstract class CommandException extends RuntimeException {
	CommandException(String message) {
		super(message);
	}
}
