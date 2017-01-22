package tunnelers.core.model.map;

import tunnelers.core.model.entities.IntDimension;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Map {

	protected final IntDimension chunksSize;
	protected final IntDimension blockSize;
	protected final int chunkSize;
	
	private final Chunk[] chunks;
	private final IntPoint[] playerBaseChunks;

	public Map(int chunkSize, int width, int height, int playerCount) {
		this.chunksSize = new IntDimension(width, height);
		this.chunkSize = chunkSize;
		this.chunks = new Chunk[height * width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.chunks[y * width + x] = new Chunk(chunkSize);
			}
		}

		this.playerBaseChunks = new IntPoint[playerCount];

		this.blockSize = new IntDimension(width * chunkSize, height * chunkSize);
	}

	public void setPlayerBaseChunk(int n, IntPoint baseChunk, Player p) {
		if (n < 0 || n >= this.playerBaseChunks.length) {
			String message = String.format("Invalid player base index");
			throw new IllegalArgumentException(message);
		}
		if(!this.chunksSize.contains(baseChunk)){
			throw new ChunkException(baseChunk.getX(), baseChunk.getY(),
					this.chunksSize.getWidth(), this.chunksSize.getHeight());
		}
		
		this.playerBaseChunks[n] = baseChunk;

		this.assignBase(n, p);
	}

	public Chunk getChunk(int x, int y) throws ChunkException {
		int width = this.getWidth(), height = this.getHeight();

		if ((x < 0 || x >= width) || (y < 0 || y >= height)) {
			throw new ChunkException(x, y, width, height);
		}
		return this.chunks[y * width + x];
	}

	private IntPoint findChunk(Chunk chunk) {
		for (int i = 0; i < this.chunks.length; i++) {
			if (this.chunks[i] == chunk) {
				return new IntPoint(i % this.getWidth(), i / this.getWidth());
			}
		}

		return null;
	}

	public Block getBlock(int x, int y) {
		Chunk chunk = this.getChunk(x / chunkSize, y / chunkSize);
		return chunk.getBlock(x % chunkSize, y % chunkSize);
	}

	public boolean setBlock(int x, int y, Block block) {
		Chunk chunk = this.getChunk(x / chunkSize, y / chunkSize);
		chunk.setBlock(x % chunkSize, y % chunkSize, block);

		return true;
	}

	/**
	 * Injects blocks into the chunk. Counts undefined blocks and returns true
	 * if there were none
	 *
	 * @param x
	 * @param y
	 * @param chunkData
	 * @return
	 * @throws ChunkException
	 */
	public boolean updateChunk(int x, int y, Block[] chunkData) throws ChunkException {
		return this.getChunk(x, y).applyData(chunkData) == 0;
	}

	public IntPoint assignBase(int i, Player p) throws IllegalStateException, IndexOutOfBoundsException {
		IntPoint base = this.playerBaseChunks[i];
		Chunk c = this.getChunk(base.getX(), base.getY());

		if (c.isAssigned()) {
			throw new IllegalStateException("Player base " + i + " chunk is already assigned.");
		}

		c.assignPlayer(p);

		IntPoint chunkPosition = this.findChunk(c);
		if (chunkPosition == null) {
			System.err.println("Chunk was not found");
			return new IntPoint(-1, -1);
		}
		chunkPosition.multiply(chunkSize);
		chunkPosition.add(new IntPoint(chunkSize / 2, chunkSize / 2));

		return chunkPosition;
	}

	public int getWidth() {
		return this.chunksSize.getWidth();
	}

	public int getHeight() {
		return this.chunksSize.getHeight();
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

	public int getPlayerCount() {
		return this.playerBaseChunks.length;
	}

	public IntDimension getBlockSize() {
		return this.blockSize;
	}

}
