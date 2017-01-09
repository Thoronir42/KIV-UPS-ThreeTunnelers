package tunnelers.core.engine;

import generic.SimpleScanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import temp.Mock;
import tunnelers.core.gameRoom.GameRoomFacade;

/**
 *
 * @author Skoro
 */
public class GameRoomParserTest {

	private GameRoomParser parser;

	public GameRoomParserTest() {
		parser = new GameRoomParser();
	}

	/**
	 * Test of parse method, of class GameRoomParser.
	 */
	@Test
	public void testParseOne() {
		int n = 1;
		String lobbies = "001004020103";

		GameRoomFacade[] expResult = new GameRoomFacade[]{
			new GameRoomFacade((short) 16, (byte) 4, (byte) 2, (byte) 1, (byte) 3),};

		GameRoomFacade[] result = parser.parse(n, lobbies);
		assertArrayEquals(expResult, result);
	}

	@Test
	public void testParseMultiple() {
		int n = 3;
		String lobbies = 
				"001004020103" +
				"001104010202" +
				"001204030301"
				;

		GameRoomFacade[] expResult = new GameRoomFacade[]{
			new GameRoomFacade((short) 16, (byte) 4, (byte) 2, (byte) 1, (byte) 3),
			new GameRoomFacade((short) 17, (byte) 4, (byte) 1, (byte) 2, (byte) 2),
			new GameRoomFacade((short) 18, (byte) 4, (byte) 3, (byte) 3, (byte) 1),
		};

		GameRoomFacade[] result = parser.parse(n, lobbies);
		assertArrayEquals(expResult, result);
	}

}
