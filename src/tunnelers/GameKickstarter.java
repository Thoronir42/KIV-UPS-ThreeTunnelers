package tunnelers;

import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class GameKickstarter {
	
	NetWorks networks;
	String localName;

	public GameKickstarter(NetWorks networks, String localName) {
		this.networks = networks;
		this.localName = localName;
	}

	public NetWorks getNetworks() {
		return networks;
	}

	public String getLocalName() {
		return localName;
	}
	
	
	
	
}
