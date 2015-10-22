package tunnelers.Game.structure;

import javafx.geometry.Point2D;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Stepan
 */
public class TunnelMap {

    public static TunnelMap getMockMap() {
        return new TunnelMap(20, 12, 8);
    }
    
    private final Chunk[][] map;
    protected int mapWidth, mapHeight;
    protected int chunkSize;
    
    
    public TunnelMap(int chunkSize, int width, int height){
        map = new Chunk[width][height];
        this.mapWidth = width;
        this.mapHeight = height;
        this.chunkSize = chunkSize;
    }
    
    public void updateChunk(int x, int y, char[][] chunkData) throws ChunkException{
        if((x < 0 || x >= this.mapWidth)||(y < 0 || y >= this.mapHeight)){
            throw new ChunkException(x, y, mapWidth, mapHeight);
        }
    }
    
    
    public void drawMapSection(GraphicsContext g, Point2D location, Dimension2D size){
        boolean[][] chunkDrawn = new boolean[mapWidth][mapHeight];
        int minX = (int)location.getX(), minY = (int)location.getY();
        int maxX = (int)size.getWidth() + minX, maxY = (int)size.getHeight() + minY;
        for(int y = minY; y < maxY; y++){
            for (int x = minX; x < maxX; x++){
                
            }
        }
    }
    
    class Chunk{
        protected Block[][] map;

        public Chunk(){
            this.map = new Block[chunkSize][chunkSize];
            for(int row = 0; row < chunkSize; row++){
                for(int col = 0; col < chunkSize; col++){
                    int val = Settings.getRandInt(100);
                    this.map[row][col] = (val < 80) ? Block.Breakable :
                            (val < 95) ? Block.Tough : Block.Base;
                }
            }
        }
    }
    
    enum Block{
        Breakable('a'),
        Tough('b'),
        Base('c'),
        Undefined('z');
        
        private final char type;
        
        private Block(char type){
            this.type = type;
        }
        
        public boolean isBreakable(){
            return this.type == Breakable.type;
        }
    }
    
}






class ChunkException extends Exception{
    public ChunkException(int x, int y, int maxX, int maxY){
        super(String.format("Invalid chunk position:[%dx%d] Max position (%dx%d) exceeded", x, y, maxX, maxY));
    }
}