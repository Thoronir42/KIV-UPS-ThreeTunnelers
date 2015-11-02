package tunnelers.Game;

import javafx.scene.paint.Color;
import tunnelers.Game.structure.TunnelMap;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class TunColors {
    
    private static Color breakable = Color.DARKSALMON;
    private static Color tough = Color.DARKGREY;
    private static Color empty = Color.DARKRED;
    public static Color getBlockColor(int x, int y, TunnelMap.Block block){
        switch(block){
            default: return Settings.getRandColor();
            case Breakable: return breakable;
            case Tough: return tough;
            case Empty: return empty;
        }
    }
}
