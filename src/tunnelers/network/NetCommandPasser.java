package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class NetCommandPasser implements Runnable{

    private NetCommand cmd;

    public NetCommand getMessage() {
        return cmd;
    }
    
    public void run() {
        throw new UnsupportedOperationException("Command hasn't been passed: no uzuý");
    }
    
    public void passCommand(NetCommand cmd){
        this.cmd = cmd;
        this.run();
    }
    
}
