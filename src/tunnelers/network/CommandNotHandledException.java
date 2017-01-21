package tunnelers.network;

import tunnelers.network.command.Command;

/**
 *
 * @author Skoro
 */
public class CommandNotHandledException extends CommandException {

	public CommandNotHandledException(Command command) {
		super(command.getType() + ": " + command.getData());
	}
}
