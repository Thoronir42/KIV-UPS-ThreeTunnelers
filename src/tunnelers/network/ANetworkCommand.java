package tunnelers.network;

/**
 *
 * @author Stepan
 */
public abstract class ANetworkCommand {
    
    protected char COMMAND_SPLIT = '|';
    
    
    public abstract String getCommandCode();
    
}
