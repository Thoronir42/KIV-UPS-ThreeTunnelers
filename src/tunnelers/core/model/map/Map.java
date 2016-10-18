package tunnelers.core.model.map;

import generic.RNG;
import javafx.geometry.Point2D;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class Map {

	private final Chunk[][] map;
	public final int Xchunks, Ychunks;
	protected final int chunkSize;
	protected final int width, height;
	private final Chunk[] playerBaseChunks;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public Map(int chunkSize, int width, int height, int playerCount) {
		this.Xchunks = width;
		this.Ychunks = height;
		this.chunkSize = chunkSize;
		this.width = Xchunks * chunkSize;
		this.height = Ychunks * chunkSize;
		map = initChunks(width, height);

		this.playerBaseChunks = this.initPlayerBaseChunks(playerCount);
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

	private Chunk[] initPlayerBaseChunks(int playerCount) {
		Chunk[] chunks = new Chunk[playerCount];
		// TODO: base distance
		// TODO: terrain editing
		for (int i = 0; i < playerCount; i++) {
			Chunk currentChunk;
			
			do {
				int x = RNG.getRandInt(Xchunks - 2) + 1,
						y = RNG.getRandInt(Ychunks - 2) + 1;
				currentChunk = this.map[x][y];
			} while (currentChunk.isBase());
			
			currentChunk.setType(ChunkType.Base);
			chunks[i] = currentChunk;
		}

		return chunks;
	}

	public void updateChunk(int x, int y, char[][] chunkData) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		this.map[x][y].applyData(chunkData);
	}

	public Point2D assignBase(int i, APlayer p) throws IllegalStateException, IndexOutOfBoundsException {
		Chunk c = this.playerBaseChunks[i];

		if (c.isAssigned()) {
			throw new IllegalStateException("Player base " + i + " chunk is already assigned.");
		}

		System.out.println(" Assigning base " + c.getCenter() + " to " + p.getID());
		c.assignPlayer(p);
		return c.getCenter();

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
