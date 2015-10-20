package tunnelers.Game.structure;

import javafx.geometry.Point2D;

/**
 *
 * @author Stepan
 */
public enum Direction {
    North(0, -1, false, 3), NorthEast(1, -1, true, 0),
    East(1, 0, false, 0),   SouthEast(1, 1, true, 1),
    South(0, 1, false, 1),  SouthWest(-1, 1, true, 2),
    West(-1, 0, false, 2),  NorthWest(-1, -1, true, 3);
    
    
    private final Point2D direction;
    private final boolean offset;
    private final int rotation;
    
            
            
    private Direction(int x, int y, boolean offset, int rotation){
        this.direction = new Point2D(x, y);
        this.offset = offset;
        this.rotation = rotation;
    }
    
    public boolean resourceOffset(){
        return this.offset;
    }
    
    public int getRotation(){
        return this.rotation;
    }
    
    public Point2D getDirection(){
        return this.direction;
    }
    
}
