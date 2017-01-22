package tunnelers.core.model.map;

import java.util.Arrays;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Chunk {
	
	private final int chunkSize;

	protected Block[] chunkData;
	protected Player assignedPlayer;
	protected Type type;

	public Chunk(int chunkSize) {
		this.chunkSize = chunkSize;
		this.chunkData = this.createBlockArray(chunkSize);
		this.type = Type.Regular;
	}

	private Block[] createBlockArray(int chunkSize) {
		int blockInChunk = chunkSize * chunkSize;
		Block[] array = new Block[blockInChunk];
		Arrays.fill(array, 0, blockInChunk, Block.Breakable);
		return array;
	}

	void assignPlayer(Player p) {
		this.assignedPlayer = p;
		this.type = Type.PlayerBase;
	}

	protected int applyData(Block[] chunkData) {
		int errors = 0;
		if (chunkData.length != this.chunkData.length) {
			return -1;
		}
		for (int i = 0; i < chunkData.length; i++) {
			if ((this.chunkData[i] = chunkData[i]) == Block.Undefined) {
				errors++;
			};
		}

		return errors;
	}

	public int getSize() {
		return this.chunkSize;
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
		return this.type == Type.PlayerBase;
	}
	
	public static enum Type { Regular, PlayerBase };
}
