package tunnelers.core.engine;

import generic.SimpleScanner;
import generic.SimpleScannerException;
import tunnelers.core.gameRoom.GameRoomFacade;

public class GameRoomParser {

	public static final int LOBBY_STRING_LENGTH = 12;

	private final SimpleScanner wholeScanner;
	private final SimpleScanner singleScanner;

	public GameRoomParser() {
		this.wholeScanner = new SimpleScanner(SimpleScanner.RADIX_HEXADECIMAL);
		this.singleScanner = new SimpleScanner(SimpleScanner.RADIX_HEXADECIMAL);
	}

	public GameRoomFacade[] parse(String lobbies) throws NumberFormatException, SimpleScannerException {
		this.wholeScanner.setSourceString(lobbies);
		int n = wholeScanner.nextByte();

		GameRoomFacade[] facades = new GameRoomFacade[n];
		for (int i = 0; i < n; i++) {
			if (wholeScanner.remainingLength() < LOBBY_STRING_LENGTH) {
				System.err.format("Lobby string invalid (%s)\n", wholeScanner.readToEnd());
				break;
			}
			facades[i] = this.parseOne(this.wholeScanner.read(LOBBY_STRING_LENGTH));
		}

		return facades;
	}

	private GameRoomFacade parseOne(String gameRoomString) throws SimpleScannerException {
		this.singleScanner.setSourceString(gameRoomString);

		short id = this.singleScanner.nextShort();
		short maxPlayers = this.singleScanner.nextByte();
		short curPlayers = this.singleScanner.nextByte();
		short difficulty = this.singleScanner.nextByte();
		short flags = this.singleScanner.nextByte();

		return new GameRoomFacade(id, (byte) maxPlayers, (byte) curPlayers, (byte) difficulty, (byte) flags);
	}
}
