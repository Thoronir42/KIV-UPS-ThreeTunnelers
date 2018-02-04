package tunnelers.app.render;

import javafx.scene.canvas.GraphicsContext;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.core.model.entities.IntDimension;
import tunnelers.core.model.entities.IntRectangle;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Chunk;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;

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

	public void drawMap(GraphicsContext g, IntRectangle rendSrc) {
		int yMin = rendSrc.getMinY(),
				xMin = rendSrc.getMinX(),
				xMax = rendSrc.getMinX() + rendSrc.getWidth(),
				yMax = rendSrc.getMinY() + rendSrc.getHeight();

		int chTop = Math.max(0, yMin / chunkSize),
				chLeft = Math.max(0, xMin / chunkSize),
				chRight = (int) Math.min(map.getWidth(), Math.ceil((xMax + 1.0) / chunkSize)),
				chBottom = (int) Math.min(map.getHeight() - 1, Math.ceil((yMax + 1.0) / chunkSize));
		for (int Y = chTop; Y <= chBottom; Y++) {
			for (int X = chLeft; X < chRight; X++) {
				IntRectangle chunkBounds = new IntRectangle(X * chunkSize, Y * chunkSize, chunkSize - 1, chunkSize - 1);
				renderChunk(g, map.getChunk(X, Y), chunkBounds, rendSrc);
			}
		}
	}

	private void renderChunk(GraphicsContext g, Chunk chunk, IntRectangle chunkBounds, IntRectangle renderBounds) {
		IntRectangle currentBounds = renderBounds.intersection(chunkBounds);

		for (int y = currentBounds.getMinY(); y <= currentBounds.getMaxY(); y++) {
			for (int x = currentBounds.getMinX(); x <= currentBounds.getMaxX(); x++) {
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
