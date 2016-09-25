package tunnelers.Settings.Providers;

import tunnelers.Settings.Settings;

/**
 *
 * @author Stepan
 */
public class DefaultSettigs implements ISettingsSpecifier{

	@Override
	public void set(Settings s) {
		s.setWindowSize(800, 600);
		s.setTickRate(32);
		s.setChatMessageCapacity(32);
	}
	
}
