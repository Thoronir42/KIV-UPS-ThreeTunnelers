package tunnelers.network.command;

import generic.SimpleScanner;
import java.util.HashMap;
import tunnelers.network.CommandNotRecognisedException;

/**
 *
 * @author Stepan
 */
public class CommandParser {

	private final SimpleScanner sc = new SimpleScanner(SimpleScanner.RADIX_HEXADECIMAL);
	private static final HashMap<Short, CommandType> TYPE_MAP;

	static {
		TYPE_MAP = new HashMap<>();
		for (CommandType type : CommandType.values()) {
			TYPE_MAP.put(type.value(), type);
		}
	}

	public String parse(Command cmd) {
		//String str = String.format("%02X%04X%04X%s\n", cmd.getId(), cmd.getType().value(), cmd.getLength(), cmd.getData());
		return String.format("%04X%s\n", cmd.getType().value(), cmd.getData());
	}

	public Command parse(String str) throws CommandNotRecognisedException {
		//short id;
		short type;
		//short length;
		String body;

		sc.setSourceString(str);

		try {
			//id = sc.nextByte();
			type = sc.nextShort();
			//length = sc.nextInt();
			body = sc.readToEnd();
		} catch (NumberFormatException ex) {
			throw new CommandNotRecognisedException(str, ex.getMessage());
		} catch (StringIndexOutOfBoundsException ex){
			throw new CommandNotRecognisedException(str, ex.getMessage());
			// asdf
		}

		CommandType cmdType = TYPE_MAP.getOrDefault(type, CommandType.Undefined);
		if (cmdType == CommandType.Undefined) {
			throw new CommandNotRecognisedException(str);
		}

		return new Command(cmdType, body);
	}
}
