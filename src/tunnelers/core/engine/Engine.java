package tunnelers.core.engine;

import tunnelers.model.entities.Direction;
import tunnelers.model.entities.Tank;
import tunnelers.model.player.Controls;
import tunnelers.model.player.APlayer;
import tunnelers.model.player.PlayerRemote;
import java.util.Collection;
import javafx.geometry.Point2D;
import tunnelers.Game.Chat.Chat;
import tunnelers.Game.IO.InputAction;
import tunnelers.core.GameContainer;
import tunnelers.core.Warzone;
import tunnelers.network.NetWorks;
import tunnelers.network.command.GameCommand;
import tunnelers.network.command.MessageCommand;
import tunnelers.network.command.NCG;

/**
 *
 * @author Stepan
 */
public final class Engine {
	
	private final GameContainer container;
	private final NetWorks networks;
	private final Chat chat;	
	
	public Engine(GameContainer container, NetWorks networks, Chat chat){
		this.container = container;
		this.networks = networks;
		this.chat = chat;
	}

	public GameContainer getContainer() {
		return container;
	}
	
	public APlayer getPlayer(int n){
		return this.container.getPlayer(n);
	}
	
	public void update(long tick){
		Warzone warzone = this.container.getWarzone();
		if(warzone != null){
			warzone.update();
		}
		updatePlayers(this.container.getPlayers(), tick);
	}
	
	public void handleNetworkCommand(NCG.NetCommand command) {
		if (command instanceof MessageCommand.Plain) {
			MessageCommand.Plain cmd = (MessageCommand.Plain) command;
			String msg = cmd.getMessageText();
			APlayer p = this.getPlayer(cmd.getPlayerId());
			this.chat.addMessage(p, msg);
		} else {
			System.err.println("Incomming command not recognised");
		}
	}
	
	private void updatePlayers(Collection<APlayer> players, long tick) {
		for (APlayer p : players) {
			if(p instanceof PlayerRemote && tick % 15 == 0){
				((PlayerRemote)p).mockControls(tick);
			}
			Tank tank = p.getTank();
			Controls c = p.getControls();
			
			tank.update();
			tankShoot(tank, true && c.isShooting());		// TODO: omezeni poctu strel
			updateTank(tank, c.getDirection());	
		}
	}

	protected void tankShoot(Tank tank, boolean tryShoot){
		if(tryShoot){
			System.out.print("Click... ");
			if(tank.tryShoot() != null){
				System.out.print("BANG!!!");
			}
			System.out.println("");
		}
	}
	
	protected Point2D updateTank(Tank tank, Direction d) {if (d == null) {
			return null;
		}
		Point2D plr_loc = tank.getLocation();
		double newX = plr_loc.getX() + d.getX(),
				newY = plr_loc.getY() + d.getY();

		if ((newY - tank.getHeight() / 2 > 0)
				&& (newX - tank.getWidth() / 2 > 0)
				&& (newX + tank.getWidth() / 2 < this.container.getMap().getWidth())
				&& (newY + tank.getHeight() / 2 < this.container.getMap().getHeight())) {
			tank.setLocation(new Point2D(newX, newY));
			tank.setDirection(d);
		}
		
		return tank.getLocation();
	}

	public void exit() {
		this.networks.close();
	}

	public void handleInput(InputAction inp, int playerID, boolean pressed) {
		APlayer p = this.getPlayer(playerID);
		
		if (p.getControls().handleControl(inp, pressed)) {
			NCG.NetCommand cmd = new GameCommand.ControlSet(inp.intVal(), pressed ? 1 : 0);
		}
	}
}
