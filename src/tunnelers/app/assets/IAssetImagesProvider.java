package tunnelers.app.assets;

import javafx.scene.image.Image;

public interface IAssetImagesProvider {
	Image getImage(Asset type) throws IllegalArgumentException;
}
