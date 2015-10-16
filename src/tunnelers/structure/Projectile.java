package tunnelers.structure;

import java.awt.Point;

/**
 *
 * @author Stepan
 */
public class Projectile implements GameObject{
    
    private Point location;
    private Direction direction;
    private Player player;
    
    public Projectile(Point location, Direction direction, Player player){
        this.location = location;
        this.direction = direction;
        this.player = player;
    }
            
    
    @Override
    public int update() {
        return 0;
    }
    
}
