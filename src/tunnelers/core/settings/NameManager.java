package tunnelers.core.settings;

import java.util.Random;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * Shamelessly stolen from http://www.java-gaming.org/index.php?topic=35802.0
 */
public final class NameManager {

	private static String[] beginning = { "Kr", "Ca", "Ra", "Mrok", "Cru",
         "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
         "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
         "Mar", "Luk" };
	private static String[] middle = { "air", "ir", "mi", "sor", "mee", "clo",
         "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer",
         "marac", "zoir", "slamar", "salmar", "urak" };
	private static String[] end = { "d", "ed", "ark", "arc", "es", "er", "der",
         "tron", "med", "ure", "zur", "cred", "mur" };
   
   
	private Random rand;
   
	public final StringProperty CurrentName;

	public NameManager(){
	   this(System.currentTimeMillis());
	}
   
	public NameManager(long seed){
		this.rand = new Random(seed);
		CurrentName = new SimpleStringProperty(generateNext());
	}
   
	public String generateNext() {
		return beginning[rand.nextInt(beginning.length)] + 
            middle[rand.nextInt(middle.length)]+
            end[rand.nextInt(end.length)];

	}

}