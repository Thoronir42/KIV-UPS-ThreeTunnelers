package temp;

import generic.RNG;
import java.util.ArrayList;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.GameContainer;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.network.NetClient;

/**
 *
 * @author Stepan
 */
public class Mock {

	public static final RNG REMOTE_CONTROLS_RNG = new RNG(37);

	private static Controls[] MOCKED_CONTROLS;

	public static GameContainer gameContainer(AControlsManager csmgr, PlayerColorManager colors) {
		MOCKED_CONTROLS = new Controls[]{
			new Controls(2),
			new Controls(4),};

		NetClient[] clients = {
			new NetClient("Karel"),
			new NetClient("Frederick"),
			new NetClient("Obama"),};
		
		Controls[] playerControls = csmgr.getAllSchemes();
		int iPlayer = 0;
		ArrayList<Player> players = new ArrayList<>();

		for (Controls c : playerControls) {
			players.add(new Player(iPlayer++, colors.useRandomColor().intValue(), clients[0], c));
		}

		players.add(new Player(iPlayer++, colors.useRandomColor().intValue(), clients[1], MOCKED_CONTROLS[0]));
		players.add(new Player(iPlayer++, colors.useRandomColor().intValue(), clients[2], MOCKED_CONTROLS[1]));

		GameContainer c = new GameContainer(players.size());

		for (Player p : players) {
			c.setPlayer(p.getID(), p);
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
