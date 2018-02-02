package tunnelers.app.assets;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

public class StandardImageProvider extends AAssetsImageProvider {

	@Override
	protected InputStream getImageStream(Asset type) throws IllegalArgumentException {
		String encoded = getEncodedImage(type);
		byte[] buf = Base64.getDecoder().decode(encoded);
		return new ByteArrayInputStream(buf);
	}

	private static String getEncodedImage(Asset type) throws IllegalArgumentException {
		switch (type) {
			default:
				throw new IllegalArgumentException("Unrecognised resource const: " + type);
			case TankBody:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4QELFi8U68D/5AAAAClJREFUCNdjYCAEysrK/mPjM8EE/v///x+ZRpHEBlAkkXURr5MRCcDEACtlEM3AYLCJAAAAAElFTkSuQmCC";
			case TankBodyDiag:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QAdgB2AHZmy277AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAcl90AB7QAAAC5JREFUCNd1jrENADAMg3I179OpUpo6TJYYcFUDsBKAqqv45BR3Pwkbsb2K9PYAhGU+3Zyl4S4AAAAASUVORK5CYII=";
			case TankCannon:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4QELFi8uLcwmVgAAABpJREFUCNdjYEAC/////4/MZ2LAA8iXpBEAAPNdBAM6JcmnAAAAAElFTkSuQmCC";
			case TankCannonDiag:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QAdgB2AHZmy277AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAgSyGW4LQAAABRJREFUCNdjYCAV/P///z+VJKgLAOkoC/U1nz3zAAAAAElFTkSuQmCC";
			case Projectile:
				return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAADCAYAAABS3WWCAAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAQOcNGrbgAAABdJREFUCNdj+P///08mBgaG50wMDAyXADf1BbTazYReAAAAAElFTkSuQmCC";
			case ProjectileDiag:
				return "iVBORw0KGgoAAAANSUhEUgAAAAMAAAADCAYAAABWKLW/AAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsdDAUbBBd+xAAAABdJREFUCNdjYICC/////4QxHsAYm2CyAOO4C4Pn0+mkAAAAAElFTkSuQmCC";
		}
	}

}
