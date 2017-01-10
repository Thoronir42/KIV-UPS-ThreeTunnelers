package tunnelers.core.model.map;

import java.util.Arrays;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Chunk {
	private final int chunkSize;
	protected ChunkType type;
	
	protected Block[] chunkData;
	protected Player assignedPlayer;

	public Chunk(int chunkSize) {
		this.chunkSize = chunkSize;
		this.chunkData = this.createBlockArray(chunkSize);
		
		this.type = ChunkType.Standard;
	}

	private Block[] createBlockArray(int chunkSize) {
		int blockInChunk = chunkSize * chunkSize;
		Block[] array = new Block[blockInChunk];
		Arrays.fill(array, 0, blockInChunk, Block.Breakable);
		return array;
	}

	public void setType(ChunkType type) {
		this.type = type;
	}

	void assignPlayer(Player p) {
		this.assignedPlayer = p;
	}

	protected int applyData(byte[] chunkData) {
		int errors = 0;
		for (int i = 0; i < chunkData.length; i++) {
			int row = i % chunkSize;
			int col = i / chunkSize;
			Block b = Block.fromByteValue(chunkData[col]);
			if (b.equals(Block.Undefined)) {
				errors++;
				continue;
			}
			this.chunkData[row * chunkSize + col] = b;
		}

		return errors;
	}

	public Block getBlock(int x, int y) {
		return chunkData[y * chunkSize + x];
	}

	public void setBlock(int x, int y, Block block) {
		this.chunkData[y * chunkSize + x] = block;
	}

	public Player getAssignedPlayer() {
		return this.assignedPlayer;
	}

	public boolean isAssigned() {
		return this.assignedPlayer != null;
	}

	public boolean isBase() {
		return this.type == ChunkType.Base;
	}
}
