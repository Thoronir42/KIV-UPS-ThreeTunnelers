package tunnelers.Game;

/**
 *
 * @author Stepan
 */
public class Impulser extends Thread{
	long tickCount;
	Runnable executer;
	
	public Impulser(Runnable executer){
		tickCount = 0;
		this.executer = executer;
	}

	@Override
	public void run() {
		tickCount++;
		this.executer.run();
	}
	
	
}
