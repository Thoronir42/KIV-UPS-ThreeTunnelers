package tunnelers.Menu;

import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class GameRoom {

	static final protected byte 
			FLAG_RUNNING		= Byte.parseByte("00000001", 2),
			FLAG_SPECTATABLE	= Byte.parseByte("00000010", 2),
			FLAG_FULL			= Byte.parseByte("00000100", 2);
	
	
	static GameRoom fromString(String l) {
		byte id = Byte.parseByte(l.substring(0, 2), 16),
			maxPlayers = Byte.parseByte(l.substring(2, 4), 16),
			curPlayers = Byte.parseByte(l.substring(4, 6), 16),
			flags = Byte.parseByte(l.substring(6,8), 16);
		return new GameRoom(id, maxPlayers, curPlayers, flags);
	}
	byte roomID;
	byte maxPlayers;
	byte curPlayers;
	byte flags;
	
	public GameRoom(byte id){
		this(id, (byte)Settings.MAX_PLAYERS, (byte)0);
	}
	public GameRoom(byte id, byte maxPlayers, byte curPlayers){
		this(id, maxPlayers, curPlayers, (byte)0);
	}
	public GameRoom(byte id, byte maxPlayers, byte curPlayers, byte flags){
		this.roomID = id;
		this.curPlayers = curPlayers;
		this.maxPlayers = maxPlayers;
		this.flags = flags;
	}

	@Override
	public String toString() {
		return String.format("GameRoom#%d {PlrMax=%d, PlrCur=%d, flags=%d}", roomID, maxPlayers, curPlayers, flags);
	}
	
	
	
	
}
