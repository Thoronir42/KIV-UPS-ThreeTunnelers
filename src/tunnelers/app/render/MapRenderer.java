package tunnelers.app.render;

import tunnelers.app.render.colors.AColorScheme;
import javafx.geometry.Rectangle2D;
import tunnelers.core.model.entities.IntDimension;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.entities.IntRectangle;
import tunnelers.core.model.map.Map;
import tunnelers.core.model.map.Chunk;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class MapRenderer extends ARenderer {

	protected Map map;
	protected int chunkSize;

	public MapRenderer(AColorScheme colorScheme) {
		super(colorScheme);
	}
	
	public void setMap(Map map) {
		this.map = map;
		this.chunkSize = map.getChunkSize();
	}

	public void drawMap(IntRectangle rendSrc) {
		int yMin = (int) (rendSrc.getMinY()),
				xMin = (int) (rendSrc.getMinX()),
				xMax = (int) (rendSrc.getMinX() + rendSrc.getWidth() - 1),
				yMax = (int) (rendSrc.getMinY() + rendSrc.getHeight() - 1);
		IntRectangle bounds = new IntRectangle(xMin, xMax, yMin, yMax);

		int chTop = Math.max(0, yMin / chunkSize),
				chLeft = Math.max(0, xMin / chunkSize),
				chRight = (int) Math.min(map.getWidth(), Math.ceil((xMax + 1.0) / chunkSize)),
				chBottom = (int) Math.min(map.getHeight() - 1, Math.ceil((yMax + 1.0) / chunkSize));
		for (int Y = chTop; Y <= chBottom; Y++) {
			for (int X = chLeft; X < chRight; X++) {
				IntRectangle chunkBounds = new IntRectangle(X * chunkSize, (X + 1) * chunkSize - 1, Y * chunkSize, (Y + 1) * chunkSize);
				renderChunk(map.getChunk(X, Y), chunkBounds, bounds, chunkSize);
			}
		}
	}

	void renderChunk(Chunk chunk, IntRectangle chunkBounds, IntRectangle renderBounds, int chunkSize) {
		
		IntRectangle currentBounds = renderBounds.intersection(chunkBounds);
		for (int y = currentBounds.yMin; y <= currentBounds.yMax; y++) {
			for (int x = currentBounds.xMin; x <= currentBounds.xMax; x++) {
				Block b = chunk.getBlock(x % chunkSize, y % chunkSize);
				if (b == Block.BaseWall) {
					Player p = chunk.getAssignedPlayer();
					if (p == null) {
						g.setFill(this.colorScheme.getError());
					} else {
						g.setFill(colorScheme.playerColors().get(p).color());
					}
				} else {
					g.setFill(this.colorScheme.getBlockColor(x, y, b));
				}
				g.fillRect(x * blockSize.getWidth(), y * blockSize.getHeight(), blockSize.getWidth(), blockSize.getHeight());
			}
		}
		// g.setFill(AColorScheme.getChunkColor(selfXmin / chunkSize, selfYmin / chunkSize));
		// g.fillRect(xFrom*blockSize.getWidth(), yFrom*blockSize.getHeight(), (xTo - xFrom + 1)*blockSize.getWidth(), (yTo - yFrom + 1)*blockSize.getHeight());
	}

	public IntDimension getMapSize() {
		return this.map.getBlockSize();
	}
}
