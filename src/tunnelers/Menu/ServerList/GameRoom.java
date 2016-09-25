package tunnelers.Menu.ServerList;

import tunnelers.Menu.ServerList.GameRoomView.GRTVItem;
import java.util.Random;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import tunnelers.Settings.Settings;

/**
 *
 * @author Stepan
 */
public class GameRoom implements GRTVItem {

	public static final byte FLAG_RUNNING = Byte.parseByte("00000001", 2),
			//FLAG_SPECTATABLE = Byte.parseByte("00000010", 2),
			FLAG_SPECTATABLE = Byte.parseByte("00000000", 2),
			FLAG_FULL = Byte.parseByte("00000100", 2);
	
	public static GameRoom fromString(String l) {
		byte id = Byte.parseByte(l.substring(0, 2), 16),
				maxPlayers = Byte.parseByte(l.substring(2, 4), 16),
				curPlayers = Byte.parseByte(l.substring(4, 6), 16),
				flags = Byte.parseByte(l.substring(6, 8), 16);
		RoomDifficulty difficulty = getRandDifficulty();
		return new GameRoom(id, maxPlayers, curPlayers, flags, difficulty);
	}

	private static RoomDifficulty getRandDifficulty() {
		Random rand = new Random();
		RoomDifficulty[] vals = RoomDifficulty.values();
		RoomDifficulty d = vals[rand.nextInt(vals.length)];
		return d;

	}
	byte roomID;
	byte maxPlayers;
	byte curPlayers;
	byte flags;
	RoomDifficulty difficulty;
	
	public final ReadOnlyBooleanProperty Running;
	public final ReadOnlyBooleanProperty Spectable;
	public final ReadOnlyBooleanProperty Full;

	public GameRoom(byte id) {
		this(id, (byte) Settings.MAX_PLAYERS, (byte) 0);
	}

	public GameRoom(byte id, byte maxPlayers, byte curPlayers) {
		this(id, maxPlayers, curPlayers, (byte) 0, RoomDifficulty.Unspecified);
	}

	public GameRoom(byte id, byte maxPlayers, byte curPlayers, byte flags, RoomDifficulty difficulty) {
		this.roomID = id;
		this.curPlayers = curPlayers;
		this.maxPlayers = maxPlayers;
		this.flags = flags;
		this.difficulty = difficulty;
		
		this.Running = createFlagProperty(FLAG_RUNNING);
		this.Full = createFlagProperty(FLAG_FULL);
		this.Spectable = createFlagProperty(FLAG_SPECTATABLE);
		
	}

	@Override
	public String toString() {
		return String.format("GameRoom#%d {PlrMax=%d, PlrCur=%d, flags=%d}", roomID, maxPlayers, curPlayers, flags);
	}

	@Override
	public RoomDifficulty getDIfficulty() {
		return this.difficulty;
	}

	@Override
	public String getOccupancy() {
		return String.format("%d / %d", this.curPlayers, this.maxPlayers);
	}

	@Override
	public String getTitle() {
		return "Místnost #" + this.roomID;
	}

	@Override
	public String getFlags() {
		boolean first = true;
		String result = "";
		if(this.Full.get()){
			result += "Plná";
			first = false;
		}
		if(this.Running.get()){
			if(!first){
				result += ", ";
			}
			result += "Právě běží";
		}
		if(this.Spectable.get()){
			if(!first){
				result += ", ";
			}
			result += "Je pozorovatelná";
		}
		
		return result;
	}
	
	
	
	private ReadOnlyBooleanWrapper createFlagProperty(int flag){
		return new ReadOnlyBooleanWrapper()
		{
			@Override
			public boolean get() {
				return (flags & flag) > 0;
			}
		};
	}
}
