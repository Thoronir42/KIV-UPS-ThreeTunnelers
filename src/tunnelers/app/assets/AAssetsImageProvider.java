package tunnelers.app.assets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javafx.scene.image.Image;

/**
 *
 * @author Stepan
 */
public abstract class AAssetsImageProvider implements IAssetImagesProvider{

	@Override
	public Image getImage(int type) throws IllegalArgumentException{
		InputStream imageStream = this.getImageStream(type);
//		this.printB64encoded(type, imageStream);
		return new Image(imageStream);
	}
	
	private void printB64encoded(int type, InputStream stream) throws IOException{
		byte[] bytes = new byte[stream.available()];
			stream.read(bytes);
			System.out.println(type + ": " + Base64.getEncoder().encodeToString(bytes));
	}
	
	protected abstract InputStream getImageStream(int type) throws IllegalArgumentException;
	
}
