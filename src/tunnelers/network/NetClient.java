package tunnelers.network;

import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class NetClient {
	public static int MAX_NAME_LENGTH = 12;
	
	
	private String name;
	private Player[] players;

	public NetClient() {
		this("");
	}

	public NetClient(String name) throws IllegalArgumentException{
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws IllegalArgumentException{
		if(name.length() > MAX_NAME_LENGTH){
			throw new IllegalArgumentException("Name length must be less or equal to " + MAX_NAME_LENGTH);
		}
		this.name = name;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	
}
