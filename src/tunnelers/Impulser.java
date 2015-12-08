package tunnelers;

import generic.BackPasser;

/**
 *
 * @author Stepan
 */
public class Impulser extends Thread{
	long tickCount;
	BackPasser<Long>  executer;
	boolean stop;
	long tickDelay;
	
	public Impulser(BackPasser<Long> executer){
		this.tickCount = 0;
		this.stop = false;
		this.tickDelay = 1000 / Settings.TICK_RATE;
		this.executer = executer;
	}
	
	public long getCurrentTick(){
		return this.tickCount;
	}

	@Override
	public void run() {
		try{
			while(!stop){
				tickCount++;
				this.executer.pass(tickCount);
				sleep(tickDelay);
			}
		} catch (InterruptedException e){
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}
	}
	
	public void stopRun(){
		this.stop = true;
	}
	
}
