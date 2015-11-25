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
	static NetCommand parse(String msg) throws NetworksException{
		String[] segments = msg.split(COMMAND_SPLIT);
		if(segments.length < 2){ return null; }
		Class cmdClass = getCmdClass(segments[0].charAt(0), segments[1]);
		Object[] params = getCmdParams(segments);
		if(cmdClass != null){
			try {
				NetCommand nc = (NetCommand)cmdClass.newInstance();
				nc.setParams(params);
				return nc;
				
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new NetworksException(ex.getMessage());
			}
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
		Class[] receivableClasses = group.getDeclaredClasses();
		for(Class c : receivableClasses){
			if(c.getSimpleName().equals("Receivable")){ continue; }
			try{
				Field cmdField = c.getDeclaredField("CMD_TYPE");
				if(cmdField.get(null).equals(command)){
					return c;
				}
			} catch (NoSuchFieldException | IllegalAccessException e){
				System.err.println(e.toString()+" on "+c.getSimpleName());
			}
		}
		
		
		return null;
	}

	private static Object[] getCmdParams(String[] segments) {
		if(segments.length <= 2){
			return new Object[0];
		}
		Object[] params = new Object[segments.length -2];
		for(int i = 2; i < segments.length; i++){
			params[i-2] = segments[i];
		}
		return params;
	}
	
	final char aspectLetter;
	final String commandType;
    final Object[] params;
	
	public NetCommand(char aspectLetter, String commandType){
		this(aspectLetter, commandType, new Object[0]);
	}
	
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

	private void setParams(Object[] params) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}