package tunnelers.app.views.components.flash;

import javafx.beans.property.FloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
public class FlashAreaControl extends VBox {

	private final Label label;
	private final FlashContainer container;

	public FlashAreaControl(FlashContainer container) {
		super();
		this.label = new Label(container.getMessage());
		
		System.out.println("New flash area control created with " + container.getMessage());
		
		label.prefWidthProperty().bind(this.widthProperty());
		label.setAlignment(Pos.CENTER);
		
		this.setBackground(new Background(new BackgroundFill(Color.SNOW, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPadding(new Insets(10));
		
		this.getChildren().add(this.label);
		this.container = container;
	}

	public void display(String message) {
		this.display(message, false);
	}

	public void display(String message, boolean immediate) {
		this.container.setMessage(message);
		label.setText(message);
		container.setTarget(1, immediate);
	}

	public void clear() {
		this.clear(false);
	}

	public void clear(boolean immediately) {
		this.container.setTarget(0, immediately);
	}

	

	public float getVisibility() {
		return container.visibilityProperty().get();
	}

	public boolean updateVisibility() {
		return this.container.updateVisibility();
	}
	
	public FloatProperty visibilityProperty(){
		return container.visibilityProperty();
	}
}
