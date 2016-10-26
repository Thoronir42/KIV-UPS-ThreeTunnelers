package tunnelers.network;

import tunnelers.network.command.Command;

/**
 *
 * @author Stepan
 */
public interface INetCommandHandler {
	public void handle(Command cmd);
}
