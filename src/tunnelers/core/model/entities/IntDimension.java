package tunnelers.core.model.entities;

import javafx.geometry.Dimension2D;

/**
 *
 * @author Skoro
 */
public class IntDimension {

	private final int width;
	private final int height;

	public IntDimension() {
		this(0, 0);
	}
	
	public IntDimension(int width, int height){
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Dimension2D fx(){
		return new Dimension2D(width, height);
	}
	
}
