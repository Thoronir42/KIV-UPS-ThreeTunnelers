package tunnelers.Game.structure;

import tunnelers.Settings;
import javafx.geometry.Point2D;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import tunnelers.Game.TunColors;

/**
 *
 * @author Stepan
 */
public class TunnelMap {

    public static TunnelMap getMockMap() {
        return new TunnelMap(Settings.MOCK_CHUNK_SIZE, 12, 8);
    }
    
    private final Chunk[][] map;
    protected final int Xchunks, Ychunks;
    protected final int chunkSize;
    protected final int mapWidth, mapHeight;
    
    
    public TunnelMap(int chunkSize, int width, int height){
        this.Xchunks = width;
        this.Ychunks = height;
        this.chunkSize = chunkSize;
        this.mapWidth = Xchunks * chunkSize;
        this.mapHeight = Ychunks * chunkSize;
        map = initChunky(width, height);
    }
    private Chunk[][] initChunky(int width, int height) {
        Chunk[][] tmp = new Chunk[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                tmp[i][j] = new Chunk(i, j);
            }
        }
        return tmp;
    }
    
    
    public void updateChunk(int x, int y, char[][] chunkData) throws ChunkException{
        if((x < 0 || x >= this.Xchunks)||(y < 0 || y >= this.Ychunks)){
            throw new ChunkException(x, y, Xchunks, Ychunks);
        }
    }
    
    
    public void drawMapSection(GraphicsContext g, Dimension2D blockSize, Rectangle render){
        int yMin = (int)(render.getY()),
            xMin = (int)(render.getX()),
            xMax = (int)(render.getX() + render.getWidth() - 1),
            yMax = (int)(render.getY() + render.getHeight() -1);
        int chTop = Math.max(0, yMin / chunkSize),
                chLeft = Math.max(0, xMin / chunkSize),
                chRight = (int)Math.min(this.Xchunks - 1, Math.ceil(xMax * 1.0 / chunkSize)),
                chBottom= (int)Math.min(this.Ychunks - 1,Math.ceil(yMax * 1.0 / chunkSize));
        System.out.format("Map: Rendering blocks from [%d,%d] to [%d,%d]%n", xMin, yMin, xMax, yMax);
        for(int Y = chTop; Y <= chBottom; Y++){
            for(int X = chLeft; X <chRight; X++){
                this.map[X][Y].renderChunk(g, xMin, xMax, yMin, yMax, blockSize);
            }
        }
        
    }

    Point2D getFreeBaseSpot() {
        int x = Settings.getRandInt(Xchunks - 2) + 1, y = Settings.getRandInt(Ychunks - 2) + 1;
        return new Point2D(x * chunkSize, y * chunkSize);
    }
    
    class Chunk{
        protected Block[][] chunkData;
        private final int selfXmin, selfXmax,
                    selfYmin, selfYmax;
        private Player assignedPlayer;
        
        private Chunk(int x, int y){
            this.chunkData = new Block[chunkSize][chunkSize];
            for(int row = 0; row < chunkSize; row++){
                for(int col = 0; col < chunkSize; col++){
                    int val = Settings.getRandInt(100);
                    this.chunkData[row][col] = (val < 75) ? Block.Breakable :
                            (val < 95) ? Block.Tough : Block.BaseWall;
                }
            }
            this.selfXmin = x * chunkSize;
            this.selfXmax = ((x + 1) * chunkSize) - 1;
            this.selfYmin = y * chunkSize;
            this.selfYmax = ((y + 1) * chunkSize) - 1;
            System.out.format("Chunk [%d,%d] is from [%d,%d] to [%d,%d]%n",x, y, selfXmin, selfYmin, selfXmax, selfYmax);
        }
        
        void assignPlayer(Player p){
            this.assignedPlayer = p;
        }
        
        void renderChunk(GraphicsContext g, int xMin, int xMax, int yMin, int yMax, Dimension2D blockSize){
            int xFrom = Math.max(xMin, selfXmin),
                xTo = Math.min(xMax, selfXmax),
                yFrom = Math.max(yMin, selfYmin),
                yTo = Math.min(yMax, selfYmax);
            System.out.format("CHNK: Rendering blocks from [%d,%d] to [%d,%d]%n", xFrom, yFrom, xTo, yTo);
            for(int y = yFrom; y <= yTo; y++){
                for(int x = xFrom; x <= xTo; x++){
                    Block b = this.chunkData[x%chunkSize][y%chunkSize];
                    if(b == Block.BaseWall && this.assignedPlayer != null){
                        g.setFill(assignedPlayer.getColor());
                    } else {
                        g.setFill(TunColors.getBlockColor(x, y, b));
                    }
                    g.fillRect(x*blockSize.getWidth(), y*blockSize.getHeight(), blockSize.getWidth(), blockSize.getHeight());
                }
            }
            g.setFill(TunColors.getChunkColor(selfXmin / chunkSize, selfYmin / chunkSize));
            g.fillRect(xFrom*blockSize.getWidth(), yFrom*blockSize.getHeight(), (xTo - xFrom + 1)*blockSize.getWidth(), (yTo - yFrom + 1)*blockSize.getHeight());
        }
        
        
    }
    
    public enum Block{
        Breakable('a'),
        Tough('b'),
        Empty('c'),
        BaseWall('d'),
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