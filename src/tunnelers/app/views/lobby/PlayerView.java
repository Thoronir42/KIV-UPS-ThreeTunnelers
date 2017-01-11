package tunnelers.app.views.lobby;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.app.render.colors.FxPlayerColor;

/**
 *
 * @author Stepan
 */
public class PlayerView extends GridPane {

	private static final CornerRadii CORNER_RADII = new CornerRadii(6);
	private static final BorderWidths BORDER_WIDTHS = new BorderWidths(3);

	private static final Border EMPTY_BORDER = new Border(new BorderStroke(Color.DIMGRAY, BorderStrokeStyle.DASHED, CORNER_RADII, new BorderWidths(2)));
	private static final Background EMPTY_BACKGROUND = new Background(new BackgroundFill(Color.web("DDDDDD"), CORNER_RADII, Insets.EMPTY));

	private final Label lblName;
	private final Label lblColor;

	public PlayerView() {
		this.setPadding(new Insets(4));

		this.add(lblName = new Label(), 0, 0);
		this.add(lblColor = new Label(), 0, 1);
		
		this.clear();
		
	}

	public void setName(String name) {
		this.lblName.setText(name);
	}

	public void setColor(FxPlayerColor color) {
		boolean darkBackground;
		if(color == null){
			this.setBackground(EMPTY_BACKGROUND);
			this.setBorder(EMPTY_BORDER);
			this.lblColor.setText("---");
			
			darkBackground = false;
		} else {
			Color fxColor = color.color();
			this.setBackground(new Background(new BackgroundFill(fxColor, CORNER_RADII, Insets.EMPTY)));
			this.setBorder(new Border(new BorderStroke(fxColor.darker().darker(), BorderStrokeStyle.SOLID, CORNER_RADII, BORDER_WIDTHS)));
			this.lblColor.setText(String.format("%s", color.name()));
			
			darkBackground = (fxColor.grayscale().getBrightness() < 0.4);
		}
		
		Color labelColor = darkBackground ? Color.WHITE : Color.BLACK;
		this.lblColor.setTextFill(labelColor);
		this.lblName.setTextFill(labelColor);
	}

	public void set(String name, FxPlayerColor color) {
		this.setName(name);
		this.setColor(color);
	}

	public void clear() {
		this.setName("- - -");
		this.setColor(null);
	}
}
