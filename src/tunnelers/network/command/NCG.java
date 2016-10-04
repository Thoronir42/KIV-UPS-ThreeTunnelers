package tunnelers.network.command;

import java.lang.reflect.Field;
import tunnelers.network.NetworksException;

/**
 * NCG stands for Network Command Group
 *
 * @author Stepan
 */
public abstract class NCG {
	
	/**
	 *
	 * @author Stepan
	 */
	public static abstract class NetCommand {

		private static Class[] RcvCmdClasses()
		{
			return new Class[]{
				LeadCommand.class, ConnectionCommand.class,
				GameCommand.class, MessageCommand.class
			};
		}
		public static byte RoomNumber;

		public static NetCommand parse(String msg) throws NetworksException {
			int room = Integer.parseInt(msg.substring(0, 2), 16),
					MID = Integer.parseInt(msg.substring(2, 4), 16),
					mType = Integer.parseInt(msg.substring(4, 8), 16);
			String body = msg.substring(8);
			/*
			 if(cmdClass != null){
			 try {
			 NetCommand nc = (NetCommand)cmdClass.newInstance();
			 nc.setParams(params);
			 return nc;

			 } catch (InstantiationException | IllegalAccessException ex) {
			 throw new NetworksException(ex.getMessage());
			 }
			 }
			 */
			System.out.format("MSG: %d, %d, %d, %s%n", room, MID, mType, body);
			return null;
		}

		private static Class getCmdClass(short type) throws ClassNotFoundException{
			for (Class classGroup : RcvCmdClasses()) {
				try {
					Class[] classes = classGroup.getDeclaredClasses();
					for (Class c : classes) {

					}
					Field aspectField = classGroup.getDeclaredField("CMD_NUM");
					if (aspectField.get(null).equals(type)) {
						return classGroup;
					}
				} catch (NoSuchFieldException | IllegalAccessException e) {
					System.err.println(e.getMessage());
				}
			}
			throw new ClassNotFoundException("No appropriate command class was found for type " + type);
		}
		static byte LastMsgId = 0;
		final byte message_id;
		final short cmd_type;

		final Object[] params;

		public NetCommand(short cmd_type) {
			this(cmd_type, new Object[0]);
		}

		public NetCommand(byte message_id, short cmd_type) {
			this(message_id, cmd_type, new Object[0]);
		}

		public NetCommand(short cmd_type, Object[] params) {
			this(++LastMsgId, cmd_type, params);
		}

		public NetCommand(byte message_id, short cmd_type, Object[] params) {
			this.message_id = message_id;
			this.cmd_type = cmd_type;
			this.params = params;
		}

		public String getCommandCode() {
			StringBuilder sb = new StringBuilder();
			sb.append(bts(RoomNumber))
					.append(bts(this.message_id))
					.append(sts(this.cmd_type));
			sb.append("\0");
			return sb.toString();
		}

		public boolean isLeadCommand() {
			return LeadCommand.commandBelongs(this.cmd_type);
		}

		public boolean isConnectionCommand() {
			return ConnectionCommand.commandBelongs(this.cmd_type);
		}

		public boolean isMessageCommand() {
			return MessageCommand.commandBelongs(this.cmd_type);
		}

		public boolean isLobbyCommand() {
			return LobbyCommand.commandBelongs(this.cmd_type);
		}

		public boolean isGameCommand() {
			return GameCommand.commandBelongs(this.cmd_type);
		}

		protected String bts(byte n) {
			return String.format("%02X", n);
		}

		protected String sts(short n) {
			return String.format("%04X", n);
		}

		protected String its(int n) {
			return String.format("%08X", n);
		}

		protected void setParamsFromString(String body) {
			
		}

	}
}
