package tunnelers.network;

import java.util.HashMap;

/**
 *
 * @author Stepan
 */
public class NetworkCommandManager {
	
	private final HashMap<Short, NCG.NetCommand> commands;
	
	public NetworkCommandManager(){
		commands = initCommands();
	}
	
	public NCG.NetCommand parseCommand(String message) throws NetworksException {
		short type = 1;
		NCG.NetCommand command = getCommandByType(type);
		
		
		
		return command;
	}
	
	private NCG.NetCommand getCommandByType(Short type) throws NetworksException{
		if(commands.containsKey(type)){
			return commands.get(type);
		}
		throw new NetworksException("Command type was not recognised " + type);
	}

	private HashMap<Short, NCG.NetCommand> initCommands() {
		HashMap<Short, NCG.NetCommand> map = new HashMap<>();
		
		
		
		return map;
				
	}
}
