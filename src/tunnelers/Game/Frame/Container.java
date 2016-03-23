package tunnelers.Game.Frame;

import java.util.ArrayList;
import tunnelers.Game.Map.TunnelMap;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.IO.ControlScheme;
import tunnelers.Game.Map.MapGenerator;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class Container {

	public static Container mockContainer(ControlSchemeManager controlSchemeManager) {
		Player[] players = new Player[]{
			new PlayerLocal(47, Settings.getRandInt(Settings.PLAYER_COLORS.length), "Jouda"),
			new PlayerLocal(53, Settings.getRandInt(Settings.PLAYER_COLORS.length)),
			new PlayerRemote(12, Settings.getRandInt(Settings.PLAYER_COLORS.length), "Frederick"),
		};
		TunnelMap map = MapGenerator.mockMap(players);
		Container c = new Container(players, map);
		
		byte[] controlSchemeIDs = ControlSchemeManager.getKeyboardLayoutIDs();
		for(byte i = 0; i < controlSchemeIDs.length; i++){
			ControlScheme.Keyboard keyboardScheme= controlSchemeManager.getKeyboardScheme(i);
			keyboardScheme.setPlayerID(players[i].getID());
			//System.out.format("sch: %s - pid: %d\n", keyboardScheme.toString(), keyboardScheme.getPlayerID());
		}
		
		
		return c;
	}
	

	private final Player[] players;
	private final ArrayList<Projectile> projectiles;
	private final TunnelMap map;

	public Container(Player[] players, TunnelMap map) {
		this.players = players;
		this.projectiles = new ArrayList<>(Settings.MAX_PLAYERS * Settings.MAX_PLAYER_PROJECTILES);
		this.map = map;
		
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public int getPlayerCount() {
		return players.length;
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public TunnelMap getMap() {
		return this.map;
	}

	public Player getPlayer(int playerId) {
		for (Player p : this.players) {
			if (p.getID() == playerId) {
				return p;
			}
		}
		return null;
	}

}
