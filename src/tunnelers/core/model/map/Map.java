package tunnelers.core.model.map;

import tunnelers.core.model.entities.IntPoint;
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
				this.chunks[y * width + x] = new Chunk(chunkSize);
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

	private IntPoint findChunk(Chunk chunk) {
		for (int i = 0; i < this.chunks.length; i++) {
			if (this.chunks[i] == chunk) {
				return new IntPoint(i % this.getWidth(), i / this.getWidth());
			}
		}

		return null;
	}

	public void updateChunk(int x, int y, byte[] chunkData) throws ChunkException {
		this.getChunk(x, y).applyData(chunkData);
	}

	public IntPoint assignBase(int i, Player p) throws IllegalStateException, IndexOutOfBoundsException {
		Chunk c = this.playerBaseChunks[i];

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
		return this.Xchunks;
	}

	public int getHeight() {
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

	public int getPlayerCount() {
		return this.playerBaseChunks.length;
	}

}
