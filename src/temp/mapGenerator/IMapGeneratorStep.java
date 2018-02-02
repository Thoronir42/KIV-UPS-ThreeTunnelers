package temp.mapGenerator;

import tunnelers.core.model.map.Map;

public interface IMapGeneratorStep {
	void applyOn(Map map);
}
