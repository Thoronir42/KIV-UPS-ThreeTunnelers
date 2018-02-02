package tunnelers.app.render.colors;

import javafx.scene.paint.Color;
import tunnelers.core.colors.PlayerColorManager;

public class FxPlayerColorManager extends PlayerColorManager<FxPlayerColor> {

	private static FxPlayerColor[] getDefaultColors() {
		String[][] definitions = {
				// 0
				{"0x55D43F", "Green"},
				{"0x000084", "Navy blue"},
				{"0xFF6600", "Orange"},
				{"0x663399", "Purple"},
				{"0xFF0080", "Deep pink"},
				// 5
				{"0x3399FF", "Blue"},
				{"0xFFC200", "Orange yellow"},
				{"0x9C2A00", "Red orange"},
				{"0xFEFCD7", "Moon glow"},
				{"0xAA0078", "Fuchsia"},
				// 10
				{"0xFE4902", "Vermilion"},
				{"0x005A04", "Camarone"},
				{"0x66A7C5", "Teal"},
				// 13
		};
		FxPlayerColor[] colors = new FxPlayerColor[definitions.length];

		for (int i = 0; i < definitions.length; i++) {
			String[] definition = definitions[i];
			colors[i] = new FxPlayerColor(i, Color.web(definition[0]), definition[1]);
		}

		return colors;
	}

	public FxPlayerColorManager() {
		super(getDefaultColors(), new FxPlayerColor(-1, Color.web("#800"), "SYSLOL"));
	}
}
