package tunnelers.core.engine;

import generic.SimpleScanner;
import generic.SimpleScannerException;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarZoneStage;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.GameRoomState;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.settings.Settings;
import tunnelers.network.Networks;
import tunnelers.network.NetClient;
import tunnelers.network.command.Command;
import tunnelers.network.handling.INetworkProcessor;
import tunnelers.network.command.Signal;

public final class Engine extends Thread implements INetworkProcessor {

	private final int tickRate;
	private AEngineStage currentStage;

	private final EngineUserInterface guiInterface;

	protected NetClient localClient;
	protected final Networks netadapter;
	protected final PersistentString connectionSecret;

	private final SimpleScanner commandScanner;
	private final EngineNetworksInterface networksInterface;

	protected GameRoom currentGameRoom;

	protected IView view;
	protected AControlsManager controls;
	protected String preferredName;

	private final int tickDelay;
	private long currentTick;
	private boolean keepRunning;

	public Engine(Settings settings, Networks adapter) {
		this.netadapter = adapter;

		this.setStage(Stage.Menu);
		this.tickRate = settings.getTickRate();
		this.connectionSecret = new PersistentString(settings.getConnectionLogRelativePath());

		this.guiInterface = new EngineUserInterface(this);
		this.commandScanner = new SimpleScanner(SimpleScanner.RADIX_HEXADECIMAL);
		this.networksInterface = new EngineNetworksInterface(this, true);

		this.preferredName = "";

		this.tickDelay = 1000 / settings.getTickRate();
	}

	public EngineUserInterface userInterface() {
		return guiInterface;
	}

	public void setView(IView view) {
		this.view = view;
		this.controls = view.getControlsManager();

	}

	@Override
	public void start() {
		super.start();
		this.netadapter.start();
	}

	public void setStage(Stage stage) {
		switch (stage) {
			case Menu:
				this.currentStage = new MenuStage();
				break;
			case Warzone:
				this.currentStage = new WarZoneStage(currentGameRoom);
				break;
		}
	}

	public void setCurrentGameRoomState(GameRoomState state) {
		this.currentGameRoom.setState(state);
		for (NetClient c : currentGameRoom.getClients()) {
			if (c == null) {
				continue;
			}
			c.setReady(false);
		}
	}

	public GameRoom getGameRoom() {
		return currentGameRoom;
	}

	@Override
	public void run() {
		this.currentTick = 0;
		this.keepRunning = true;

		try {
			while (this.keepRunning) {
				this.currentTick++;

				try {
					this.currentStage.update(this.currentTick);
				} catch (Exception e) {
					System.err.println("Engine stage update failed:");
					e.printStackTrace();
				}
				try {
					if (this.currentTick % (tickRate / 2) == 0) {
						this.netadapter.update(this.currentTick);
					}
				} catch (Exception e) {
					System.err.println("Network update failed:");
					e.printStackTrace();
				}
				try {
					this.view.update(this.currentTick);
				} catch (Exception e) {
					System.err.println("View update failed:");
					e.printStackTrace();
				}

				sleep(tickDelay);
			}
		} catch (InterruptedException e) {
			System.err.println("Engine has been interrupted. Shutting everything down");
			this.exit();
		}

	}

	public void exit() {
		this.keepRunning = false;
		this.netadapter.shutdown();
		this.view.exit();

	}

	@Override
	public boolean handle(Command cmd) {
		System.out.format("Engine processing command '%s': [%s]\n", cmd.getType().toString(), cmd.getData());
		commandScanner.setSourceString(cmd.getData());
		IAction action = this.networksInterface.getAction(cmd.getType());

		if (action == null) {
			System.err.format("Engine: No action for %s\n", cmd.getType());
			return false;
		}
		try {
			return action.execute(commandScanner);
		} catch (SimpleScannerException ex) {
			System.err.println(ex.toString());
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			this.netadapter.disconnect("Unhandled exception");
			return true;
		}
	}

	@Override
	public void signal(Signal signal) {
		System.out.println("Engine: processing signal " + signal.getType());
		view.setConnectEnabled(true);
		switch (signal.getType()) {
			case ConnectionEstablished:
				this.localClient = new NetClient();
				view.showScene(IView.Scene.GameRoomList);
				break;
			case UnknownHost:
				view.alert("Adresa nebyla rozpoznána: " + signal.getMessage());
				break;
			case ConnectingTimedOut:
				view.alert("Čas pro navázání spojení vypršel");
				break;
			case ConnectionNoRouteToHost:
				view.alert("Připojení k internetu není k dispozici");
				break;
			case ConnectingFailedUnexpectedError:
				view.alert("Neznámá chyba navazování spojení: " + signal.getMessage());
				break;
			case ConnectionReset:
				this.localClient = null;
				view.alert("Spojení bylo ukončeno: " + signal.getMessage());
				view.showScene(IView.Scene.MainMenu);
				break;
		}
	}

	public enum Stage {
		Menu,
		Warzone,
	}
}
