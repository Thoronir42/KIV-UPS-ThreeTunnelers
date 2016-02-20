package tunnelers.Game.Render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import tunnelers.Game.structure.Container;

/**
 *
 * @author Stepan
 */
public abstract class CanvasLayout {	
    public static CanvasLayout choseIdeal(Container container, Dimension2D d) {
        try{
            return RectangularCanLayout.getLayoutFor(container, d);
        } catch (CanvasLayoutException e){
            throw new IllegalArgumentException(String.format("Could not find layout suitable for %d players.", container.getPlayerCount()));
        }
    }
    
    protected final Container container;
    
    public CanvasLayout(Container c){
        this.container = c;
    }
    
    public abstract void drawLayout(GraphicsContext g);
}