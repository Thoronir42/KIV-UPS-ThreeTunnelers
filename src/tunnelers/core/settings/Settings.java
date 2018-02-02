package tunnelers.core.settings;

import tunnelers.core.settings.specifier.DefaultSettingsSpecifier;
import tunnelers.core.settings.specifier.FileSettingsSpecifier;
import tunnelers.core.settings.specifier.ISettingsSpecifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Settings {

	public static final int MAX_PLAYERS = 4;
	public static final int TUNNELER_DEFAULT_PORT = 4200;

	private static Settings instance;

	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	private int windowWidth;
	private int windowHeight;

	private int tickRate;

	private int chatMessageCapacity;

	private String connectionLogRelativePath;

	private String serverAddress;
	private int serverPort;

	private final List<ISettingsSpecifier> configurators;

	private Settings() {
		this.configurators = new ArrayList<>();
		this.configurators.add(new DefaultSettingsSpecifier());
	}

	public Settings(String configFile) {
		this();
		this.addConfigFile(configFile);
	}

	public void addConfigFile(String configFile) {
		try {
			this.configurators.add(new FileSettingsSpecifier(configFile));
		} catch (IOException e) {
			System.err.println("Error with adding config file: " + e.getMessage());
		}
	}

	public void init() {
		this.configurators.forEach((configurator) -> configurator.initialize(this));

		this.configurators.clear();
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {

		this.serverPort = serverPort;
	}

	public void setWindowSize(int width, int height) {
		this.setWindowWidth(width);
		this.setWindowHeight(height);
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public int getTickRate() {
		return tickRate;
	}

	public void setTickRate(int tickRate) {
		this.tickRate = tickRate;
	}

	public int getChatMessageCapacity() {
		return chatMessageCapacity;
	}

	public void setChatMessageCapacity(int chatMessageCapacity) {
		this.chatMessageCapacity = chatMessageCapacity;
	}

	public String getConnectionLogRelativePath() {
		return connectionLogRelativePath;
	}

	public void setConnectionLogRelativePath(String connectionLogRelativePath) {
		this.connectionLogRelativePath = connectionLogRelativePath;
	}
}
