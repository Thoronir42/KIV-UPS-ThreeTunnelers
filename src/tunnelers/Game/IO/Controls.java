package tunnelers.Game.IO;

import tunnelers.Game.structure.Direction;


/**
 *
 * @author Stepan
 */
public class Controls {
    public byte activeX = 0,
				activeY = 0;
	public boolean activeShoot = false;
	
	public void handleControl(Input type, boolean pressed){
		if(type.equals(Input.actShoot)){
			this.activeShoot = pressed;
			return;
		}
		
		int pressFix = pressed ? 1 : -1;
		int addX = (int)type.getX() * pressFix,
			addY = (int)type.getY() * pressFix;
		this.activeX += addX;
		this.activeY += addY;
	}
	
	public Direction getDirection(){
		if(Math.abs(activeX)> 1) activeX = (byte)Math.signum(activeX);
		if(Math.abs(activeY)> 1) activeY = (byte)Math.signum(activeY);
		return Direction.getDirection(activeX, activeY);
	}

	@Override
	public String toString() {
		return String.format("Controls{activeX=%d, activeY=%d, activeShoot=%s}",
				activeX, activeY, activeShoot ? 'Y':'N');
	}
	
	
}