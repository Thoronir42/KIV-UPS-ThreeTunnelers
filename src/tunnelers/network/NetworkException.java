package tunnelers.network;

import java.io.IOException;

/**
 *
 * @author Stepan
 */
class NetworksException extends IOException{

	public NetworksException(String message) {
		super(message);
	}

	public NetworksException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworksException(Throwable cause) {
		super(cause);
	}
	
	
}