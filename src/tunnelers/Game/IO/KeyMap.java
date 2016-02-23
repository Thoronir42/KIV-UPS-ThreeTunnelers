package tunnelers.Game.IO;

import java.util.HashMap;
import java.util.Map.Entry;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Stepan
 */
public class KeyMap {

	public static final byte KEYBOARD_PRIMARY = 0,
			KEYBOARD_SECONDARY = 1;
	protected static final byte[] KEYBOARD_LAYOUTS = {KEYBOARD_PRIMARY};

	public static byte[] getKeyboardLayouts(){
		return KEYBOARD_LAYOUTS;
	}
	
	public static String codeToStr(KeyCode kc){
		if(kc == null){
			return "N/A";
		}
		switch(kc){
			
		}
		return kc.getName();
	}
	
	private final HashMap<KeyCode, PlrInput> map;

	public KeyMap() {
		map = new HashMap<>();
	}

	public void resetAll() {
		for (byte sIndex = 1; sIndex <= 2; sIndex++) {
			resetScheme(sIndex);
		}
	}

	public void resetScheme(byte sIndex) {
		switch (sIndex) {
			case KEYBOARD_PRIMARY:
				set(KeyCode.UP, sIndex, Input.movUp);
				set(KeyCode.LEFT, sIndex, Input.movLeft);
				set(KeyCode.RIGHT, sIndex, Input.movRight);
				set(KeyCode.DOWN, sIndex, Input.movDown);
				set(KeyCode.NUMPAD0, sIndex, Input.actShoot);
				break;
			case KEYBOARD_SECONDARY:
				set(KeyCode.W, sIndex, Input.movUp);
				set(KeyCode.A, sIndex, Input.movLeft);
				set(KeyCode.D, sIndex, Input.movRight);
				set(KeyCode.S, sIndex, Input.movDown);
				set(KeyCode.F, sIndex, Input.actShoot);
				break;
		}
	}

	public PlrInput set(KeyCode code, byte pIndex, Input i) {
		return this.set(code, new PlrInput(pIndex, i));
		
	}
	
	public PlrInput set(KeyCode code, PlrInput pin){
		PlrInput cur = null;
		if (map.containsValue(pin)) {
			cur = map.remove(code);
		}
		map.put(code, pin);

		return cur;
	}

	public PlrInput getInput(KeyCode code) {
		return map.get(code);
	}

	public KeyCode getKey(PlrInput pin) {
		for (Entry<KeyCode, PlrInput> entry : map.entrySet()) {
			if (pin.equals(entry.getValue())) {
				return entry.getKey();
			}

		}
		return null;
	};
}
