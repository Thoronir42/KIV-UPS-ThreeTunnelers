package Mock;

import generic.RNG;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;
import tunnelers.network.NetClient;

public class GameRoomMock {

	private static final RNG REMOTE_CONTROLS_RNG = new RNG(37);
	private static Controls[] MOCKED_CONTROLS;

	public static GameRoom create(AControlsManager csmgr, PlayerColorManager colors) {
		MOCKED_CONTROLS = new Controls[]{
				new Controls(2),
				new Controls(4),};

		NetClient[] clients = {
				new NetClient("Karel"),
				new NetClient("Frederick"),
				new NetClient("Obama"),};

		Controls[] playerControls = csmgr.getAllSchemes();

		Player[] players = new Player[]{
				new Player(clients[0], colors.useRandomColor().intValue(), playerControls[0]),
				null,
				new Player(clients[1], colors.useRandomColor().intValue(), MOCKED_CONTROLS[0]),
				new Player(clients[2], colors.useRandomColor().intValue(), MOCKED_CONTROLS[1])
		};

		int playerCapacity = 4;
		int chatCapacity = 20;

		GameRoom c = new GameRoom(3, playerCapacity, chatCapacity);

		for (int i = 0; i < 4; i++) {
			c.setPlayer(i, players[i]);
		}

		return c;
	}

	public static void controls(long seed) {
		InputAction[] inputs = InputAction.values();
		for (Controls controls : MOCKED_CONTROLS) {
			REMOTE_CONTROLS_RNG.setSeed(seed * 7 + controls.getID() * 11);
			for (InputAction ia : inputs) {
				controls.set(ia, REMOTE_CONTROLS_RNG.nextBoolean());
			}
		}
	}
}
