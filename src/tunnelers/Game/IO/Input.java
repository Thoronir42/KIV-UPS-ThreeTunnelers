package tunnelers.Game.IO;

import tunnelers.Game.structure.Direction;
    
public enum Input{
    movUp   (Direction.North),
    movDown (Direction.South),
    movLeft (Direction.West),
    movRight(Direction.East),
    actShoot();
    
    private final Direction dir;
    
    private Input(){
        dir = null;
    }
    
    private Input(Direction dir){
        this.dir = dir;
    }
    
    public Direction getDirection(){
        return this.dir;
    }
	
	public double getX(){
		return this.dir.getX();
	}
	
	public double getY(){
		return this.dir.getY();
	}
	
}