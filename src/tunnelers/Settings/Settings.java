package tunnelers.Settings;

import tunnelers.Settings.Providers.DefaultSettigs;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Settings.Providers.FileSettingsProvider;
import tunnelers.Settings.Providers.ISettingsSpecifier;

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
	public static final Color[] PLAYER_COLORS;

	private static final Random RNG;
	public static final double MIN_BLOCKS_ON_DIMENSION = 27;
	public static final int MOCK_CHUNK_SIZE = 20;

	public static int IMAGE_UPSCALE_MULT = 20;
	public static int MAX_PLAYER_PROJECTILES = 7;
	
	public static NameManager nameGenerator;
	

	static {
		PLAYER_COLORS = preparePlayerColors();
		RNG = new Random(420);
		nameGenerator = new NameManager(420);
	}

	private static Settings instance;
	
	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public static int getRandInt(int i) {
		return RNG.nextInt(i);
	}

	private static Color[] preparePlayerColors() {
		Color[] colors = new Color[]{
			Color.web("0x55D43F"), // 01. green
			Color.web("0x000084"), // 02. navy blue
			Color.web("0xFF6600"), // 03. orange
			Color.web("0x663399"), // 04. purple
			Color.web("0xFF0080"), // 05. deep pink
			Color.web("0x3399FF"), // 06. blue
			Color.web("0xFFC200"), // 07. orange yellow
			Color.web("0x9C2A00"), // 08. red orange
			Color.web("0xFEFCD7"), // 09. moon glow
			Color.web("0xAA0078"), // 10. fuchsia
			Color.web("0xFE4902"), // 11. vermilion
			Color.web("0x005A04"), // 12. camarone
			Color.web("0x66A7C5"), // 13. teal
		};
		return colors;
	}

	private static boolean[] preparePlayerColorUsage(int n) {
		boolean[] usage = new boolean[n];
		for (int i = 0; i < n; i++) {
			usage[i] = false;
		}
		return usage;
	}

	private int windowWidth;
	private int windowHeight; 
	
	private int tickRate;
	
	private int chatMessageCapacity;

	public final boolean[] playerColorUsage;

	private String serverAddress;
	private int serverPort;

	private final ControlSchemeManager controlSchemeManager;
	
	private List<ISettingsSpecifier> configurators;
	
	private Settings() {
		this.playerColorUsage = preparePlayerColorUsage(PLAYER_COLORS.length);
		this.controlSchemeManager = new ControlSchemeManager();
		
		this.configurators = new ArrayList<>();
		this.configurators.add(new DefaultSettigs());
	}
	
	public Settings(String configFile){
		this();
		this.addConfigFile(configFile);
	}
	
	public void addConfigFile(String configFile) {
		try{
			this.configurators.add(new FileSettingsProvider(configFile));
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

	public Color getColor(Color color, int colorId) {
		boolean available = (colorId >= 0 && colorId < PLAYER_COLORS.length) && !this.playerColorUsage[colorId];
		int oldCol = (color == null) ? -1 : Arrays.asList(PLAYER_COLORS).indexOf(color);

		if (oldCol == -1) {
			if (available) {
				this.playerColorUsage[colorId] = true;
				return PLAYER_COLORS[colorId];
			} else {
				int unused = this.colorFirstUnused();
				if (unused != -1) {
					return this.getColor(null, unused);
				} else {
					return PLAYER_COLORS[getRandInt(PLAYER_COLORS.length)];
				}
			}
		} else {
			if (!available || oldCol == colorId) {
				return color;
			}
			this.playerColorUsage[oldCol] = false;
			return getColor(null, colorId);
		}
	}

	private int colorFirstUnused() {
		for (int i = 0; i < PLAYER_COLORS.length; i++) {
			if (!this.playerColorUsage[i]) {
				return i;
			}
		}
		return -1;
	}

	public ControlSchemeManager getControlSchemeManager() {
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
