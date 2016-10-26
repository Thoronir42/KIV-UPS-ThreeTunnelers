package tunnelers.network.command;

/**
 *
 * @author Stepan
 */
public class Command {

	
	protected short length;
	protected final CommandType type;
	protected final byte messageId;
	String data;
	
	public Command(CommandType type, byte messageId){
		this.type = type;
		this.messageId = messageId;
		this.length = 0;
		this.data = "";
	}
	
	public CommandType getType() {
		return CommandType.LeadApprove;
	}
	
	
	protected String append(byte n) {
		return String.format("%02X", n);
	}

	protected String append(short n) {
		return String.format("%04X", n);
	}

	protected String append(int n) {
		return String.format("%08X", n);
	}
	
	public String getDataString(){
		return this.data;
	}
	
	public void setDataString(String data){
		this.data = data;
	}

}
