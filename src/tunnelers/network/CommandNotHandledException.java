package tunnelers.network;

import tunnelers.network.command.Command;

class CommandNotHandledException extends CommandException {
	CommandNotHandledException(Command command) {
		super(command.getType() + ": " + command.getData());
	}
}
