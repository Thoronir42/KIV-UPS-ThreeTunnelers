package tunnelers.model.map;

import tunnelers.Settings.Settings;
import javafx.geometry.Point2D;
import tunnelers.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class Zone {

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

	public Zone(int chunkSize, int width, int height) {
		this.Xchunks = width;
		this.Ychunks = height;
		this.chunkSize = chunkSize;
		this.width = Xchunks * chunkSize;
		this.height = Ychunks * chunkSize;
		map = initChunks(width, height);
	}

	private Chunk[][] initChunks(int width, int height) {
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

	public Point2D getFreeBaseSpot(APlayer p) {
		int x = Settings.getRandInt(Xchunks - 2) + 1, y = Settings.getRandInt(Ychunks - 2) + 1;
		Chunk c = this.map[x][y];
		c.assignedPlayer = p;
		return new Point2D(x * chunkSize + chunkSize / 2, y * chunkSize + chunkSize / 2);
	}

	void assignPlayer(int chX, int chY, APlayer p) {
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
