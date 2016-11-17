package tunnelers.core.engine.stage;

import tunnelers.app.IUpdatable;

/**
 *
 * @author Stepan
 */
public abstract class AEngineStage implements IUpdatable{
	
	@Override
	public abstract void update(long tick);
}
