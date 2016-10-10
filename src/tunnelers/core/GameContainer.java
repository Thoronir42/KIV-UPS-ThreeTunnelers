package tunnelers.core;

import generic.RNG;
import tunnelers.core.model.player.APlayer;
import tunnelers.core.model.player.PlayerLocal;
import tunnelers.core.model.player.PlayerRemote;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tunnelers.core.model.map.Map;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.IO.KeyboardControls;
import tunnelers.core.model.map.MapGenerator;

/**
 *
 * @author Stepan
 */
public class GameContainer {

	public static GameContainer mockContainer(ControlSchemeManager csmgr, String localName, int maxColorId) {
		APlayer[] players = new APlayer[]{
			new PlayerLocal(47, RNG.getRandInt(maxColorId), csmgr.getKeyboardScheme((byte) 0), localName),
			new PlayerLocal(53, RNG.getRandInt(maxColorId), csmgr.getKeyboardScheme((byte) 1), "Jouda"),
			new PlayerRemote(12, RNG.getRandInt(maxColorId), "Frederick"),};
		Map map = MapGenerator.mockMap(players);
		GameContainer c = new GameContainer(players);

		c.initWarzone(map);

		byte[] controlSchemeIDs = csmgr.getKeyboardLayoutIDs();
		for (byte i = 0; i < controlSchemeIDs.length; i++) {
			KeyboardControls keyboardScheme = csmgr.getKeyboardScheme(i);
			keyboardScheme.setPlayerID(players[i].getID());
			//System.out.format("sch: %s - pid: %d\n", keyboardScheme.toString(), keyboardScheme.getPlayerID());
		}

		return c;
	}

	private Warzone warzone;
	private List<APlayer> players;

	public GameContainer(APlayer[] players) {
		this(Arrays.asList(players));
	}

	public GameContainer(int expectedPlayerCount) {
		this(new ArrayList<>(expectedPlayerCount));
	}

	public GameContainer(List<APlayer> players) {
		this.players = players;
	}

	public void initWarzone(Map map) {
		if (map != null) {
			System.err.println("Warzone had already been set");
		}
		this.warzone = new Warzone(players, map);
	}

	public Warzone getWarzone() {
		return this.warzone;
	}

	public int getPlayerCount() {
		return players.size();
	}

	public List<APlayer> getPlayers() {
		return this.players;
	}

	/**
	 *
	 * @return @deprecated use Warzone in stead
	 */
	public Map getMap() {
		return this.getWarzone().getMap();
	}

	public APlayer getPlayer(int playerId) {
		for (APlayer p : this.players) {
			if (p.getID() == playerId) {
				return p;
			}
		}
		return null;
	}

	public APlayer getLocalPlayer() {
		for (APlayer p : this.players) {
			return p;
		}
		return null;
	}

}
