package tunnelers.structure;

/**
 *
 * @author Stepan
 */
public class Control {
    
}
    
enum Input{
    movUp   (Direction.North),
    movDown (Direction.South),
    movLeft (Direction.West),
    movRight(Direction.East),
    actShoot();
    
    private Direction dir;
    
    private Input(){
        dir = null;
    }
    
    private Input(Direction dir){
        this.dir = dir;
    }
    
    public Direction getDirection(){
        return this.dir;
    }
}