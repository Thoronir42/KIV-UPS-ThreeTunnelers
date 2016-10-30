package temp;

import generic.RNG;
import tunnelers.app.controls.ControlsManager;
import tunnelers.core.gameRoom.GameContainer;
import tunnelers.core.player.Controls;
import tunnelers.core.player.InputAction;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Mock {

	public static final RNG REMOTE_CONTROLS_RNG = new RNG(37);

	private static Controls[] MOCKED_CONTROLS;

	public static GameContainer gameContainer(ControlsManager csmgr, String localName, int maxColorId) {
		MOCKED_CONTROLS = new Controls[]{
			new Controls(2),
			new Controls(3),
			new Controls(4),
		};

		Player[] players = new Player[]{
			new Player(47, RNG.getRandInt(maxColorId), csmgr.getKeyboardScheme((byte) 0), localName),
			new Player(53, RNG.getRandInt(maxColorId), csmgr.getKeyboardScheme((byte) 1), "Jouda"),
			new Player(13, RNG.getRandInt(maxColorId), MOCKED_CONTROLS[0], "Frederick"),
			new Player(12, RNG.getRandInt(maxColorId), MOCKED_CONTROLS[1], "Frederick"),
			new Player(17, RNG.getRandInt(maxColorId), MOCKED_CONTROLS[2], "Frederick"),
		};

		GameContainer c = new GameContainer(players);

		return c;
	}

	public static void controls(long seed) {
		InputAction[] inputs = InputAction.values();

		for (Controls controls : MOCKED_CONTROLS) {
			Mock.REMOTE_CONTROLS_RNG.setSeed(seed + controls.getID());
			for (InputAction ia : inputs) {
				controls.setControlState(ia, REMOTE_CONTROLS_RNG.nextBoolean());
			}
		}

	}
}
