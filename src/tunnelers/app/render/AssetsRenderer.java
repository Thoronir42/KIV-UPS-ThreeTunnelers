package tunnelers.app.render;

import java.util.Collection;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import tunnelers.Game.Render.TunColors;
import tunnelers.app.assets.Assets;
import tunnelers.app.assets.IAssetImagesProvider;
import tunnelers.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class AssetsRenderer {
	final HashMap<Integer, Image[]> tankBody;

	final Image[] tankCannon;

	final Image[] projectile;

	public AssetsRenderer(Assets assets, Collection<APlayer> players) {
		tankBody = new HashMap<>();
		tankCannon = new Image[2];
		projectile = new Image[2];

		for (APlayer player : players) {
			Color c = player.getColor();
			Image[] tankImages = new Image[2];
			tankImages[IAssetImagesProvider.IMG_REG] = assets.getImage(IAssetImagesProvider.TANK_BODY, c);
			tankImages[IAssetImagesProvider.IMG_DIAG] = assets.getImage(IAssetImagesProvider.TANK_BODY_DIAG, c);
			tankBody.put(player.getID(), tankImages);
		}

		tankCannon[IAssetImagesProvider.IMG_REG] = assets.getImage(IAssetImagesProvider.TANK_CANNON, TunColors.getCannonColor());
		tankCannon[IAssetImagesProvider.IMG_DIAG] = assets.getImage(IAssetImagesProvider.TANK_CANNON_DIAG, TunColors.getCannonColor());
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
}
