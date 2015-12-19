package tunnelers.Game.IO;

import java.util.HashMap;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Stepan
 */
public class KeyMap {

	private final HashMap<KeyCode, PlrInput> map;
	
	
	public KeyMap(){
		map = new HashMap<>();
	}

	public void defaults() {
		byte pIndex = 1;
		set(KeyCode.UP, pIndex, Input.movUp);
		set(KeyCode.LEFT, pIndex, Input.movLeft);
		set(KeyCode.RIGHT, pIndex, Input.movRight);
		set(KeyCode.DOWN, pIndex, Input.movDown);
		set(KeyCode.NUMPAD0, pIndex, Input.actShoot);
		
		pIndex = 2;
		set(KeyCode.W, pIndex, Input.movUp);
		set(KeyCode.A, pIndex, Input.movLeft);
		set(KeyCode.D, pIndex, Input.movRight);
		set(KeyCode.S, pIndex, Input.movDown);
		set(KeyCode.F, pIndex, Input.actShoot);
	}
	
	public void set(KeyCode code, byte pIndex, Input i){
		PlrInput pin = new PlrInput(pIndex, i);
		if(map.containsValue(pin)){
			
		}
		map.put(code, pin);
	}

	public PlrInput getInput(KeyCode code) {
		return map.get(code);
	}
}
