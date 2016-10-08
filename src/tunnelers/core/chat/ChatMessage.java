package tunnelers.core.chat;

public class ChatMessage {

	private final int color;
	private final String name;
	private final String text;
	
	public ChatMessage(String name, int color, String text){
		this.name = name;
		this.color = color;
		this.text = text;
		
	}

	@Override
	public String toString() {
		return String.format("%s : %s", name, text);
	}
	
	public String getName(){
		return name;
	}
	
	public String getText(){
		return text;
	}
	
	public int getColor(){
		return this.color;
	}
}
