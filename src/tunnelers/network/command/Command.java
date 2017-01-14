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
	public int getLength() {
		return data.length();
	}

	public Command append(byte n) {
		return this.append(String.format("%02X", n));
	}

	public Command append(short n) {
		return this.append(String.format("%04X", n));
	}

	public Command append(int n) {
		return this.append(String.format("%08X", n));
	}
	
	public Command append(String s){
		this.data += s;
		
		return this;
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
