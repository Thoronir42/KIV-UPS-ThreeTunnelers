package tunnelers.Game.structure;

/**
 *
 * @author Stepan
 */
public class Container {
    
    private Player[] players;
    private TunnelMap map;
    
    public Container(){
        
    }
    
    public int getPlayerCount(){
        return players.length;
    }
    
}
