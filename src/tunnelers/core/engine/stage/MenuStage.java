package tunnelers.core.engine.stage;

/**
 *
 * @author Stepan
 */
public class MenuStage extends AEngineStage{

	@Override
	public void update(long tick) {
		if(tick % 32 == 0){
			System.out.println("Engine is sleeping: " + tick);
		}
	}
	
}
