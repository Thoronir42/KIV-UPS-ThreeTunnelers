package tunnelers.app.render;

import javafx.scene.canvas.GraphicsContext;
import tunnelers.core.engine.Engine;

/**
 *
 * @author Stepan
 */
public class FxRenderer {

	private final Engine engine;
	private GraphicsContext gc;
	
	public FxRenderer(Engine engine){
		this.engine = engine;
		
	}
	
	public void setGraphicsContext(GraphicsContext context){
		this.gc = context;
	}

	public void render() {
		System.err.println("Rendering not implemented");
	}
}
