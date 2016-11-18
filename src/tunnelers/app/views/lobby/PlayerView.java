package tunnelers.app.views.lobby;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class PlayerView extends GridPane {

	private Label lblName;

	public PlayerView() {
		this.setPadding(new Insets(4));

		this.add(lblName = new Label(), 0, 0);
	}

	public void setName(String name) {
		this.lblName.setText(name);
	}

	public void setColor(Color color) {
		BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
		this.backgroundProperty().set(new Background(fill));
	}

	public void set(String name, Color color) {
		this.setName(name);
		this.setColor(color);
	}

}
