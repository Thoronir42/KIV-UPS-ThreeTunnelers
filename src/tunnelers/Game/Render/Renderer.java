package tunnelers.Game.Render;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import tunnelers.Assets;
import tunnelers.Game.Map.Block;
import tunnelers.Game.Map.Bounds;
import tunnelers.Game.Map.TunnelMap;
import tunnelers.Game.Map.Chunk;
import tunnelers.Game.Frame.Player;
import tunnelers.Game.Frame.Tank;

/**
 *
 * @author Stepan
 */
public class Renderer {

	protected GraphicsContext g;
	protected Dimension2D blockSize;

	protected final TunnelMap map;
	protected final Assets assets;

	public Renderer(GraphicsContext g, TunnelMap map, Assets assets, Dimension2D blockSize) {
		this.g = g;

		this.map = map;
		this.assets = assets;

		this.blockSize = blockSize;
	}

	public void drawMap(Rectangle rendSrc) {
		int yMin = (int) (rendSrc.getY()),
				xMin = (int) (rendSrc.getX()),
				xMax = (int) (rendSrc.getX() + rendSrc.getWidth()),
				yMax = (int) (rendSrc.getY() + rendSrc.getHeight() - 1);
		Bounds bounds = new Bounds(xMin, xMax, yMin, yMax);
		int chunkSize = this.map.getChunkSize();

		int chTop = Math.max(0, yMin / chunkSize),
				chLeft = Math.max(0, xMin / chunkSize),
				chRight = (int) Math.min(map.Xchunks, Math.ceil((xMax + 1.0) / chunkSize)),
				chBottom = (int) Math.min(map.Ychunks - 1, Math.ceil((yMax + 1.0) / chunkSize));
		for (int Y = chTop; Y <= chBottom; Y++) {
			for (int X = chLeft; X < chRight; X++) {
				renderChunk(map.getChunk(X, Y), bounds, chunkSize);
			}
		}
	}

	void renderChunk(Chunk chunk, Bounds renderBounds, int chunkSize) {
		Bounds currentBounds = renderBounds.intersection(chunk.bounds);
		for (int y = currentBounds.yMin; y <= currentBounds.yMax; y++) {
			for (int x = currentBounds.xMin; x <= currentBounds.xMax; x++) {
				Block b = chunk.getBlock(x % chunkSize, y % chunkSize);
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

	void drawTank(Tank t) {
		Image iv_body = this.assets.getTankBodyImage(t.getPlayerId() - 1, t.getDirection().isDiagonal());
		Image iv_cannon = this.assets.getTankCannonImage(t.getDirection().isDiagonal());

		double bw = blockSize.getWidth(), bh = blockSize.getHeight();

		int rotation = t.getDirection().getRotation();
		int dx = (int) (Tank.SIZE.getWidth() / 2),
				dy = (int) (Tank.SIZE.getHeight() / 2);
		switch (rotation) {
			case 0:
			default:
				break;
			case 1:
				g.translate(bw, 0);
				break;
			case 2:
				g.translate(bw, bh);
				break;
			case 3:
				g.translate(0, bh);
				break;
		}
		g.rotate(rotation * 90);
		g.drawImage(iv_body, -dx * bw, -dy * bh,
				Tank.SIZE.getWidth() * bw, Tank.SIZE.getHeight() * bh);
		g.drawImage(iv_cannon, -dx * bw, -dy * bh,
				Tank.SIZE.getWidth() * bw, Tank.SIZE.getHeight() * bh);

	}

}
