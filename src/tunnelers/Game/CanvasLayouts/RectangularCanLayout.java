package tunnelers.Game.CanvasLayouts;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import tunnelers.Game.structure.Container;
import tunnelers.Game.structure.Player;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class RectangularCanLayout extends CanvasLayout{
    
    static CanvasLayout getLayoutFor(Container c, Dimension2D canvasArea) throws CanvasLayoutException{
        int playerCount = c.getPlayerCount();
        int rows = 1, cols = 2;
        while (rows * cols < playerCount){
            if(cols > rows){ rows++; }
            else { cols++; }
        }
        return new RectangularCanLayout(c, rows, cols, canvasArea);
    }
    
    
    
    
    private final int rows, cols;
    
    private final PlayerArea playerArea;
    
    public RectangularCanLayout(Container c, int rows, int cols, Dimension2D canvasArea) {
        super(c);
        this.rows = rows;
        this.cols = cols;
        Dimension2D playerAreaBounds = new Dimension2D(canvasArea.getWidth() / cols,
                    canvasArea.getHeight() / rows);
        this.playerArea = new PlayerArea(playerAreaBounds);
    }
    
    @Override
    public int getPlayerCapacity(){ return this.getRowAmount() * this.getColAmount(); }
    
    
    protected int getRowAmount(){ return this.rows; }
    protected int getColAmount(){ return this.cols; }
    
    @Override
    public void drawLayout(GraphicsContext g) {
        Affine defTransform = g.getTransform();
        Dimension2D playerAreaBounds = this.playerArea.getBounds();
        int row = 0, col = 0;
        
        for(Player p : this.container.getPlayers()){
            g.translate(col * playerAreaBounds.getWidth(), row * playerAreaBounds.getHeight());
            System.out.println("Drawing player "+p.getName());
            this.playerArea.draw(g, playerAreaBounds, p);
            
            if(++col >= cols){
                col = 0;
                row++;
            }
            g.setTransform(defTransform);
        }
    }
    
    private class PlayerArea{
        
        private final Dimension2D playerAreaBounds;
        private final Rectangle viewWindow;
        //private final int blockSize;
        //private final Dimension2D blocks;
        
        PlayerArea(Dimension2D playerAreaBounds){
            this.playerAreaBounds = playerAreaBounds;
            this.viewWindow = new Rectangle(playerAreaBounds.getWidth() * 0.9, playerAreaBounds.getHeight() * 0.6);
            //this.blockSize = getBlockSize();
            //this.blocks = getBlocks();
        }
        
        public Dimension2D getBounds(){
            return this.playerAreaBounds;
        }
        
        protected void draw(GraphicsContext g, Dimension2D bounds, Player p) {
            Affine defTransform = g.getTransform();

            g.setFill(p.getColor());
            g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());

            
            g.translate(bounds.getWidth() * 0.05, bounds.getHeight() * 0.05);
            drawViewWindow(g, viewWindow);//, p.getLocation());
            g.setTransform(defTransform);

            Rectangle inBounds = new Rectangle(bounds.getWidth() * 0.8, bounds.getHeight() * 0.1);
            inBounds.setX(bounds.getWidth() * 0.1);
            inBounds.setY(bounds.getHeight() * 0.7);
            fillStatusBar(g, inBounds, Player.COL_HITPOINTS);

            inBounds.setY(bounds.getHeight() * 0.85);
            fillStatusBar(g, inBounds, Player.COL_ENERGY);
        }
        private void fillStatusBar(GraphicsContext g, Rectangle r, Color c){
            g.setFill(Color.DIMGREY);
            g.setLineWidth(4);
            g.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            g.setFill(c);
            g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }

        private void drawViewWindow(GraphicsContext g, Rectangle bounds){//, Point2D center){

            g.setStroke(Color.DIMGREY);
            g.setLineWidth(2);
            g.strokeRect(0, 0, bounds.getWidth(), bounds.getHeight());

            /*Point2D[] corners = {
                new Point2D(center.getX() - blocks.getWidth() / 2, center.getY() - blocks.getHeight() / 2 ),
                new Point2D(center.getX() + blocks.getWidth() / 2, center.getY() - blocks.getHeight() / 2 ),
                new Point2D(center.getX() - blocks.getWidth() / 2, center.getY() + blocks.getHeight() / 2 ),
                new Point2D(center.getX() + blocks.getWidth() / 2, center.getY() + blocks.getHeight() / 2 ),
            };*/

            g.setFill(Settings.getRandColor());
            g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
        }
        
        
        private int getBlockSize(){
            return (int)(Math.min(playerAreaBounds.getHeight(),
                                  playerAreaBounds.getWidth()) / 60);
        }
        /*private Dimension2D getBlocks(){
            
        }*/
    }
}
