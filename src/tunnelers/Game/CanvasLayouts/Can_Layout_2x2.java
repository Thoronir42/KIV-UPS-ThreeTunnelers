package tunnelers.Game.CanvasLayouts;

import tunnelers.Game.structure.Container;

/**
 *
 * @author Stepan
 */
public class Can_Layout_2x2 extends CanvasLayout{
    @Override
    public int getPlayerCapacity(){
        return 4;
    }
    
    public Can_Layout_2x2(Container container) throws CanvasLayoutException{
        int playerAmount = container.getPlayerCount();
        if(playerAmount> this.getPlayerCapacity()){
            throw new CanvasLayoutException(playerAmount);
        }
    }
}
