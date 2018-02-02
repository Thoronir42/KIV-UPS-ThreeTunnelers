package temp;

import generic.RNG;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;
import tunnelers.core.settings.Settings;
import tunnelers.network.NetClient;

public class Mock {

	private static final RNG REMOTE_CONTROLS_RNG = new RNG(37);

	private static Controls[] MOCKED_CONTROLS;

	public static GameRoom gameRoom(AControlsManager csmgr, PlayerColorManager colors) {
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
		int projectileCapacity = playerCapacity * 20;

		GameRoom c = new GameRoom(3, playerCapacity, chatCapacity, projectileCapacity);

		for (int i = 1; i <= 4; i++) {
			c.setPlayer(i, players[i - 1]);
		}

		return c;
	}

	public static void controls(long seed) {
		InputAction[] inputs = InputAction.values();

		for (Controls controls : MOCKED_CONTROLS) {
			Mock.REMOTE_CONTROLS_RNG.setSeed(seed * 7 + controls.getID() * 11);
			for (InputAction ia : inputs) {
				controls.set(ia, REMOTE_CONTROLS_RNG.nextBoolean());
			}
		}
	}

	public static String serverListString(int n) {
		StringBuilder sb = new StringBuilder(String.format("%02X", n));
		for (byte i = 0; i < n; i++) {
			int players = RNG.getRandInt(Settings.MAX_PLAYERS) + 1;
			int gamemode = IGameRoomInfo.GAMEMODE_FFA;
			byte flags = 0;
			if (i % 2 == 0) {
				flags |= IGameRoomInfo.FLAG_RUNNING;
			}
			if (i % 3 == 1) {
				flags |= IGameRoomInfo.FLAG_SPECTATABLE;
			}

			String s = String.format("%04X%02X%02X%02X%02X", i, Settings.MAX_PLAYERS, players, gamemode, flags);
			sb.append(s);
		}

		return sb.toString();
	}
}
