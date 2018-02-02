package tunnelers.app.assets;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Assets {

	public static final int IMAGE_UPSCALE_MULT = 10;

	private final Image[] resources;

	private final IAssetImagesProvider[] imageProviders;

	public Assets() {
		this(new StandardImageProvider());
	}

	public Assets(String imagesPath) {
		this(
				new FileSystemImageProvider(imagesPath),
				new StandardImageProvider()
		);
	}

	private Assets(IAssetImagesProvider... imageProviders) {
		resources = new Image[Asset.count()];
		this.imageProviders = imageProviders;
	}

	private Image getImage(Asset type) {
		return resources[type.getOrder()];
	}

	public Image getImage(Asset type, Color c) {
		return ImageTools.recolor(getImage(type), c);
	}

	public void init() {
		for (Asset a : Asset.values()) {
			resources[a.getOrder()] = loadImage(a);
		}
	}

	private Image loadImage(Asset type) {
		Image tmp = null;
		for (IAssetImagesProvider provider : this.imageProviders) {
			try {
				tmp = provider.getImage(type);
				break;
			} catch (IllegalArgumentException e) {
				System.err.println(e.toString());
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
