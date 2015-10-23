package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class LobbyCommand extends ANetworkCommand{

    protected static final char AREA_LETTER = 'L';
    
    Action commandAction;
    Object[] params;
    
    public LobbyCommand(Action a, Object[] params){
        this.commandAction = a;
        this.params = params;
    }
    
    @Override
    public String getCommandCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getAreaLetter()).append(COMMAND_SPLIT).append(this.getCommandHandle());
        for(Object o : params){
            sb.append(COMMAND_SPLIT).append(o.toString());
        }
        return sb.toString();
    }
    
    protected char getAreaLetter(){
        return AREA_LETTER;
    }
    
    protected String getCommandHandle(){
        return this.commandAction.handle;
    }
    
    public enum Action{
        Join("U_THERE", 1),
        Disconnect("I_MUST_GO", 0),
        Kick("SEE_YA", 1),
        ChangeColor("IM_BLU", 1),
        IAm("I_AM", 1),
        WhoIs("WHO_IS", 1),
        Start("LETS_GO", 0),
        
        WhoAreYou("WHO_R_U", 0),
        PlayerJoined("OTHR_HELLO", 2),
        PlayerDisconnected("OTHR_BYE", 1),
        PlayerIs("THEY_ARE", 2),
        GameStarted("ITS_ON", 0),
        ;
        
        private String handle;
        private int paramCount;
        
        private Action(String handle, int paramCount){
            this.handle = handle;
            this.paramCount = paramCount;
        }
        
        public boolean paramsOk(int n){
            return n == this.paramCount;
        }
    }
    
}
