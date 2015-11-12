package tunnelers.Game.structure;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Stepan
 */
public class Container {
    
    public static Container mockContainer(){
        Player[] players = new Player[]{
            new Player("Yahoo"),new Player("Yahoo"),
        };
        TunnelMap map = TunnelMap.getMockMap();
        for(Player p : players){
            Point2D baseCenter = map.getFreeBaseSpot();
            Tank t = new Tank(p, baseCenter);
            p.setTank(t);
        }
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

    public int getMapWidth(){
        return this.map.mapWidth;
    }
    public int getMapHeight(){
        return this.map.mapHeight;
    }
    
    public void drawMap(GraphicsContext g,Dimension2D blockSize, Rectangle render) {
        this.map.drawMapSection(g, blockSize, render);
    }
    
}
