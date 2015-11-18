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
    private final Control controlScheme;
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
        this.controlScheme = new Control();
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public final void setColor(int colorId){
        this.color = settings.getColor(this.color, colorId);
    }
    
    public void handleControl(Control control){
        
    }

    public String getName() {
        return this.name;
    }
    
	public int getID(){
		return this.playerID;
	}
	
    @Override
    public String toString(){
        return String.format("[%2d] %16s (%s)", this.playerID, this.name, this.color);
    }

    public Point2D getLocation() {
        return this.tank.getLocation();
    }
    
    public void setLocation(Point2D loc) {
        this.tank.setLocation(loc);
        System.out.println(name + " new location set: " + loc.toString());
    }

    public double getEnergyPct(){
        return this.tank.EnergyStatus / Tank.MAX_ENERGY;
    }
    
    void setTank(Tank t) {
        this.tank = t;
    }
	
	Tank getTank(){
		return this.tank;
	}
}


class PlayerException extends Exception{
    public PlayerException(int n){
        super(String.format("Tried to create %d. player, while the player limit is %d.", n, Settings.MAX_PLAYERS));
    }
}