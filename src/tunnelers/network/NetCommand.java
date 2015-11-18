package tunnelers.network;

import java.lang.reflect.Field;

/**
 *
 * @author Stepan
 */
public abstract class NetCommand {
    
    public static final String COMMAND_SPLIT = "|";
	private static final Class[] RcvCmdClasses = {
		ConnectionCommand.class,
		GameCommand.class,
		MessageCommand.class
		
	};
	static NetCommand parse(String msg){
		String[] segments = msg.split(COMMAND_SPLIT);
		if(segments.length < 2){ return null; }
		Class command = getCmdClass(segments[0].charAt(0), segments[1]);
		if(command != null){
			System.out.println(command.toString());
		}
		return null;
	}
    
	private static Class getCmdGroup(char aspect){
		for(Class c : RcvCmdClasses){
			try{
				Field aspectField = c.getDeclaredField("ASPECT_LETTER");
				if(aspectField.get(null).equals(aspect)){
					return c;
				}
			} catch (NoSuchFieldException | IllegalAccessException e){
				System.err.println(e.getMessage());
			}
		}
		
		
		return null;
	}
	
	private static Class getCmdClass(char aspect, String command){
		Class group = getCmdGroup(aspect);
		Class[] recievableClasses = group.getDeclaredClasses();
		for(Class c : recievableClasses){
			try{
				Field cmdField = c.getDeclaredField("CMD_TYPE");
				if(cmdField.get(null).equals(command)){
					return c;
				}
			} catch (NoSuchFieldException | IllegalAccessException e){
				System.err.println(e.getMessage());
			}
		}
		
		
		return null;
	}
	
	final char aspectLetter;
	final String commandType;
    final Object[] params;
	
	public NetCommand(char aspectLetter, String commandType, Object[] params){
		this.aspectLetter = aspectLetter;
		this.commandType = commandType;
		this.params = params;
	}
	
    public String getCommandCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.aspectLetter)
				.append(COMMAND_SPLIT)
				.append(this.commandType);
		
        for(Object o : params){
            sb.append(COMMAND_SPLIT).append(o.toString());
        }
        return sb.toString();
    }
	
}