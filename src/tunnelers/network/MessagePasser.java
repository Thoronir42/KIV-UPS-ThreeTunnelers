package tunnelers.network;

/**
 *
 * @author Stepan
 */
public class MessagePasser implements Runnable{

    private String message = "NA";

    public String getMessage() {
        return message;
    }
    
    public void run() {
        throw new UnsupportedOperationException("Message was not run");
    }
    
    public void passMessage(String message){
        this.message = message;
        this.run();
    }
    
}
