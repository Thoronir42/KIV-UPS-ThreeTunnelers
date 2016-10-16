package tunnelers.app.assets;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class Assets{

	public static int IMAGE_UPSCALE_MULT = 10;
	
	private final Image[] RESOURCES;

	private final Collection<IAssetImagesProvider> imageProviders;

	public Assets() {
		this(Arrays.asList(
				new IAssetImagesProvider[]{
					new StandardImageProvider(),}
		));
	}

	public Assets(String imagesPath) {
		this(Arrays.asList(
				new IAssetImagesProvider[]{
					new FileSystemImageProvider(imagesPath),
					new StandardImageProvider(),}
		));
	}

	private Assets(Collection<IAssetImagesProvider> imageProviders) {
		RESOURCES = new Image[IAssetImagesProvider.IMAGES_COUNT];
		this.imageProviders = imageProviders;
	}
	
	public Image getImage(int type) {
		if (type < 0 || type >= IAssetImagesProvider.IMAGES_COUNT) {
			throw new IllegalArgumentException("Unrecognised resource const: " + type);
		}
		return RESOURCES[type];
	}

	public Image getImage(int type, Color c) {
		return ImageTools.recolor(getImage(type), c);
	}

	public void init() {

		for (int i = 0; i < IAssetImagesProvider.IMAGES_COUNT; i++) {
			RESOURCES[i] = loadImage(i);
		}
	}

	private Image loadImage(int type) {
		Image tmp = null;
		for (IAssetImagesProvider provider : this.imageProviders) {
			try {
				tmp = provider.getImage(type);
				break;
			} catch (IllegalArgumentException e) {

			}
		}
		if (tmp == null) {
			throw new IllegalArgumentException("No image provider was able to "
					+ "load image of type" + type);
		}

		try {
			return ImageTools.scale(tmp, IMAGE_UPSCALE_MULT);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Upscaling of " + type + " failed");
			return tmp;
		}
	}

}
