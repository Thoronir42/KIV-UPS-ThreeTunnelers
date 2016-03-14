package tunnelers.Game.Frame;

import java.util.ArrayList;
import tunnelers.Game.Map.TunnelMap;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.Map.MapGenerator;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class Container {

	public static final int SERVER_PLAYER_ID = -1;

	public static Container mockContainer(ControlSchemeManager controlSchemeManager) {
		Player[] players = new Player[]{
			new Player("Yahoo"), new Player("Yahoo"),};
		TunnelMap map = MapGenerator.mockMap(players);
		Container c = new Container(players, map);
		
		byte[] controlSchemeIDs = ControlSchemeManager.getKeyboardLayoutIDs();
		for(byte i = 0; i < controlSchemeIDs.length; i++){
			controlSchemeManager.getKeyboardScheme(i).setPlayerID(players[i].getID());
		}
		
		
		return c;
	}
	

	private final PlayerSrv playerSrv;
	private final Player[] players;
	private final ArrayList<Projectile> projectiles;
	private final TunnelMap map;

	public Container(Player[] players, TunnelMap map) {
		this.playerSrv = new PlayerSrv();
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
		if (playerId == SERVER_PLAYER_ID) {
			return this.playerSrv;
		}

		for (Player p : this.players) {
			if (p.getID() == playerId) {
				return p;
			}
		}
		return null;
	}

}
