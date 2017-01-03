package tunnelers.core.view;

import tunnelers.core.colors.PlayerColorManager;

/**
 *
 * @author Stepan
 */
public interface IColorScheme {
	public int getAvailablePlayerColors();
	
	public PlayerColorManager getPlayerColorManager();
}
