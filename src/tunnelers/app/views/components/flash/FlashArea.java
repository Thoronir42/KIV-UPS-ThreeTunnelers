package tunnelers.app.views.components.flash;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class FlashArea extends VBox {

	private final Label label;

	private float visibility;

	private LinearEaseFunction ease;

	public FlashArea() {
		super();
		this.label = new Label();
		this.visibility = 0;
		
		this.setBackground(new Background(new BackgroundFill(Color.SNOW, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPadding(new Insets(10));
		
		this.getChildren().add(this.label);
	}

	public void display(String message) {
		this.display(message, false);
	}

	public void display(String message, boolean immediate) {
		label.setText(message);
		setTarget(1, immediate);
	}

	public void clear() {
		this.clear(false);
	}

	public void clear(boolean immediately) {
		this.setTarget(0, immediately);
	}

	private void setTarget(float value, boolean immediate) {
		if (immediate) {
			this.visibility = value;
		} else {
			this.ease = new LinearEaseFunction(this.visibility, value, 0.1f);
		}
	}

	public float getVisibility() {
		return visibility;
	}

	public boolean updateVisibility() {
		if (this.ease == null) {
			return false;
		}

		float newVal = this.ease.next();
		if (newVal == this.visibility) {
			this.ease = null;
			return false;
		}

		this.visibility = newVal;

		return true;
	}
}
