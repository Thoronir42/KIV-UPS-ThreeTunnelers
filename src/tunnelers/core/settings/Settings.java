package tunnelers.core.settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import tunnelers.app.controls.ControlsManager;
import tunnelers.core.settings.specifier.DefaultSettigsSpecifier;
import tunnelers.core.settings.specifier.FileSettingsSpecifier;
import tunnelers.core.settings.specifier.ISettingsSpecifier;

/**
 *
 * @author Stepan
 */
public final class Settings {

	public static final int VERSION = 00101;

	public static final int MAX_PLAYERS = 4;

	public static final String DEFAULT_SERVER_ADDRESS = "localhost";
	public static final int DEFAULT_PORT = 8047;

	public static final String GAME_NAME = "Three Tunnelers",
			TITLE_SEPARATOR = "|";

	public static final double MIN_BLOCKS_ON_DIMENSION = 27;
	public static final int MOCK_CHUNK_SIZE = 20;

	public static int IMAGE_UPSCALE_MULT = 20;
	public static int MAX_PLAYER_PROJECTILES = 7;

	public static NameManager nameGenerator;

	static {
		nameGenerator = new NameManager(420);
	}

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

	private String serverAddress;
	private int serverPort;

	private final ControlsManager controlSchemeManager;

	private List<ISettingsSpecifier> configurators;

	private Settings() {
		this.controlSchemeManager = new ControlsManager();

		this.configurators = new ArrayList<>();
		this.configurators.add(new DefaultSettigsSpecifier());
	}

	public Settings(String configFile) {
		this();
		this.addConfigFile(configFile);
	}

	public void addConfigFile(String configFile) {
		try {
			this.configurators.add(new FileSettingsSpecifier(configFile));
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void init() {
		this.configurators.stream().forEach((configurator) -> {
			configurator.set(this);
		});

		this.serverPort = Settings.DEFAULT_PORT;
		this.serverAddress = "localhost";

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

	public String getGameName() {
		return GAME_NAME;
	}

	public String getTitleSeparator() {
		return TITLE_SEPARATOR;
	}

	public int getDefaultPort() {
		return DEFAULT_PORT;
	}

	public ControlsManager getControlSchemeManager() {
		return this.controlSchemeManager;
	}

	public void setWindowSize(int width, int height) {
		this.setWindowWidth(windowWidth);
		this.setWindowHeight(windowHeight);
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

}
