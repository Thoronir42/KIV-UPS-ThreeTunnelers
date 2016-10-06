package tunnelers.app.assets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author Stepan
 */
public class FileSystemImageProvider extends AAssetsImageProvider{

	private final static String[] RESOURCE_FILENAMES = {
		"tank_body.png", "tank_body_diag.png",
		"tank_cannon.png", "tank_cannon_diag.png",
		"projectile.png", "projectile_diag.png",};

	private final String path;

	public FileSystemImageProvider(String path) {
		this.path = path;
	}

	@Override
	protected InputStream getImageStream(int type) throws IllegalArgumentException{
		if (type < 0 || type > IMAGES_COUNT) {
			return null;
		}
		try {
			File resFile = new File(this.path + RESOURCE_FILENAMES[type]);
			return new FileInputStream(resFile);
		} catch (FileNotFoundException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}
