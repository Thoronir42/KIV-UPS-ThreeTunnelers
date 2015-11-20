package tunnelers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Stepan
 */
public class Assets {
	
	public final static int TANK_BODY = 0, TANK_BODY_DIAG = 1,
							TANK_CANNON = 2, TANK_CANNON_DIAG = 3,
							PROJECTILE = 4, PROJECTILE_DIAG = 5;
	public final static int RESOURCE_COUNT = 6;
	private final static String[] RES_PATHS = {
		"resources/tank_body.png", "resources/tank_body_diag.png",
		"resources/tank_cannon.png", "resources/tank_cannon_diag.png", 
		"resources/projectile.png", "resources/projectile_diag.png", 
	};
	
	private static final Image[] resources;
	static{
		resources = new Image[RESOURCE_COUNT];
		for(int i = 0; i < RESOURCE_COUNT; i++){
			resources[i] = loadImage(i);
		}
	}
	
	private static Image loadImage(int type){
		File resFile = new File(RES_PATHS[type]);
		try{
			String path = resFile.getCanonicalPath();
			return new Image("file://"+path);
		} catch (IOException e){
			InputStream is = createStdImage(type);
			System.out.println("Loading img from stream");
			return new Image(is);
		}
	}
	private static InputStream createStdImage(int type){
		String encoded = getEncodedImage(type);
		byte[] buf = Base64.getDecoder().decode(encoded);
		return new ByteArrayInputStream(buf);
	}
	private static String getEncodedImage(int type){
		switch(type){
			default: throw new IllegalArgumentException("Unrecognised resource const: "+type);
			case TANK_BODY:
				return "iVBORw0KGgoAAAANSUhEUgAAAA8AAAAHCAYAAADXhRcnAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3woUDAEklnXwxwAAAF1JREFUGNOdkNEJwDAIRF+gu7iZCziHC7iZ06Q/tSRpKaEHfnjeeShA3y0zm/p2EQ+4+9SbGaqKiNxcA3pmTsJRMBrX2fFlejNWUETsJQOMmojA3dm+uRaUsfD72yc9hEpnxRlC4AAAAABJRU5ErkJggg==";
			case TANK_BODY_DIAG:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsUEyMFV7RrRAAAADRJREFUCNdjYGBg+A/DxcXF/5H5DMgSN2/exJSESWBIokvA2FArGFAEkSQQkjAFaI7C7VoARPhXka6Uxo0AAAAASUVORK5CYII=";
			case TANK_CANNON:
				return  "iVBORw0KGgoAAAANSUhEUgAAAA8AAAAHCAYAAADXhRcnAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3woUDAEcvndIWQAAADNJREFUGNO9z7EJADAMA8FX8P4rfyp3rkyIap1AAWSZs0HqDqskAaCmxU6XJgiQ75+f4AsMEQ4KHWE6ggAAAABJRU5ErkJggg==";
			case TANK_CANNON_DIAG:
				return "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAYAAADEUlfTAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsUEyQKiErgEgAAACxJREFUCNeVzTEKADAMw0C5//+zshaaQONRNziADDtdVHtUSfLiDQBZf35hAZRSCwik++I4AAAAAElFTkSuQmCC";
			case PROJECTILE:
				return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAADCAYAAABS3WWCAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsUFiIftQZhlAAAABhJREFUCNdjuHnz5n+ms2fPMjCdPXuWAQBGnghd0KMt9gAAAABJRU5ErkJggg==";
			case PROJECTILE_DIAG:
				return "iVBORw0KGgoAAAANSUhEUgAAAAMAAAADCAYAAABWKLW/AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wsUFiMJWMnlhAAAACNJREFUCNctyTEBADAMAyCmuZ7iqWqyp7w8FHYXNElRM9PbfudgDNHI5ZVsAAAAAElFTkSuQmCC";
		}
	}
	
	public Image getResourceImage(int type){
		if(type < 0 || type >= RESOURCE_COUNT)
			throw new IllegalArgumentException("Unrecognised resource const: "+type);
		return resources[type];
	}
	
	
	public static void loadAssets(){
		
	}
	public static void printFileToBase64(File file){
		try {
			byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
			String s = new String(encoded);
			System.out.println(file.getName() + " " + s);
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
}
