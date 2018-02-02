package tunnelers.app.assets;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageTools {

	/**
	 * @param src original image
	 * @param c   color to be applied over original image
	 * @return Original image with applied color
	 */
	public static Image recolor(Image src, Color c) {
		int width = (int) src.getWidth(),
				height = (int) src.getHeight();
		WritableImage fin = new WritableImage(width, height);

		PixelWriter pw = fin.getPixelWriter();
		PixelReader pr = src.getPixelReader();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color sc = pr.getColor(x, y);
				Color fc = new Color(
						sc.getRed() * c.getRed(), sc.getGreen() * c.getGreen(),
						sc.getBlue() * c.getBlue(), sc.getOpacity()
				);
				pw.setColor(x, y, fc);
			}
		}

		return fin;
	}

	public static Image scale(Image src, int upscale) throws IndexOutOfBoundsException {
		int width = (int) (src.getWidth() * upscale),
				height = (int) (src.getHeight() * upscale);

		WritableImage fin = new WritableImage(width, height);
		PixelReader pr = src.getPixelReader();
		PixelWriter pw = fin.getPixelWriter();

		for (int y = 0; y < width; y++) {
			for (int x = 0; x < height; x++) {
				pw.setColor(x, y, pr.getColor(x / upscale, y / upscale));
			}
		}

		return fin;
	}
}
