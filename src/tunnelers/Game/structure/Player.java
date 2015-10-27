package tunnelers.Game.structure;

import javafx.geometry.Point2D;
import tunnelers.Settings;
import javafx.scene.paint.Color;



/**
 *
 * @author Stepan
 */
public class Player {
    
    public static final Color COL_ENERGY = Color.DEEPPINK,
                              COL_HITPOINTS = Color.LAWNGREEN;
    
    private static int num_of_instances = 0;
    private final static Settings settings = Settings.getInstance();
    
    private final int playerID;
    private final String name;
    private short activeX, activeY;
    private boolean activeShoot;
    private Color color;
    private Tank tank;
    
    public Player(String name){
    //public Player(String name) throws PlayerException{
        int newID = num_of_instances + 1;
        if(newID > Settings.MAX_PLAYERS){
            //throw new PlayerException(newID);
        }
        this.playerID = num_of_instances = newID;
        this.name = name;
        this.setColor(newID - 1);
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public void setColor(int colorId){
        this.color = settings.getColor(this.color, colorId);
    }
    
    public void handleControl(Control control){
        
    }

    public String getName() {
        return this.name;
    }
    
    public String toString(){
        return String.format("[%2d] %16s (%s)", this.playerID, this.name, this.color);
    }

    public Point2D getLocation() {
        return this.tank.getLocation();
    }
    
}


class PlayerException extends Exception{
    public PlayerException(int n){
        super(String.format("Tried to create %d. player, while the player limit is %d.", n, Settings.MAX_PLAYERS));
    }
}