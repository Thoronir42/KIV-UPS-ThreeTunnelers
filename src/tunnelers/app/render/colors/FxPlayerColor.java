package tunnelers.app.render.colors;

import javafx.scene.paint.Color;
import tunnelers.core.colors.PlayerColor;

public class FxPlayerColor extends PlayerColor {

	private final Color fxColor;
	private final String name;

	FxPlayerColor(int value, Color fxColor, String name) {
		super(value);
		this.fxColor = fxColor;
		this.name = name;
	}

	public String name() {
		return name;
	}

	public Color color() {
		return this.fxColor;
	}
}
