package tunnelers.network.command;

/**
 * 
 * @author Stepan
 */
public interface INetworkProcessor {
	
	/**
	 * Used to signalizes processor of Connection status changes.
	 * @param signal 
	 */
	public void signal(Signal signal);
	
	/**
	 * Processess container instance of Command. Returns indicates if the 
	 * command has been handled successfully.
	 * If command to be handled is of wrong format or should not be handled
	 * at given context, CommandNotHandledException should be thrown.
	 * 
	 * 
	 * @param cmd
	 * @return success of command handling
	 */
	public boolean handle(Command cmd);
	
	
}
