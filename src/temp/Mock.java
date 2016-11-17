package temp;

import generic.RNG;
import tunnelers.app.controls.ControlsManager;
import tunnelers.core.gameRoom.GameContainer;
import tunnelers.core.player.Controls;
import tunnelers.core.player.InputAction;
import tunnelers.core.player.Player;
import tunnelers.network.NetClient;

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
			new Controls(4),};

		NetClient[] clients = {
			new NetClient(localName),
			new NetClient("Frederick"),
			new NetClient("Obama"),};

		Player[] players = new Player[]{
			new Player(0, RNG.getRandInt(maxColorId), clients[0], csmgr.getKeyboardScheme((byte) 0)),
			new Player(1, RNG.getRandInt(maxColorId), clients[0], csmgr.getKeyboardScheme((byte) 1)),
			new Player(2, RNG.getRandInt(maxColorId), clients[1], MOCKED_CONTROLS[0]),
			new Player(3, RNG.getRandInt(maxColorId), clients[2], MOCKED_CONTROLS[1]),};

		GameContainer c = new GameContainer(players.length);

		for(int i = 0; i < players.length; i++){
			c.setPlayer(players[i].getID(), players[i]);
		}

		return c;
	}

	public static void controls(long seed) {
		InputAction[] inputs = InputAction.values();

		for (Controls controls : MOCKED_CONTROLS) {
			Mock.REMOTE_CONTROLS_RNG.setSeed(seed * 7 + controls.getID() * 11);
			for (InputAction ia : inputs) {
				controls.setControlState(ia, REMOTE_CONTROLS_RNG.nextBoolean());
			}
		}

	}
}
