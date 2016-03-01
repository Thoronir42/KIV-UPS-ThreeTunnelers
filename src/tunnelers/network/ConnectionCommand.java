package tunnelers.network;

/**
 *
 * @author Stepan
 */
public abstract class ConnectionCommand extends NCG {

	public static final short[] CMD_RANGE = {10, 99};

	public static boolean commandBelongs(short t) {
		return t > CMD_RANGE[0] && t < CMD_RANGE[1];
	}

	public static class FetchLobbies extends NetCommand {

		public final static short CMD_NUM = 10;

		public FetchLobbies(int versionNum) {
			super(CMD_NUM, new Object[]{versionNum});
		}
	}

	public static class CreateLobby extends NetCommand {

		public final static short CMD_NUM = 11;

		public CreateLobby(int clientVersion) {
			super(CMD_NUM, new Object[]{clientVersion});
		}
	}

	public static class JoinLobby extends NetCommand {

		public final static short CMD_NUM = 12;

		public JoinLobby(int clientVersion, int lobbyNum) {
			super(CMD_NUM, new Object[]{clientVersion, lobbyNum});
		}
	}

	public static class Reconnect extends NetCommand {

		public static final short CMD_NUM = 13;

		public Reconnect(int lobbyRoom) {
			super(CMD_NUM, new Object[]{lobbyRoom});
		}
	}

	public static class Disconnect extends NetCommand {

		public final static short CMD_NUM = 49;

		public Disconnect() {
			this(0);
		}

		public Disconnect(int disconnectReason) {
			super(CMD_NUM, new Object[]{disconnectReason});
		}
	}

	//			###		RECEIVABLE		###
	public static class IncorrectPhase extends NetCommand {

		public static final short CMD_NUM = 80;

		public IncorrectPhase(int correctPhase) {
			super(CMD_NUM, new Object[]{correctPhase});
		}
	}

	public static class GameRoomList extends NetCommand {

		public static final short CMD_NUM = 90;

		public GameRoomList(int games, String gamesInfo) {
			super(CMD_NUM, new Object[]{games, gamesInfo});
		}
	}

	public static class GameRoomListEnd extends NetCommand {

		public static final short CMD_NUM = 91;

		public GameRoomListEnd() {
			super(CMD_NUM);
		}
	}

	public static class PlayerDisconnected extends NetCommand {

		public static short CMD_NUM = 99;

		public PlayerDisconnected(int playerId, String reason) {
			super(CMD_NUM, new Object[]{playerId, reason});
		}
	}
}
