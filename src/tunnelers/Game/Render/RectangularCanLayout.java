package tunnelers.Game.Render;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import tunnelers.Game.TunColors;
import tunnelers.Game.structure.Container;
import tunnelers.Game.structure.Player;
import tunnelers.Game.structure.Tank;
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
        this.playerArea = new PlayerArea(playerAreaBounds, c);
    }
    
    
    protected int getRowAmount(){ return this.rows; }
    protected int getColAmount(){ return this.cols; }
    
    @Override
    public void drawLayout(GraphicsContext g) {
        Affine defTransform = g.getTransform();
        Dimension2D playerAreaBounds = this.playerArea.getBounds();
        int row = 0, col = 0;
        Player[] players = this.container.getPlayers();
		
		
        for(int i = 0; i < players.length; i++){
            g.translate(col * playerAreaBounds.getWidth(), row * playerAreaBounds.getHeight());
            this.playerArea.draw(g, playerAreaBounds, players, i);
            
            if(++col >= cols){
                col = 0;
                row++;
            }
            g.setTransform(defTransform);
        }
    }

	@Override
	public Dimension2D getBlockSize() {
		return this.playerArea.blockSize;
	}
    
    private class PlayerArea{
        private final Dimension2D bounds;
        private final Rectangle viewWindow;
        private final Dimension2D blockSize;
        private final Rectangle render;
		private final Container container;
        
        PlayerArea(Dimension2D playerAreaBounds, Container c){
            this.bounds = playerAreaBounds;
			this.container = c;
            this.viewWindow = new Rectangle(bounds.getWidth() * 0.05, bounds.getHeight() * 0.05, bounds.getWidth() * 0.9, bounds.getHeight() * 0.6);
            this.blockSize = calcBlockSize();
            this.render = calcRender();
        }
        
        public Dimension2D getBounds(){
            return this.bounds;
        }
        
        protected void draw(GraphicsContext g, Dimension2D bounds, Player[] p, int curPlayer) {
            Affine defTransform = g.getTransform();

            g.setFill(p[curPlayer].getColor());
            g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
            
            g.translate(viewWindow.getX(), viewWindow.getY());
            drawViewWindow(g, p, curPlayer);
            g.setTransform(defTransform);

            Rectangle inBounds = new Rectangle(bounds.getWidth() * 0.8, bounds.getHeight() * 0.1);
            inBounds.setX(bounds.getWidth() * 0.1);
            inBounds.setY(bounds.getHeight() * 0.7);
            fillStatusBar(g, inBounds, TunColors.UI_HITPOINTS);

            inBounds.setY(bounds.getHeight() * 0.85);
            fillStatusBar(g, inBounds, TunColors.UI_ENERGY);
        }
        private void fillStatusBar(GraphicsContext g, Rectangle r, Color c){
            g.setFill(Color.DIMGREY);
            g.setLineWidth(4);
            g.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            g.setFill(c);
            g.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }

        private void drawViewWindow(GraphicsContext g, Player[] p, int curPlayer){
            Affine defTransform = g.getTransform();
            g.setFill(Color.BLACK);
            g.fillRect(0-2, 0-2, viewWindow.getWidth()+6, viewWindow.getHeight()+4);
            clampRender(render, p[curPlayer].getLocation());
            try{
                g.translate(-render.getX() * blockSize.getWidth(),
						-render.getY() * blockSize.getHeight());
				renderer.drawMap(render);
				this.drawTanks(g, render, p);
            } catch (Exception e){
                System.err.println(e.getMessage());
            } finally {
                g.setTransform(defTransform);
            }
            
            if(false){
                //this.renderStatic(g, render, p[curPlayer].getEnergyPct());
            }
            
        }
		
		private void drawTanks(GraphicsContext g, Rectangle render, Player[] players){
			Affine defTransform = g.getTransform();
			double bw = blockSize.getWidth(),
					bh= blockSize.getHeight();
			for(Player plr : players){
				Tank t = plr.getTank();
				Point2D po = t.getLocation();
				if(render.contains(po)){
					po = new Point2D(po.getX() * bw, po.getY() * bh);
					g.setFill(plr.getColor());
					g.translate(po.getX(), po.getY());
					renderer.drawTank(t);
					g.setTransform(defTransform);
				}
			}
		}
		
        private void clampRender(Rectangle render, Point2D center){
            double halfWidth = render.getWidth() / 2,
					halfHeight= render.getHeight()/2;
			double mapWidth = container.getMapWidth(),
					mapHeight = container.getMapHeight();
			
			if(center.getX() - halfWidth < 0){
				render.setX(0);
			} else if(center.getX() + halfWidth > mapWidth){
				render.setX(mapWidth - 2 * halfWidth);
			} else {
				render.setX(center.getX() - (int)halfWidth);
			}
			
			if(center.getY() - halfHeight < 0){
				render.setY(0);
			} else if(center.getY() + halfHeight > mapHeight){
				render.setY(mapHeight - 2 * halfHeight);
			} else {
				render.setY(center.getY() - (int)halfHeight);
			}
        }
        
        private void renderStatic(GraphicsContext g, Rectangle render, double energyPct) {
            for(int row = 0; row < render.getWidth(); row++){
                for(int col = 0; col < render.getHeight(); col++){
                    g.setFill(TunColors.getRandStatic(col, row, 1 - energyPct));
                    g.fillRect(col*blockSize.getWidth(), row*blockSize.getHeight(), blockSize.getWidth(), blockSize.getHeight());
                }
            }
        }
        
        
        private Dimension2D calcBlockSize(){
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
            return new Dimension2D((int)width, (int)height);
        }
        private Rectangle calcRender(){
            return new Rectangle(
                    Math.floor(viewWindow.getWidth() / this.blockSize.getWidth()),
                    Math.floor(viewWindow.getHeight() / this.blockSize.getHeight())
            );
        }

        
    }
}
