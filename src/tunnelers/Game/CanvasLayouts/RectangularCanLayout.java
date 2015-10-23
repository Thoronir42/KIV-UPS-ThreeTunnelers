package tunnelers.Game.CanvasLayouts;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import tunnelers.Game.structure.Container;
import tunnelers.Game.structure.Player;

/**
 *
 * @author Stepan
 */
public class RectangularCanLayout extends CanvasLayout{
    
    private final int rows, cols;
    
    static CanvasLayout getLayoutFor(Container c) throws CanvasLayoutException{
        int playerCount = c.getPlayerCount();
        int rows = 1, cols = 2;
        while (rows * cols < playerCount){
            if(cols > rows){ rows++; }
            else { cols++; }
        }
        return new RectangularCanLayout(c, rows, cols);
    }
    
    public RectangularCanLayout(Container c, int rows, int cols) {
        super(c);
        this.rows = rows;
        this.cols = cols;
    }
    
    @Override
    public int getPlayerCapacity(){ return this.getRowAmount() * this.getColAmount(); }
    
    
    protected int getRowAmount(){ return this.rows; }
    protected int getColAmount(){ return this.cols; }
    
    @Override
    public void drawLayout(GraphicsContext g) {
        Affine defTransform = g.getTransform();
        
        final int ROWS = this.getRowAmount(),
                  COLS = this.getColAmount();
        final Dimension2D bounds = new Dimension2D(g.getCanvas().getWidth() / COLS,
                    g.getCanvas().getHeight() / ROWS);
        
        
        int row = 0, col = 0;
        for(Player p : this.container.getPlayers()){
            g.translate(col * bounds.getWidth(), row * bounds.getHeight());
            this.drawPlayerArea(g, bounds, p);
            if(++col >= COLS){
                col = 0;
                row++;
            }
            g.setTransform(defTransform);
        }
    }

    @Override
    protected void drawPlayerArea(GraphicsContext g, Dimension2D bounds, Player p) {
        Affine defTransform = g.getTransform();
        g.setFill(p.getColor());
        g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
        g.setTransform(defTransform);
        Rectangle r = new Rectangle(bounds.getWidth() * 0.8, bounds.getHeight() * 0.1);
        
        r.setX(bounds.getWidth() * 0.1);
        
        r.setY(bounds.getHeight() * 0.7);
        fillRect(g, r, Player.COL_HITPOINTS);
        
        r.setY(bounds.getHeight() * 0.85);
        fillRect(g, r, Player.COL_ENERGY);
        
    }
    private void fillRect(GraphicsContext g, Rectangle r, Color c){
        g.setFill(Color.DIMGREY);
        g.setLineWidth(4);
        g.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        g.setFill(c);
        g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
    
    
}
