package tunnelers.Game.Render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import tunnelers.Game.TunColors;
import tunnelers.Game.map.Block;
import tunnelers.Game.map.Bounds;
import tunnelers.Game.map.TunnelMap;
import tunnelers.Game.map.Chunk;
import tunnelers.Game.structure.Player;

/**
 *
 * @author Stepan
 */
public class Renderer {

	protected GraphicsContext g;
	protected Dimension2D blockSize;

	protected final TunnelMap map;

	public Renderer(GraphicsContext g, TunnelMap map, Dimension2D blockSize) {
		this.g = g;
		this.blockSize = blockSize;

		this.map = map;
	}

	public void drawMap(Rectangle rendSrc) {
		int yMin = (int) (rendSrc.getY()),
				xMin = (int) (rendSrc.getX()),
				xMax = (int) (rendSrc.getX() + rendSrc.getWidth() - 1),
				yMax = (int) (rendSrc.getY() + rendSrc.getHeight() - 1);
		Bounds bounds = new Bounds(xMin, xMax, yMin, yMax);
		int chTop = Math.max(0, yMin / map.chunkSize),
				chLeft = Math.max(0, xMin / map.chunkSize),
				chRight = (int) Math.min(map.Xchunks, Math.ceil((xMax + 1.0) / map.chunkSize)),
				chBottom = (int) Math.min(map.Ychunks - 1, Math.ceil((yMax + 1.0) / map.chunkSize));
		for (int Y = chTop; Y <= chBottom; Y++) {
			for (int X = chLeft; X < chRight; X++) {
				renderChunk(map.getChunk(X, Y), bounds, blockSize);
			}
		}
	}

	void renderChunk(Chunk chunk, Bounds renderBounds, Dimension2D blockSize) {
		Bounds currentBounds = renderBounds.intersection(chunk.bounds);
		for (int y = currentBounds.yMin; y <= currentBounds.yMax; y++) {
			for (int x = currentBounds.xMin; x <= currentBounds.xMax; x++) {
				Block b = chunk.getBlock(x % this.map.chunkSize, y % this.map.chunkSize);
				if (b == Block.BaseWall) {
					Player p = chunk.getAssignedPlayer();
					g.setFill(p != null ? p.getColor() : TunColors.error);

				} else {
					g.setFill(TunColors.getBlockColor(x, y, b));
				}
				g.fillRect(x * blockSize.getWidth(), y * blockSize.getHeight(), blockSize.getWidth(), blockSize.getHeight());
			}
		}
		// g.setFill(TunColors.getChunkColor(selfXmin / chunkSize, selfYmin / chunkSize));
		// g.fillRect(xFrom*blockSize.getWidth(), yFrom*blockSize.getHeight(), (xTo - xFrom + 1)*blockSize.getWidth(), (yTo - yFrom + 1)*blockSize.getHeight());
	}

}
