package tunnelers.Game.structure;

import javafx.geometry.Point2D;



/**
 *
 * @author Stepan
 */
public class Projectile implements GameEntity{
    
    private Point2D location;
    private Direction direction;
    private Player player;
    
    public Projectile(Point2D location, Direction direction, Player player){
        this.location = location;
        this.direction = direction;
        this.player = player;
    }
            
    
    @Override
    public int update() {
        return 0;
    }
    
}
