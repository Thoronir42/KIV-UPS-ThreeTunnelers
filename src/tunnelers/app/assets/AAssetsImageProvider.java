package tunnelers.app.assets;

import java.io.InputStream;
import javafx.scene.image.Image;

/**
 *
 * @author Stepan
 */
public abstract class AAssetsImageProvider implements IAssetImagesProvider{

	@Override
	public Image getImage(int type) throws IllegalArgumentException{
		return new Image(this.getImageStream(type));
	}
	
	protected abstract InputStream getImageStream(int type) throws IllegalArgumentException;
	
}
