package tunnelers.Game.CanvasLayouts;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.Game.structure.Container;
import tunnelers.Game.structure.Player;

/**
 *
 * @author Stepan
 */
public abstract class CanvasLayout {
    // TODO: implement dynamic layout chosing
    public static CanvasLayout choseIdeal(Container container) {
        try{
            return RectangularCanLayout.getLayoutFor(container);
        } catch (CanvasLayoutException e){
            throw new IllegalArgumentException(String.format("Could not find layout suitable for %d players.", container.getPlayerCount()));
        }
    }
    
    protected final Container container;
    
    public CanvasLayout(Container c){
        this.container = c;
    }
    
    public int getPlayerCapacity(){
        throw new UnsupportedOperationException("Unimplemented player amount check method");
    }
    
    public abstract void drawLayout(GraphicsContext g);
    
    protected abstract void drawPlayerArea(GraphicsContext g, Dimension2D bounds, Player p);
}