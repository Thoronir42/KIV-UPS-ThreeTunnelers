package tunnelers.Game.CanvasLayouts;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import tunnelers.Game.TunColors;
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
        
        private final Dimension2D bounds;
        private final Rectangle viewWindow;
        private final Dimension2D blockSize;
        private final Rectangle render;
        
        PlayerArea(Dimension2D playerAreaBounds){
            this.bounds = playerAreaBounds;
            this.viewWindow = new Rectangle(bounds.getWidth() * 0.05, bounds.getHeight() * 0.05, bounds.getWidth() * 0.9, bounds.getHeight() * 0.6);
            this.blockSize = getBlockSize();
            this.render = getRender();
        }
        
        public Dimension2D getBounds(){
            return this.bounds;
        }
        
        protected void draw(GraphicsContext g, Dimension2D bounds, Player p) {
            Affine defTransform = g.getTransform();

            g.setFill(p.getColor());
            g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
            
            g.translate(viewWindow.getX(), viewWindow.getY());
            drawViewWindow(g, p);
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

        private void drawViewWindow(GraphicsContext g, Player p){
            Affine defTransform = g.getTransform();
            g.setFill(Color.BLACK);
            g.fillRect(0, 0, viewWindow.getWidth(), viewWindow.getHeight());
            clampRender(render, p.getLocation());
            try{
                g.translate(-render.getX(), -render.getY());
                container.drawMap(g, blockSize, render);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                g.setTransform(defTransform);
            }
            
            if(false){
                this.renderStatic(g, render, p.getEnergyPct());
            }
            
        }
        
        private void clampRender(Rectangle render, Point2D center){
            
        }
        
        private void renderStatic(GraphicsContext g, Rectangle render, double energyPct) {
            for(int row = 0; row < render.getWidth(); row++){
                for(int col = 0; col < render.getHeight(); col++){
                    g.setFill(TunColors.getRandStatic(col, row, 1 - energyPct));
                    g.fillRect(col*blockSize.getWidth(), row*blockSize.getHeight(), blockSize.getWidth(), blockSize.getHeight());
                }
            }
        }
        
        
        private Dimension2D getBlockSize(){
            double width, height;
            int tmp;
            double bWidth = this.viewWindow.getWidth(),
                   bHeight = this.viewWindow.getHeight();
            if(bWidth < bHeight){
                width = bWidth / Settings.MIN_BLOCKS_ON_DIMENSION;    
                tmp = (int)Math.ceil(bHeight / width);
                if(tmp % 2 == 0){ tmp--; }
                height = bHeight / tmp;
            } else {
                height = bHeight / Settings.MIN_BLOCKS_ON_DIMENSION;    
                tmp = (int)Math.ceil(bWidth / height);
                if(tmp % 2 == 0){ tmp--; }
                width = bWidth / tmp;
            }
            return new Dimension2D(width, height);
        }
        private Rectangle getRender(){
            return new Rectangle(
                    Math.floor(viewWindow.getWidth() / this.blockSize.getWidth()),
                    Math.floor(viewWindow.getHeight() / this.blockSize.getHeight())
            );
        }

        
    }
}
