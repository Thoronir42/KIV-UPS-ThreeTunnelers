package tunnelers.Game.IO;


/**
 *
 * @author Stepan
 */
public class Controls {
    public byte activeX = 0,
				activeY = 0;
	public boolean activeShoot = false;
	
	
	public void handleControll(Input type, boolean pressed){
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
}