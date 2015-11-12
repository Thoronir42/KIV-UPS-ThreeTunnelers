package tunnelers.network;

/**
 *
 * @author Stepan
 */
public abstract class NetCommand {
    
    protected static final char COMMAND_SPLIT = '|';
    
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
