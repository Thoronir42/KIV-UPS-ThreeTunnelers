package tunnelers.network;

abstract class CommandException extends RuntimeException {
	CommandException(String message) {
		super(message);
	}
}
