package tunnelers.core.model.map;

import javafx.geometry.Point2D;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class Chunk {

	protected Block[][] chunkData;
	public final Bounds bounds;
	private final int chunkSize;
	protected APlayer assignedPlayer;

	protected Chunk(int x, int y, int chunkSize) {
		this.chunkData = new Block[chunkSize][chunkSize];
		this.chunkSize = chunkSize;

		int xMin = x * chunkSize,
				xMax = ((x + 1) * chunkSize) - 1,
				yMin = y * chunkSize,
				yMax = ((y + 1) * chunkSize) - 1;
		this.bounds = new Bounds(xMin, xMax, yMin, yMax);

		//System.out.format("Chunk [%d,%d] is from [%d,%d] to [%d,%d]%n",x, y, selfXmin, selfYmin, selfXmax, selfYmax);
	}

	void assignPlayer(APlayer p) {
		this.assignedPlayer = p;
	}

	protected int applyData(char[][] chunkData) {
		int errors = 0;
		for (int row = 0; row < chunkData.length; row++) {
			if (row > chunkSize) {
				break;
			}
			char[] chunkRow = chunkData[row];
			for (int col = 0; col < chunkRow.length; col++) {
				if (col > chunkSize) {
					break;
				}
				Block b = Block.fromChar(chunkRow[col]);
				if (b.equals(Block.Undefined)) {
					errors++;
					continue;
				}
				this.chunkData[row][col] = b;
			}
		}

		return errors;
	}

	public Block getBlock(int x, int y) {
		return chunkData[x][y];
	}

	public APlayer getAssignedPlayer() {
		return this.assignedPlayer;
	}

	boolean isAssigned() {
		return this.assignedPlayer != null;
	}

	Point2D getCenter() {
		return new Point2D(
				(this.bounds.xMax + this.bounds.xMin) / 2,
				(this.bounds.yMax + this.bounds.yMin) / 2
		);
	}

}
