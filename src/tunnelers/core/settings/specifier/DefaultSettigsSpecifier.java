package tunnelers.core.settings.specifier;

import tunnelers.core.settings.Settings;

/**
 *
 * @author Stepan
 */
public class DefaultSettigsSpecifier implements ISettingsSpecifier {

	@Override
	public void set(Settings s) {
		s.setWindowSize(800, 600);
		s.setTickRate(32);
		s.setChatMessageCapacity(32);
		s.setServerPort(4200);
		s.setServerAddress("192.168.99.2");
		s.setConnectionLogRelativePath("files/connection.txt");
	}

}
