package tunnelers.app.assets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileSystemImageProvider extends AAssetsImageProvider {

	private final static Map<Asset, String> RESOURCE_FILE_NAMES = createMap();

	private static Map<Asset, String> createMap() {
		HashMap<Asset, String> map = new HashMap<>();
		map.put(Asset.TankBody, "tank_body.png");
		map.put(Asset.TankBodyDiag, "tank_body_diag.png");
		map.put(Asset.TankCannon, "tank_cannon.png");
		map.put(Asset.TankCannonDiag, "tank_cannon_diag.png");
		map.put(Asset.Projectile, "projectile.png");
		map.put(Asset.ProjectileDiag, "projectile_diag.png");
		return map;
	}

	private final String path;

	FileSystemImageProvider(String path) {
		this.path = path;
	}

	@Override
	protected InputStream getImageStream(Asset type) throws IllegalArgumentException {
		try {
			File resFile = new File(this.path + RESOURCE_FILE_NAMES.get(type));
			return new FileInputStream(resFile);
		} catch (FileNotFoundException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}
