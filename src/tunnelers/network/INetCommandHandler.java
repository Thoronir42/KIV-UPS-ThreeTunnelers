package tunnelers.network;

import tunnelers.network.command.Command;

/**
 *
 * @author Stepan
 */
public interface INetCommandHandler {
	public boolean handle(Command cmd);
}
