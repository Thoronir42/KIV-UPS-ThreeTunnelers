package tunnelers.Game.CanvasLayouts;


import tunnelers.Game.structure.Container;

/**
 *
 * @author Stepan
 */
public class CanLayout_2x2 extends ARectangularCanLayout{
    
    private static final int COLS = 2,
                             ROWS = 2;

    @Override
    protected int getRowAmount() {
        return ROWS;
    }

    @Override
    protected int getColAmount() {
        return COLS;
    }
    
    public CanLayout_2x2(Container container) throws CanvasLayoutException{
        super(container);
        int playerAmount = container.getPlayerCount();
        if(playerAmount> this.getPlayerCapacity()){
            throw new CanvasLayoutException(playerAmount);
        }
    }
    
}
