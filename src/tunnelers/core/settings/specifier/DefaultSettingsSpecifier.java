package tunnelers.core.settings.specifier;

import tunnelers.core.settings.Settings;

public class DefaultSettingsSpecifier implements ISettingsSpecifier {

	@Override
	public void initialize(Settings s) {
		s.setWindowSize(800, 600);
		s.setTickRate(32);
		s.setChatMessageCapacity(32);
		s.setServerPort(4200);
		s.setServerAddress("192.168.56.101");
		s.setConnectionLogRelativePath("files/connection.txt");
		s.setControlSchemesCount(1);
	}

}
