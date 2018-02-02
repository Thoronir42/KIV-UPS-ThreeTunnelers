package tunnelers.network.command;

public interface INetworkProcessor {

	/**
	 * Used to signalizes processor of Connection status changes.
	 *
	 * @param signal signal to be processed
	 */
	void signal(Signal signal);

	/**
	 * Processes given Command, returns value which indicates if the
	 * command has been handled successfully.
	 * <p>
	 * If command to be handled is of wrong format or should not be handled
	 * at given context, CommandNotHandledException should be thrown.
	 *
	 * @param cmd command to be handled
	 * @return success of command handling
	 */
	boolean handle(Command cmd);


}
