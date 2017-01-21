package generic.Impulser;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;

/**
 *
 * @author Stepan
 */
public class Impulser extends Thread {

	long tickCount;
	boolean stop;
	long tickDelay;
	
	private final List<EventHandler<TickEvent>> hooks;

	public Impulser(int tickRate) {
		this.tickCount = 0;
		this.stop = false;
		this.tickDelay = 1000 / tickRate;
		
		this.hooks = new ArrayList<>();
	}
	
	
	
	public void addHook(EventHandler<TickEvent> handler){
		this.hooks.add(handler);
	}

	public long getCurrentTick() {
		return this.tickCount;
	}

	@Override
	public void run() {
		try {
			while (!stop) {
				tickCount++;
				TickEvent event = new TickEvent(tickCount);
				this.hooks.stream().forEach((handler) -> {
					handler.handle(event);
				});
				
				sleep(tickDelay); // TODO? optimize
			}
		} catch (InterruptedException e) {
			System.err.println("Impulser has been interrupted. Shutting everything down");
			Platform.exit();
		}
	}

	public void stopRun() {
		this.stop = true;
		this.hooks.clear();
	}
}
