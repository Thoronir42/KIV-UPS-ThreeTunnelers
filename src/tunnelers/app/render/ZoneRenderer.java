package tunnelers.app.render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import tunnelers.model.map.Block;
import tunnelers.model.map.Bounds;
import tunnelers.model.map.Zone;
import tunnelers.model.map.Chunk;
import tunnelers.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class ZoneRenderer extends ARenderer{

	protected Zone zone;
	protected Dimension2D zoneBounds;

	public ZoneRenderer(GraphicsContext g, Dimension2D blockSize, Zone zone) {
		super(g, blockSize);
		this.setZone(zone);
	}

	public void drawMap(Rectangle rendSrc) {
		int yMin = (int) (rendSrc.getY()),
				xMin = (int) (rendSrc.getX()),
				xMax = (int) (rendSrc.getX() + rendSrc.getWidth()),
				yMax = (int) (rendSrc.getY() + rendSrc.getHeight() - 1);
		Bounds bounds = new Bounds(xMin, xMax, yMin, yMax);
		int chunkSize = this.zone.getChunkSize();

		int chTop = Math.max(0, yMin / chunkSize),
				chLeft = Math.max(0, xMin / chunkSize),
				chRight = (int) Math.min(zone.Xchunks, Math.ceil((xMax + 1.0) / chunkSize)),
				chBottom = (int) Math.min(zone.Ychunks - 1, Math.ceil((yMax + 1.0) / chunkSize));
		for (int Y = chTop; Y <= chBottom; Y++) {
			for (int X = chLeft; X < chRight; X++) {
				renderChunk(zone.getChunk(X, Y), bounds, chunkSize);
			}
		}
	}

	void renderChunk(Chunk chunk, Bounds renderBounds, int chunkSize) {
		Bounds currentBounds = renderBounds.intersection(chunk.bounds);
		for (int y = currentBounds.yMin; y <= currentBounds.yMax; y++) {
			for (int x = currentBounds.xMin; x <= currentBounds.xMax; x++) {
				Block b = chunk.getBlock(x % chunkSize, y % chunkSize);
				if (b == Block.BaseWall) {
					APlayer p = chunk.getAssignedPlayer();
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

	private void setZone(Zone zone){
		this.zone = zone;
		this.zoneBounds = new Dimension2D(zone.getWidth(), zone.getHeight());
	}
	
	public Dimension2D getMapBounds() {
		return this.zoneBounds;
	}
}
