package tunnelers.core.engine;

import tunnelers.network.command.CommandType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandMap extends HashMap<CommandType, IAction> {

	public void printUnimplementedActions(PrintStream stream) {
		int n = 0;
		List<String> missing = new ArrayList<>();

		for (CommandType type : CommandType.values()) {
			if (type == CommandType.Undefined || this.containsKey(type)) {
				continue;
			}

			missing.add(((++n % 8 == 0) ? "\n" : "") + type.toString());
		}

		if (missing.size() > 0) {
			stream.println("Engine does not implement handling of these "
					+ "command types: " + String.join(", ", missing));
		}
	}
}
