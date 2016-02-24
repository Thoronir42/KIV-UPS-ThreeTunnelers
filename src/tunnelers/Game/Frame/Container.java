package tunnelers.Game.Frame;

import tunnelers.Game.map.TunnelMap;
import javafx.geometry.Point2D;
import tunnelers.Game.ControlSchemeManager;

/**
 *
 * @author Stepan
 */
public class Container {

	public static final int SERVER_PLAYER_ID = -1;

	public static Container mockContainer(ControlSchemeManager controlSchemeManager) {
		Player[] players = new Player[]{
			new Player("Yahoo"), new Player("Yahoo"),};
		TunnelMap map = TunnelMap.getMockMap();
		for (Player p : players) {
			Point2D baseCenter = map.getFreeBaseSpot(p);
			Tank t = new Tank(p, baseCenter);
			p.setTank(t);
		}
		Container c = new Container(players, map);
		
		byte[] controlSchemeIDs = ControlSchemeManager.getKeyboardLayoutIDs();
		for(byte i = 0; i < controlSchemeIDs.length; i++){
			controlSchemeManager.getKeyboardScheme(i).setPlayerID(players[i].getID());
		}
		
		
		return c;
	}
	

	private final PlayerSrv playerSrv;
	private final Player[] players;
	private final TunnelMap map;

	public Container(Player[] players, TunnelMap map) {
		this.playerSrv = new PlayerSrv();
		this.players = players;
		this.map = map;
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
