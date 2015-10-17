package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class LobbyCommand extends ANetworkCommand{

    protected static final char AREA_LETTER = 'L';
    
    Action commandAction;
    int param;
    
    public LobbyCommand(Action a, int param){
        this.commandAction = a;
        this.param = param;
    }
    
    @Override
    public String getCommandCode() {
        return this.getAreaLetter() + this.getCommandHandle();
    }
    
    protected char getAreaLetter(){
        return AREA_LETTER;
    }
    
    protected String getCommandHandle(){
        return this.commandAction.handle;
    }
    
    public enum Action{
        Join("U_THERE"),
        Disconnect("I_MUST_GO"),
        Kick("SEE_YA"),
        ChangeColor("IM_BLU"),
        IAm("I_AM"),
        WhoIs("WHO_IS"),
        Start("LETS_GO"),
        
        WhoAreYou("WHO_R_U"),
        PlayerJoined("OTHR_HELLO"),
        PlayerDisconnected("OTHR_BYE"),
        PlayerIs("THEY_ARE"),
        GameStarted("ITS_ON"),
        ;
        
        private String handle;
        
        private Action(String handle){
            this.handle = handle;
        }
    }
    
}
