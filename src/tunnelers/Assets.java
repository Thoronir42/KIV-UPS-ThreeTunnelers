package tunnelers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import tunnelers.Game.TunColors;
import tunnelers.Game.Frame.Player;

/**
 *
 * @author Stepan
 */
public class Assets {

	private final static int IMG_REG = 0, IMG_DIAG = 1;

	public final static int TANK_BODY = 0, TANK_BODY_DIAG = 1,
			TANK_CANNON = 2, TANK_CANNON_DIAG = 3,
			PROJECTILE = 4, PROJECTILE_DIAG = 5;
	public final static int RESOURCE_COUNT = 6;

	private final static String[] RES_PATHS = {
		"resources/tank_body.png", "resources/tank_body_diag.png",
		"resources/tank_cannon.png", "resources/tank_cannon_diag.png",
		"resources/projectile.png", "resources/projectile_diag.png",};

	private static final Image[] resources;

	static {
		resources = new Image[RESOURCE_COUNT];
		for (int i = 0; i < RESOURCE_COUNT; i++) {
			resources[i] = loadImage(i);
		}
	}

	private static Image loadImage(int type) {
		Image tmp;
		try {
			if (!Settings.ENABLE_IMAGES_FROM_FILES) {
				throw new IOException("Lel");
			}
			File resFile = new File(RES_PATHS[type]);
			String path = resFile.getCanonicalPath();
			tmp = new Image("file://" + path);
		} catch (IOException e) {
			InputStream is = decodeStdImage(type);
			//System.out.println("Loading img from stream");
			tmp = new Image(is);
		}
		return scale(tmp, Settings.IMAGE_UPSCALE_MULT, RES_PATHS[type]);
	}

	public static Image scale(Image src, int upscale, String fileName) {
		int width = (int) (src.getWidth() * upscale),
				height = (int) (src.getHeight() * upscale);
		WritableImage fin = new WritableImage(width, height);
		System.out.println(fin.toString());
		PixelReader pr = src.getPixelReader();
		PixelWriter pw = fin.getPixelWriter();
		try {
			for (int y = 0; y < width; y++) {
				for (int x = 0; x < height; x++) {
					pw.setColor(x, y, pr.getColor(x / upscale, y / upscale));
				}
			}
		} catch (IndexOutOfBoundsException e) {
			//System.err.println("Upscaling of " + fileName + " failed");
			return src;
		}
		return fin;
	}

	private static InputStream decodeStdImage(int type) {
		String encoded = getEncodedImage(type);
		byte[] buf = Base64.getDecoder().decode(encoded);
		return new ByteArrayInputStream(buf);
	}

	private static String getEncodedImage(int type) {
		switch (type) {
			default:
				throw new IllegalArgumentException("Unrecognised resource const: " + type);
			case TANK_BODY:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAYokOpMEQAAADJJREFUCNdjYMADGMvKyv5jk+jq6mJkZGBgYPj//z+KAkZGiDgLNkE4H59OvHbicywDALALEA21Z1wmAAAAAElFTkSuQmCC";
			case TANK_BODY_DIAG:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QAdgB2AHZmy277AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAcl90AB7QAAAC5JREFUCNd1jrENADAMg3I179OpUpo6TJYYcFUDsBKAqqv45BR3Pwkbsb2K9PYAhGU+3Zyl4S4AAAAASUVORK5CYII=";
			case TANK_CANNON:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QAdgB2AHZmy277AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAgBTNv58wAAABZJREFUCNdjYKAJYETm/P///z8D7QEAlbgD/jsb/gcAAAAASUVORK5CYII=";
			case TANK_CANNON_DIAG:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QAdgB2AHZmy277AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAgSyGW4LQAAABRJREFUCNdjYCAV/P///z+VJKgLAOkoC/U1nz3zAAAAAElFTkSuQmCC";
			case PROJECTILE:
				return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAADCAYAAABS3WWCAAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAQOcNGrbgAAABdJREFUCNdj+P///08mBgaG50wMDAyXADf1BbTazYReAAAAAElFTkSuQmCC";
			case PROJECTILE_DIAG:
				return "iVBORw0KGgoAAAANSUhEUgAAAAMAAAADCAYAAABWKLW/AAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAUbBBd+xAAAABdJREFUCNdjYICC/////4QxHsAYm2CyAOO4C4Pn0+mkAAAAAElFTkSuQmCC";
		}
	}

	public static Image getImage(int type) {
		if (type < 0 || type >= RESOURCE_COUNT) {
			throw new IllegalArgumentException("Unrecognised resource const: " + type);
		}
		return resources[type];
	}

	public static Image getImage(int type, Color c) {
		if (type < 0 || type >= RESOURCE_COUNT) {
			throw new IllegalArgumentException("Unrecognised resource const: " + type);
		}
		return recolor(resources[type], c);
	}

	private static Image recolor(Image src, Color c) {
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

	final Image[][] tankBody;

	final Image[] tankCannon;

	final Image[] projectile;

	public Assets(Player[] players) {
		tankBody = new Image[players.length][2];
		tankCannon = new Image[2];
		projectile = new Image[2];

		for (int i = 0; i < players.length; i++) {
			Color c = players[i].getColor();
			tankBody[i][IMG_REG] = Assets.getImage(Assets.TANK_BODY, c);
			tankBody[i][IMG_DIAG] = Assets.getImage(Assets.TANK_BODY_DIAG, c);
		}

		tankCannon[IMG_REG] = Assets.getImage(Assets.TANK_CANNON, TunColors.getCannonColor());
		tankCannon[IMG_DIAG] = Assets.getImage(Assets.TANK_CANNON_DIAG, TunColors.getCannonColor());
	}

	public Image getTankBodyImage(int playerId, boolean diagonal) {
		return imgDiagSwitch(tankBody[playerId], diagonal);
	}

	public Image getTankCannonImage(boolean diagonal) {
		return imgDiagSwitch(tankCannon, diagonal);
	}

	public Image getProjectileImage(boolean diagonal) {
		return imgDiagSwitch(projectile, diagonal);
	}

	private Image imgDiagSwitch(Image[] img, boolean diagonal) {
		return diagonal ? img[IMG_DIAG] : img[IMG_REG];
	}

}
