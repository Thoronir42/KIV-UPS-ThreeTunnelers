package tunnelers.Game;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import tunnelers.Game.structure.TunnelMap;
import tunnelers.Settings;
import static tunnelers.Settings.PLAYER_COLORS;

/**
 *
 * @author Stepan
 */
public class TunColors {
    
    private static final Color breakable = Color.DARKSALMON;
    private static final Color tough = Color.DARKGREY;
    private static final Color empty = Color.DARKRED;
    
    public static Color getBlockColor(int x, int y, TunnelMap.Block block){
        switch(block){
            default: return getRandColor();
            case Breakable: return breakable;
            case Tough: return tough;
            case Empty: return empty;
        }
    }
    
    public static Color getChunkColor(int x, int y){
        Color[] cols = PLAYER_COLORS;
        int i = (x * 2 + y * 7) % cols.length;
        return cols[i];
    }
    
    

    public static Color getRandColor(double opacity) {
        int i = Settings.getRandInt(PLAYER_COLORS.length);
        Color c = PLAYER_COLORS[i];
        
        return Color.color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
    }
    
    
    public static Color getRandColor(){
        return getRandColor(1);
    }

    public static Paint getRandStatic(int col, int row, double pct) {
        return getRandColor(0.2);
    }
}
