package tunnelers.network.handling;

import tunnelers.network.command.Signal;

public interface ISignalHandler {
	/**
	 * Used to signalizes processor of TcpConnection status changes.
	 *
	 * @param signal signal to be processed
	 */
	void signal(Signal signal);
}
