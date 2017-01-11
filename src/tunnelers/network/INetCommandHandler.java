package tunnelers.network;

import tunnelers.network.command.Command;

/**
 * 
 * @author Stepan
 */
public interface INetCommandHandler {
	/**
	 * Processess container instance of Command. Returns indicates if the 
	 * command has been handled successfully.
	 * 
	 * @param cmd
	 * @return 
	 */
	public boolean handle(Command cmd);
}
