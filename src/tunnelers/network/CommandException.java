package tunnelers.network;

/**
 *
 * @author Skoro
 */
public abstract class CommandException extends RuntimeException {

	public CommandException(String message) {
		super(message);
	}
}
