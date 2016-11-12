package tunnelers.network.command;

/**
 *
 * @author Stepan
 */
public class Command {

	/**
	 * fixme: unused
	 */
	protected short id;
	protected final CommandType type;
	protected String data;

	public Command(CommandType type) {
		this(type, "");
	}

	public Command(CommandType type, String data) {
		this.type = type;
		this.data = data;
	}

	public CommandType getType() {
		return this.type;
	}

	public short getId() {
		return id;
	}

	/**
	 * Returns length of current data
	 *
	 * @return length of data
	 */
	public short getLength() {
		return (short) data.length();
	}

	protected void append(byte n) {
		this.data += String.format("%02X", n);
	}

	protected void append(short n) {
		this.data += String.format("%04X", n);
	}

	protected void append(int n) {
		this.data += String.format("%08X", n);
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("Cmd[%03d]: %05d %05d: %s ", this.id, this.type.value(), this.getLength(), this.data);
	}
}
