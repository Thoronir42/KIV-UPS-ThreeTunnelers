package generic;

/**
 *
 * @author Stepan Sevcik, kiwi A13B0433P
 * @param <Type> type specificator
 * @deprecated This is a weird way of simulating events
 */
public abstract class BackPasser<Type> implements Runnable {

	private Type content;

	public Type get() {
		return content;
	}
	
	public abstract void run();

	public void pass(Type content) {
		this.content = content;
		this.run();
	}
}
