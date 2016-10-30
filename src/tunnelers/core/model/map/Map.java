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
	

	public int getWidth() {
		return Xblocks;
	}

	public int getHeight() {
		return Yblocks;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public Map(int chunkSize, int width, int height, int playerCount) {
		this.Xchunks = width;
		this.Ychunks = height;
		this.chunkSize = chunkSize;
		this.Xblocks = Xchunks * chunkSize;
		this.Yblocks = Ychunks * chunkSize;
		this.chunks = new Chunk[width][height];

		this.playerBaseChunks = new Chunk[playerCount];
	}

	public void initChunks(Chunk[][] chunks, Chunk[] baseChunks) {
		int newX = chunks.length, newY = this.chunks[0].length;

		if (newX != this.Xchunks || newY != this.Ychunks) {
			throw new IllegalArgumentException(String.format(
					"Invalid chunk array size. Required: [%02d, %02d], got: [%02d, %02d]",
					this.Xchunks, this.Ychunks, newX, newY)
			);
		}
		if (this.playerBaseChunks.length != baseChunks.length) {
			throw new IllegalArgumentException(String.format(
					"Invalid player base chunk arary size. Required: %02d, got: %02d",
					this.playerBaseChunks.length, baseChunks.length));
		}
		this.chunks = chunks;
		this.playerBaseChunks = baseChunks;
	}

	public void updateChunk(int x, int y, char[][] chunkData) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		this.chunks[x][y].applyData(chunkData);
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
		Chunk c = this.chunks[chX][chY];
		c.assignedPlayer = p;
	}

	public Chunk getChunk(int x, int y) throws ChunkException {
		if ((x < 0 || x >= this.Xchunks) || (y < 0 || y >= this.Ychunks)) {
			throw new ChunkException(x, y, Xchunks, Ychunks);
		}
		return this.chunks[x][y];
	}

}
