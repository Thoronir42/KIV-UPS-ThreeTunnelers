package tunnelers.core.model.map;

import javafx.geometry.Point2D;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Map {

	private final Chunk[] chunks;
	private Chunk[] playerBaseChunks;
	public final int Xchunks, Ychunks;
	protected final int chunkSize;

	public Map(int chunkSize, int width, int height, int playerCount) {
		this.Xchunks = width;
		this.Ychunks = height;
		this.chunkSize = chunkSize;
		this.chunks = new Chunk[height * width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.chunks[y * width + x] = new Chunk(x, y, chunkSize);
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
	
	public Chunk getChunk(int x, int y) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		return this.chunks[y * this.Xchunks + x];
	}

	public void updateChunk(int x, int y, byte[][] chunkData) throws ChunkException {
		this.getChunk(x, y).applyData(chunkData);
	}

	public Point2D assignBase(int i, Player p) throws IllegalStateException, IndexOutOfBoundsException {
		Chunk c = this.playerBaseChunks[i];

		if (c.isAssigned()) {
			throw new IllegalStateException("Player base " + i + " chunk is already assigned.");
		}
		
		c.assignPlayer(p);
		return c.getCenter();
	}

	void assignPlayer(int chX, int chY, Player p) {
		this.getChunk(chX, chY).assignedPlayer = p;
	}
	
	public int getWidth(){
		return this.Xchunks;
	}
	
	public int getHeight(){
		return this.Ychunks;
	}

	public int getBlockWidth() {
		return this.getWidth() * chunkSize;
	}

	public int getBlockHeight() {
		return this.getHeight() * chunkSize;
	}

	public int getChunkSize() {
		return chunkSize;
	}
	
	public int getPlayerCount(){
		return this.playerBaseChunks.length;
	}

}
