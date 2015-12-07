package tunnelers.Game.structure;

import javafx.geometry.Point2D;

/**
 *
 * @author Stepan
 */
public class Container {
    public static final int SERVER_PLAYER_ID = -1;
	
    public static Container mockContainer(){
        Player[] players = new Player[]{
            new Player("Yahoo"),new Player("Yahoo"),
        };
        TunnelMap map = TunnelMap.getMockMap();
        for(Player p : players){
            Point2D baseCenter = map.getFreeBaseSpot(p);
            Tank t = new Tank(p, baseCenter);
            p.setTank(t);
        }
        Container c = new Container(players, map);
        
        return c;
    }
	
    private final PlayerSrv playerSrv;
    private final Player[] players;
    private final TunnelMap map;
    
    public Container(Player[] players, TunnelMap map){
		this.playerSrv = new PlayerSrv();
        this.players = players;
        this.map = map;
    }
    
    public int getPlayerCount(){
        return players.length;
    }
    
    public Player[] getPlayers(){
        return this.players;
    }

	public TunnelMap getMap(){
		return this.map;
	}
	
    public int getMapWidth(){
        return this.map.mapWidth;
    }
    public int getMapHeight(){
        return this.map.mapHeight;
    }

	public Player getPlayer(int playerId) {
		if(playerId == SERVER_PLAYER_ID)
			return this.playerSrv;
		
		for(Player p : this.players){
			if(p.getID() == playerId)
				return p;
		}
		return null;
	}
    
}
