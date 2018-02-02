package tunnelers.core.model.map;

import tunnelers.core.player.Player;

import java.util.Arrays;

public class Chunk {

	private final int chunkSize;

	protected final Block[] chunkData;
	protected Player assignedPlayer;
	protected Type type;

	private int staleness;

	public Chunk(int chunkSize) {
		this.chunkSize = chunkSize;
		this.type = Type.Regular;

		int blockInChunk = chunkSize * chunkSize;
		Block[] array = new Block[blockInChunk];
		Arrays.fill(array, 0, blockInChunk, Block.Dirt);

		this.chunkData = array;
	}

	public Block getBlock(int x, int y) {
		return chunkData[y * chunkSize + x];
	}

	public void setBlock(int x, int y, Block block) {
		this.chunkData[y * chunkSize + x] = block;
	}

	void assignPlayer(Player p) {
		this.assignedPlayer = p;
		this.type = Type.PlayerBase;
	}

	public Player getAssignedPlayer() {
		return this.assignedPlayer;
	}

	public boolean isAssigned() {
		return this.assignedPlayer != null;
	}

	public int getSize() {
		return this.chunkSize;
	}

	public boolean isBase() {
		return this.type == Type.PlayerBase;
	}

	public int getStaleness() {
		return staleness;
	}

	public void setStaleness(int staleness) {
		this.staleness = staleness;
	}

	public enum Type {
		Regular, PlayerBase
	}
}
