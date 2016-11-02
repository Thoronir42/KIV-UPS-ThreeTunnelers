package tunnelers.network.command;

import generic.SimpleScanner;
import java.util.HashMap;
import tunnelers.network.CommandNotRecognisedException;
import tunnelers.network.NetworksException;

/**
 *
 * @author Stepan
 */
public class CommandParser {

	private static final int PARSE_RADIX = 16;

	private final SimpleScanner sc = new SimpleScanner(PARSE_RADIX);
	private static final HashMap<Short, CommandType> typeMap;
	
	static {
		typeMap = new HashMap<>();
		for (CommandType type : CommandType.values()) {
			typeMap.put(type.value(), type);
		}
	}

	public String parse(Command cmd) {
		String str = String.format("%02X%04X%04X%s\n", cmd.getId(), cmd.getType().value(), cmd.getLength(), cmd.getData());
		return str;
	}

	public Command parse(String str) throws NetworksException, NumberFormatException {
		sc.setSourceString(str);

		short id = sc.nextByte();
		short type = sc.nextShort();
		int length = sc.nextInt();
		String data = sc.readToEnd();
		
		System.out.format("MSG: %d, %d[%d] %s%n", id, type, length, data);

		CommandType cmdType = typeMap.getOrDefault(type, CommandType.Undefined);
		if (cmdType == CommandType.Undefined) {
			throw new CommandNotRecognisedException(str);
		}

		return new Command(cmdType, id, data);
	}
}
