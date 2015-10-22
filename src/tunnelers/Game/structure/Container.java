package tunnelers.Game.structure;

/**
 *
 * @author Stepan
 */
public class Container {
    
    private final Player[] players;
    private final TunnelMap map;
    
    public Container(Player[] players, TunnelMap map){
        this.players = players;
        this.map = map;
    }
    
    public int getPlayerCount(){
        return players.length;
    }
    
    public Player[] getPlayers(){
        return this.players;
    }
    
}
