package generic;

import java.util.Random;

/**
 *
 * Shamelessly stolen from http://www.java-gaming.org/index.php?topic=35802.0
 */
public class NameGenerator {

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

   public NameGenerator(){
	   this(System.currentTimeMillis());
   }
   
   public NameGenerator(long seed){
		this.rand = new Random(seed);
	   
   }
   
   public String next() {
      return beginning[rand.nextInt(beginning.length)] + 
            middle[rand.nextInt(middle.length)]+
            end[rand.nextInt(end.length)];

   }

}