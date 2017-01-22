package tunnelers.core.engine;

import generic.SimpleScanner;
import generic.SimpleScannerException;
import tunnelers.common.IUpdatable;
import tunnelers.network.NetAdapter;
import tunnelers.core.chat.Chat;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.settings.Settings;
import tunnelers.network.NetClient;
import tunnelers.network.command.Command;
import tunnelers.network.command.INetworkProcessor;
import tunnelers.network.command.Signal;

/**
 *
 * @author Stepan
 */
public final class Engine implements INetworkProcessor, IUpdatable {

	private final int version;

	private final int tickRate;
	private AEngineStage currentStage;

	protected final EngineUserInterface guiInterface;

	protected NetClient localClient;
	protected final NetAdapter netadapter;
	protected final PersistentString connectionSecret;

	protected final SimpleScanner commandScanner;
	protected final EngineNetworksInterface networksInterface;

	protected GameRoom currentGameRoom;

	protected IView view;
	protected AControlsManager controls;
	protected String preferredName;

	public Engine(int version, Settings settings) {
		this.version = version;
		this.netadapter = new NetAdapter(this);

		this.setStage(Stage.Menu);
		this.tickRate = settings.getTickRate();
		this.connectionSecret = new PersistentString(settings.getConnectionLogRelativePath());

		this.guiInterface = new EngineUserInterface(this);
		this.commandScanner = new SimpleScanner(SimpleScanner.RADIX_HEXADECIMAL);
		this.networksInterface = new EngineNetworksInterface(this, true);

		this.preferredName = "";
	}

	public EngineUserInterface intefrace() {
		return guiInterface;
	}

	public void setView(IView view) {
		this.view = view;
		this.controls = view.getControlsManager();

	}

	public void start() {
		this.netadapter.start();
	}

	public void setStage(Stage stage) {
		switch (stage) {
			case Menu:
				this.currentStage = new MenuStage();
				break;
			case Warzone:
				this.currentStage = new WarzoneStage(currentGameRoom);
				break;
		}
	}

	public GameRoom getGameRoom() {
		return currentGameRoom;
	}

	@Override
	public void update(long tick) {
		this.currentStage.update(tick);
		if (tick % (tickRate / 2) == 0) {
			netadapter.update(tick);
		}
	}

	public void exit() {
		this.netadapter.shutdown();
	}

	public Chat getChat() {
		if (this.currentGameRoom == null) {
			return null;
		}
		return this.currentGameRoom.getChat();
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
		try{
			return action.execute(commandScanner);
		} catch (SimpleScannerException ex){
			System.err.println(ex);
			return false;
		} catch (Exception ex){
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
			case ConnectionEstabilished:
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

	public static enum Stage {
		Menu,
		Warzone,
	}
}
