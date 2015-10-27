package tunnelers.Game.structure;



import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class Tank implements GameEntity{

    private final Color tankColor;
    private Direction direction;
    private Point2D location;
    private Player player;
    
    protected int HitPoints;
    protected int EnergyStatus;
    
    public Tank(Player player, Point2D initialLocation){
        this.direction = Direction.North;
        this.player = player;
        this.tankColor = player.getColor();
        this.location = initialLocation;
        
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
    
}
