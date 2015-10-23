package tunnelers.Game.structure;

/**
 *
 * @author Stepan
 */
public class Container {
    
    public static Container mockContainer(){
        Player[] players = new Player[]{
            new Player("Yahoo"),
            new Player("Yello"),
            new Player("Yoda"),
        };
        TunnelMap map = TunnelMap.getMockMap();
        Container c = new Container(players, map);
        
        return c;
    }
    
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
