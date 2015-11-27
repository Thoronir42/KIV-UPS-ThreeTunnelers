package tunnelers.Menu;

import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class GameRoom {
	byte roomID;
	byte maxPlayers;
	byte curPlayers;
	
	public GameRoom(byte id){
		this.curPlayers = 0;
		this.maxPlayers = Settings.MAX_PLAYERS;
		this.roomID = id;
	}

	@Override
	public String toString() {
		return "GameRoom#"+ roomID + "{ maxPlayers=" + maxPlayers + ", curPlayers=" + curPlayers + "}";
	}
	
	
	
	
}
