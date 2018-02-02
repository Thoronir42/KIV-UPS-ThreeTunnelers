package tunnelers.network;

import tunnelers.network.command.Command;

public class CommandTooShortException extends CommandException {

	public CommandTooShortException(String message) {
		super(message);
	}

	public CommandTooShortException(Command cmd, int requiredLength) {
		this(String.format("Command must be at least %d characters long. Received: (%d)%s",
				requiredLength, cmd.getLength(), cmd.toString()));
	}

}
