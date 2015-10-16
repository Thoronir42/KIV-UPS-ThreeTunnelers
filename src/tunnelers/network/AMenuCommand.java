package tunnelers.network;

/**
 *
 * @author Stepan
 */
public abstract class AMenuCommand implements INetworkCommand{

    protected static final char AREA_LETTER = 'M';
    
    @Override
    public String getCommandCode() {
        return this.getAreaLetter() + this.getCommandString();
    }
    
    protected char getAreaLetter(){
        return AREA_LETTER;
    }
    
    protected abstract char   getCommandTypeLetter();
    protected abstract String getCommandString();
    
}
