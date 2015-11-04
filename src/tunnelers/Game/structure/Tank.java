package tunnelers.Game.structure;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class Tank implements GameEntity{

    
    public static int MAX_HITPOINTS = 20,
            MAX_ENERGY = 120;
    
    private Direction direction;
    private Point2D location;
    private Player player;
    
    protected double HitPoints,
                     EnergyStatus;
    
    public Tank(Player player, Point2D initialLocation){
        this.direction = Direction.North;
        this.player = player;
        this.location = initialLocation;
        this.HitPoints = MAX_HITPOINTS;
        this.EnergyStatus = MAX_ENERGY;
    }
    
    public Color getColor(){
        return this.player.getColor();
    }
    
    public void changeDirection(Direction d){
        this.direction = d;
    }
    
    @Override
    public int update() {
        
        return 0;
    }

    Point2D getLocation() {
        return this.location;
    }
    
    void setLocation(Point2D loc){
        this.location = loc;
    }
    
}
