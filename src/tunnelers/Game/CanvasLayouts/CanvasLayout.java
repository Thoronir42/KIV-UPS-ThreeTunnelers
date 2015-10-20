package tunnelers.Game.CanvasLayouts;

/**
 *
 * @author Stepan
 */
public abstract class CanvasLayout {
    public int getPlayerCapacity(){
        throw new UnsupportedOperationException("Unimplemented player amount check method");
    }
}

class CanvasLayoutException extends Exception{
    public CanvasLayoutException(int playerAmount) {
        
    }
    
}