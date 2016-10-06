package tunnelers.app.assets;

import javafx.scene.image.Image;

/**
 *
 * @author Stepan
 */
public interface IAssetImagesProvider {
	public final static int IMAGES_COUNT = 6;
	public final static int IMG_REG = 0, IMG_DIAG = 1;

	public final static int TANK_BODY = 0, TANK_BODY_DIAG = 1,
			TANK_CANNON = 2, TANK_CANNON_DIAG = 3,
			PROJECTILE = 4, PROJECTILE_DIAG = 5;
	
	public Image getImage(int type) throws IllegalArgumentException;
}
