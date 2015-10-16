package tunnelers.Game;

import javafx.scene.paint.Color;
import tunnelers.structure.Player;

/**
 *
 * @author Stepan
 */
public class GameChat {
    
    private final int MAX_MESSAGES = 36;
    
    private ChatMessage[] messages;
    private int messageTop = 0;
    public GameChat(){
         messages = new ChatMessage[MAX_MESSAGES];
    }
    
    public void addMessage(Player p, String text){
        ChatMessage message = new ChatMessage(p, text);
        messages[messageTop++] = message;
        if(messageTop >= MAX_MESSAGES){
            messageTop = 0;
        }
    }
    
    public String getLog(){
        String chatLog = "";
        int i = messageTop - 1;
        
        while(i != messageTop && messages[i] != null){
            chatLog = messages[i].toString() + "\n" + chatLog;
            if(--i < 0){
                i = messageTop - 1;
            }
        }
        
        return chatLog;
    }
}

class ChatMessage{
    private final Player player;
    private final String text;
    
    public ChatMessage(Player player, String message){
        this.player = player;
        this.text = message;
    }
    
    @Override
    public String toString(){
        return String.format("%s : %s", player.getName(), text);
    }
    
    public Color getColor(){
        return this.player.getColor();
    }
}