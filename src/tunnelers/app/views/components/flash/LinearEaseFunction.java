package tunnelers.app.views.components.flash;

/**
 *
 * @author Stepan
 */
public class LinearEaseFunction {

	private final float start;
	private final float end;

	private final float step;

	private float value;

	public LinearEaseFunction(float start, float end, float speed) {
		this.value = this.start = start;
		this.end = end;
		this.step = Math.signum(end - start) * speed;
	}

	public float next() {
		float newVal = value + step;
		
		if (Math.abs(newVal - start) > Math.abs(end - start)) {
			newVal = end;
		}

		return value = newVal;
	}
}
