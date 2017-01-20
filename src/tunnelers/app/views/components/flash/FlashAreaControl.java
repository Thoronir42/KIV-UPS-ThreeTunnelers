package tunnelers.app.views.components.flash;

import java.time.Instant;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class FlashAreaControl extends HBox {

	private static FlashAreaControl instance;
	
	public static FlashAreaControl getInstance() {
		if(instance == null){
			instance = new FlashAreaControl();
		}
		
		return instance;
	}
	
	private final Label lblText;
	private final SimpleFloatProperty visibility;
	
	private LinearEaseFunction ease;
	
	private long shownAt;
	private int secondsToLive;
	

	public FlashAreaControl() {
		super();
		this.lblText = new Label("");
		this.visibility = new SimpleFloatProperty(0.5f);
		
		this.setBackground(new Background(new BackgroundFill(Color.SNOW, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPadding(new Insets(10));
		
		Button btnClose = new Button("x");
		btnClose.setOnAction(e -> {
			this.clear();
		});
		btnClose.setCursor(Cursor.HAND);
		
//		btnClose.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
		
		lblText.prefWidthProperty().bind(this.widthProperty().subtract(btnClose.widthProperty()));
		lblText.setAlignment(Pos.CENTER);
		
		this.getChildren().addAll(this.lblText, btnClose);
	}

	public void display(String message, int seconds) {
		this.display(message, seconds, false);
	}

	public void display(String message, int seconds, boolean immediate) {
		this.display(message, immediate, 15);
	}
	
	public void display(String message, boolean immediate, int seconds){
		this.lblText.setText(message);
		this.setTarget(1, immediate);
		
		this.shownAt = Instant.now().getEpochSecond();
		this.secondsToLive = seconds;
	}

	public void clear() {
		this.clear(false);
	}

	public void clear(boolean immediately) {
		this.setTarget(0, immediately);
		this.secondsToLive = 0;
	}
	
	protected void setTarget(float value, boolean immediate) {
		value = Math.max(0, Math.min(value, 1));
		if (immediate) {
			this.visibility.set(value);
		} else {
			this.ease = new LinearEaseFunction(this.visibility.get(), value, 0.1f);
		}
	}
	
	public void updateVisibility() {
		if (this.ease == null) {
			if(this.secondsToLive == 0 ){
				return;
			}
			long now = Instant.now().getEpochSecond();
			if(now - this.shownAt >= this.secondsToLive){
				this.clear();
			}
			
			return;
		}

		float newVal = this.ease.next();
		if (newVal == this.visibility.get()) {
			this.ease = null;
			return;
		}

		this.visibility.set(newVal);
	}
	
	public String getMessage(){
		return this.lblText.getText();
	}

	public float getVisibility() {
		return visibility.get();
	}
	
	public FloatProperty visibilityProperty(){
		return visibility;
	}
}
