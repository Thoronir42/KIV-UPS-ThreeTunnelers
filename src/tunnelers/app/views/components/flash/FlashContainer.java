package tunnelers.app.views.components.flash;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

/**
 *
 * @author Skoro
 */
public class FlashContainer {
	
	private static FlashContainer instance;
	
	public static FlashContainer getInstance(){
		if(instance == null){
			instance = new FlashContainer();
		}
		
		return instance;
	}
	
	
	private final SimpleFloatProperty visibility = new SimpleFloatProperty(0.5f);
	
	private String message;
	private LinearEaseFunction ease;
	
	public FloatProperty visibilityProperty(){
		return visibility;
	}
	
	public boolean updateVisibility() {
		if (this.ease == null) {
			return false;
		}

		float newVal = this.ease.next();
		if (newVal == this.visibility.get()) {
			this.ease = null;
			return false;
		}

		this.visibility.set(newVal);

		return true;
	}
	
	protected void setTarget(float value, boolean immediate) {
		value = Math.max(0, Math.min(value, 1));
		if (immediate) {
			this.visibility.set(value);
		} else {
			this.ease = new LinearEaseFunction(this.visibility.get(), value, 0.1f);
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
