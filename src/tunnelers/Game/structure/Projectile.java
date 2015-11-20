package tunnelers.Game.structure;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;



/**
 *
 * @author Stepan
 */
public class Projectile extends GameEntity{
	
	private static final Dimension2D SHOT_HORIZONTAL = new Dimension2D(3, 1),
									SHOT_DIAGONAL = new Dimension2D(3, 3),
									SHOT_VERTICAL = new Dimension2D(1, 3);
    
    public Projectile(Point2D location, Direction direction, Player player){
        super(direction, location, player);
    }
            
    
    @Override
    public int update() {
        return 0;
    }

	@Override
	public Dimension2D getSize() {
		switch(this.direction){
			default:
				return SHOT_DIAGONAL;
			case North: case South:
				return SHOT_VERTICAL;
			case East: case West:
				return SHOT_HORIZONTAL;
				
		}
	}
    
}
