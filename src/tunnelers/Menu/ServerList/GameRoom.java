package tunnelers.Menu.ServerList;

import java.util.Random;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class GameRoom implements GRTVItem{

	public static final byte FLAG_RUNNING = Byte.parseByte("00000001", 2),
			FLAG_SPECTATABLE = Byte.parseByte("00000010", 2),
			FLAG_FULL = Byte.parseByte("00000100", 2);

	public static GameRoom fromString(String l) {
		byte id = Byte.parseByte(l.substring(0, 2), 16),
				maxPlayers = Byte.parseByte(l.substring(2, 4), 16),
				curPlayers = Byte.parseByte(l.substring(4, 6), 16),
				flags = Byte.parseByte(l.substring(6, 8), 16);
		Difficulty difficulty = getRandDifficulty();
		return new GameRoom(id, maxPlayers, curPlayers, flags, difficulty);
	}

	private static Difficulty getRandDifficulty() {
		Random rand = new Random();
		Difficulty[] vals = Difficulty.values();
		Difficulty d = vals[rand.nextInt(vals.length)];
		System.out.println(d);
		return d;
		
	}
	byte roomID;
	byte maxPlayers;
	byte curPlayers;
	byte flags;
	Difficulty difficulty;

	public GameRoom(byte id) {
		this(id, (byte) Settings.MAX_PLAYERS, (byte) 0);
	}

	public GameRoom(byte id, byte maxPlayers, byte curPlayers) {
		this(id, maxPlayers, curPlayers, (byte) 0, Difficulty.Unspecified);
	}

	public GameRoom(byte id, byte maxPlayers, byte curPlayers, byte flags, Difficulty difficulty) {
		this.roomID = id;
		this.curPlayers = curPlayers;
		this.maxPlayers = maxPlayers;
		this.flags = flags;
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return String.format("GameRoom#%d {PlrMax=%d, PlrCur=%d, flags=%d}", roomID, maxPlayers, curPlayers, flags);
	}

	@Override
	public Difficulty getDIfficulty() {
		return this.difficulty;
	}

}
