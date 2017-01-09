package tunnelers.core.engine;

import generic.SimpleScanner;
import tunnelers.core.gameRoom.GameRoomFacade;

/**
 *
 * @author Skoro
 */
public class GameRoomParser {

	public static final int LOBBY_STRING_LENGTH = 12;
	
	private final SimpleScanner wholeScanner;
	private final SimpleScanner singleScanner;

	public GameRoomParser() {
		this.wholeScanner = new SimpleScanner();
		this.singleScanner = new SimpleScanner(16);
	}

	public GameRoomFacade[] parse(int n, String lobbies) {
		this.wholeScanner.setSourceString(lobbies);
		GameRoomFacade[] facades = new GameRoomFacade[n];
		for (int i = 0; i < n; i++) {
			facades[i] = this.parseOne(this.wholeScanner.read(LOBBY_STRING_LENGTH));
		}

		return facades;
	}

	private GameRoomFacade parseOne(String gameRoomString) {
		this.singleScanner.setSourceString(gameRoomString);

		short id = this.singleScanner.nextShort();
		short maxPlayers = this.singleScanner.nextByte();
		short curPlayers = this.singleScanner.nextByte();
		short difficulty = this.singleScanner.nextByte();
		short flags = this.singleScanner.nextByte();

		return new GameRoomFacade(id, (byte) maxPlayers, (byte) curPlayers, (byte) difficulty, (byte) flags);
	}
}
