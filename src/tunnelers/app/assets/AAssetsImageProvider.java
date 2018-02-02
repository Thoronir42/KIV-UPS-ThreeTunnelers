package tunnelers.app.assets;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public abstract class AAssetsImageProvider implements IAssetImagesProvider {

	@Override
	public Image getImage(Asset type) throws IllegalArgumentException {
		InputStream imageStream = this.getImageStream(type);
//		this.printB64encoded(type, imageStream);
		return new Image(imageStream);
	}

	private void printB64encoded(int type, InputStream stream) throws IOException {
		byte[] bytes = new byte[stream.available()];
		if (stream.read(bytes) == 0) {
			throw new IOException("Nothing to read");
		}
		System.out.println(type + ": " + Base64.getEncoder().encodeToString(bytes));
	}

	protected abstract InputStream getImageStream(Asset type) throws IllegalArgumentException;

}
