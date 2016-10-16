package tunnelers.app.render;

import tunnelers.app.render.colors.AColorScheme;
import java.util.Collection;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import tunnelers.app.assets.Assets;
import tunnelers.app.assets.IAssetImagesProvider;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class AssetsRenderer extends ARenderer {

	final HashMap<Integer, Image[]> tankBody;

	final Image[] tankCannon;

	final Image[] projectile;

	public AssetsRenderer(AColorScheme colorScheme, Assets assets, Collection<APlayer> players) {
		super(colorScheme);
		tankBody = new HashMap<>();
		tankCannon = new Image[2];
		projectile = new Image[2];

		for (APlayer player : players) {
			Color c = colorScheme.getPlayerColor(player);
			Image[] tankImages = new Image[2];
			tankImages[IAssetImagesProvider.IMG_REG] = assets.getImage(IAssetImagesProvider.TANK_BODY, c);
			tankImages[IAssetImagesProvider.IMG_DIAG] = assets.getImage(IAssetImagesProvider.TANK_BODY_DIAG, c);
			tankBody.put(player.getID(), tankImages);
		}

		tankCannon[IAssetImagesProvider.IMG_REG] = assets.getImage(IAssetImagesProvider.TANK_CANNON, this.colorScheme.getCannonColor());
		tankCannon[IAssetImagesProvider.IMG_DIAG] = assets.getImage(IAssetImagesProvider.TANK_CANNON_DIAG, this.colorScheme.getCannonColor());
	}

	public Image getTankBodyImage(int playerId, boolean diagonal) {
		return imgDiagSwitch(tankBody.get(playerId), diagonal);
	}

	public Image getTankCannonImage(boolean diagonal) {
		return imgDiagSwitch(tankCannon, diagonal);
	}

	public Image getProjectileImage(boolean diagonal) {
		return imgDiagSwitch(projectile, diagonal);
	}

	private Image imgDiagSwitch(Image[] img, boolean diagonal) {
		return diagonal ? img[IAssetImagesProvider.IMG_DIAG] : img[IAssetImagesProvider.IMG_REG];
	}

	public void drawTank(Tank t) {
		Image iv_body = this.getTankBodyImage(t.getPlayerId(), t.getDirection().isDiagonal());
		Image iv_cannon = this.getTankCannonImage(t.getDirection().isDiagonal());

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