package tunnelers.network.command;

/**
 *
 * @author Stepan
 */
public class GameCommand extends NCG {

	public static final short[] CMD_RANGE = {200, 399};

	public static boolean commandBelongs(short t) {
		return t > CMD_RANGE[0] && t < CMD_RANGE[1];
	}

	public static class ControlSet extends NetCommand {

		public static final short CMD_NUM = 201;

		public ControlSet(int ctrlType, int newVal) {
			super(CMD_NUM, new Object[]{ctrlType, newVal});
		}
	}

	public static class WhatsHere extends NetCommand {

		public static final short CMD_NUM = 210;

		public WhatsHere(int x, int y) {
			super(CMD_NUM, new Object[]{x, y});
		}
	}

	public static class ConfirmChunks extends NetCommand {

		public static final short CMD_NUM = 211;

		public ConfirmChunks(int checksum) {
			super(CMD_NUM, new Object[]{checksum});
		}
	}

	public static class SyncPlayers extends NetCommand {

		public static final short CMD_NUM = 220;

		public SyncPlayers() {
			super(CMD_NUM);
		}
	}

	public static class RemoteControlSet extends NetCommand {

		public static final short CMD_NUM = 301;

		public RemoteControlSet(int playerId, int controlId, int newVal) {
			super(CMD_NUM, new Object[]{playerId, controlId, newVal});
		}
	}

	public static class ChunkData extends NetCommand {

		public static final short CMD_NUM = 310;

		public ChunkData(String chunkData) {
			super(CMD_NUM, new Object[]{chunkData});
		}
	}

	public static class PlayerInfo extends NetCommand {

		public static final short CMD_NUM = 320;

		public PlayerInfo(int playerId, int direction, int x, int y) {
			super(CMD_NUM, new Object[]{playerId, direction, x, y});
		}
	}

}
