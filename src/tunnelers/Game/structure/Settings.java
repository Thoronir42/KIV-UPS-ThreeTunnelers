package tunnelers.Game.structure;

import javafx.scene.paint.Color;


/**
 *
 * @author Stepan
 */
public class Settings {
    public static final int MIN_PLAYERS = 2,
                             MAX_PLAYERS = 4;
    public static final int TICK_RATE = 30;
    
    public static final int DEFAULT_PORT = 8047;
    
    public static final int WIDTH_DEFAULT = 800,
                           HEIGHT_DEFAULT = 600;
    
    public static final String GAME_NAME = "Three Tunnelers",
                               TITLE_SEPARATOR = "|";
    public static final Color[] PLAYER_COLORS;
    public static final boolean[] PLAYER_COLOR_USAGE;
    
    static{
        PLAYER_COLORS = preparePlayerColors();
        PLAYER_COLOR_USAGE = preparePlayerColorUsage(PLAYER_COLORS.length);
    }

    static Color getColor(Color color, int colorId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private int playerCount;
    private int width = WIDTH_DEFAULT, height = HEIGHT_DEFAULT;
    private final long delay;
    
    private static Settings instance;
    
    private static Color[] preparePlayerColors(){
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
    private static boolean[] preparePlayerColorUsage(int n){
        boolean[] usage = new boolean[n];
        for (int i = 0; i < n; i++) {
            usage[i] = false;
        }
        return usage;
    }
    
    
    private Settings(){
        this.playerCount = MIN_PLAYERS;
        this.delay = (long)(1000.0 / TICK_RATE);
    }
    
    public static Settings getInstance(){
        if(instance == null){
            instance = new Settings();
        }
        return instance;
    }
    
    public String getGameName(){
        return GAME_NAME;
    }
    public String getTitleSeparator(){
        return TITLE_SEPARATOR;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public int getTickrate() {
        return TICK_RATE;
    }
    public long getDelay(){
        return this.delay;
    }

    public int getDefaultPort() {
        return DEFAULT_PORT;
    }
    
}
