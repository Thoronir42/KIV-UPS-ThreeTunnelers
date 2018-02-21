package tunnelers.network.command;

public class CommandNotHandledException extends CommandException {
	public CommandNotHandledException(Command command) {
		super(command.getType() + ": " + command.getData());
	}
}
