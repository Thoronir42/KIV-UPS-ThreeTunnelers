package tunnelers.core.model.map;

import javafx.geometry.Point2D;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Map {

	private Chunk[][] chunks;
	private Chunk[] playerBaseChunks;
	public final int Xchunks, Ychunks;
	protected final int chunkSize;
	protected final int Xblocks, Yblocks;

	public Map(int chunkSize, int width, int height, int playerCount) {
		this.Xchunks = width;
		this.Ychunks = height;
		this.chunkSize = chunkSize;
		this.Xblocks = Xchunks * chunkSize;
		this.Yblocks = Ychunks * chunkSize;
		this.chunks = new Chunk[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.chunks[y][x] = new Chunk(x, y, chunkSize);
			}
		}

		this.playerBaseChunks = new Chunk[playerCount];
	}

	public void setPlayerBaseChunks(Chunk[] baseChunks) {
		if (this.playerBaseChunks.length != baseChunks.length) {
			throw new IllegalArgumentException(String.format(
					"Invalid player base chunk arary size. Required: %02d, got: %02d",
					this.playerBaseChunks.length, baseChunks.length));
		}

		this.playerBaseChunks = baseChunks;
	}

	public void updateChunk(int x, int y, byte[][] chunkData) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		this.chunks[y][x].applyData(chunkData);
	}

	public Point2D assignBase(int i, Player p) throws IllegalStateException, IndexOutOfBoundsException {
		Chunk c = this.playerBaseChunks[i];

		if (c.isAssigned()) {
			throw new IllegalStateException("Player base " + i + " chunk is already assigned.");
		}

		System.out.println(" Assigning base " + c.getCenter() + " to " + p.getID());
		c.assignPlayer(p);
		return c.getCenter();

	}

	void assignPlayer(int chX, int chY, Player p) {
		Chunk c = this.chunks[chY][chX];
		c.assignedPlayer = p;
	}

	public Chunk getChunk(int x, int y) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		return this.chunks[y][x];
	}

	public int getWidth() {
		return Xblocks;
	}

	public int getHeight() {
		return Yblocks;
	}

	public int getChunkSize() {
		return chunkSize;
	}
	
	public int getPlayerCount(){
		return this.playerBaseChunks.length;
	}

}
