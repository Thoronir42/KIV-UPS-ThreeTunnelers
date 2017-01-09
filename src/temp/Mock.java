package temp;

import generic.RNG;
import java.util.ArrayList;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.settings.Settings;
import tunnelers.network.NetClient;

/**
 *
 * @author Stepan
 */
public class Mock {

	public static final RNG REMOTE_CONTROLS_RNG = new RNG(37);

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
		int iPlayer = 0;
		ArrayList<Player> players = new ArrayList<>();

		for (Controls c : playerControls) {
			players.add(new Player(iPlayer++, colors.useRandomColor().intValue(), clients[0], c));
		}

		players.add(new Player(iPlayer++, colors.useRandomColor().intValue(), clients[1], MOCKED_CONTROLS[0]));
		players.add(new Player(iPlayer++, colors.useRandomColor().intValue(), clients[2], MOCKED_CONTROLS[1]));

		GameRoom c = new GameRoom(players.size());

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
	
	public static String serverListString(int n){
		return serverListString(n, 0);
	}

	public static String serverListString(int n, int remaining) {
		StringBuilder sb = new StringBuilder(String.format("%02X%02X", n, remaining));
		for (byte i = 0; i < n; i++) {
			int players = RNG.getRandInt(Settings.MAX_PLAYERS) + 1;
			int difficulty = RNG.getRandInt(4);
			byte flags = 0;
			if (i % 2 == 0) {
				flags |= IGameRoomInfo.FLAG_RUNNING;
			}
			if (i % 3 == 1) {
				flags |= IGameRoomInfo.FLAG_SPECTATABLE;
			}
			
			String s = String.format("%04X%02X%02X%02X%02X", i, Settings.MAX_PLAYERS, players, difficulty, flags);
			sb.append(s);
		}
		
		return sb.toString();
	}
}
