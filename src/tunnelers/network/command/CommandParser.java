package tunnelers.network.command;

/**
 *
 * @author Stepan
 */
public class CommandParser {
	public String parse(Command cmd){
		return "";
	}
	
	public Command parse(String str){
		int room = Integer.parseInt(str.substring(0, 2), 16),
					MID = Integer.parseInt(str.substring(2, 4), 16),
					mType = Integer.parseInt(str.substring(4, 8), 16);
			String body = str.substring(8);
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
}
