package tunnelers.Game.map;

import tunnelers.Settings;
import javafx.geometry.Point2D;
import tunnelers.Game.Frame.Player;

/**
 *
 * @author Stepan
 */
public class TunnelMap {

	public static TunnelMap getMockMap() {
		return new TunnelMap(Settings.MOCK_CHUNK_SIZE, 12, 8);
	}

	private final Chunk[][] map;
	public final int Xchunks, Ychunks;
	protected final int chunkSize;
	protected final int width, height;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public TunnelMap(int chunkSize, int width, int height) {
		this.Xchunks = width;
		this.Ychunks = height;
		this.chunkSize = chunkSize;
		this.width = Xchunks * chunkSize;
		this.height = Ychunks * chunkSize;
		map = initChunky(width, height);
	}

	private Chunk[][] initChunky(int width, int height) {
		Chunk[][] tmp = new Chunk[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tmp[i][j] = MapGenerator.makeChunk(i, j, this.chunkSize);
			}
		}
		return tmp;
	}

	public void updateChunk(int x, int y, char[][] chunkData) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		this.map[x][y].applyData(chunkData);
	}

	public Point2D getFreeBaseSpot(Player p) {
		int x = Settings.getRandInt(Xchunks - 2) + 1, y = Settings.getRandInt(Ychunks - 2) + 1;
		Chunk c = this.map[x][y];
		c.assignedPlayer = p;
		return new Point2D(x * chunkSize + chunkSize / 2, y * chunkSize + chunkSize / 2);
	}

	void assignPlayer(int chX, int chY, Player p) {
		Chunk c = this.map[chX][chY];
		c.assignedPlayer = p;
	}

	public Chunk getChunk(int x, int y) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		return this.map[x][y];
	}

}
