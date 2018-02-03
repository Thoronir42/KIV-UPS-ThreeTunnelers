package Mock;

import generic.RNG;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.settings.Settings;

public class NetworkMock {

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
