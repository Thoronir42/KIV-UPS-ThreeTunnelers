package generic;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import tunnelers.network.ConnectionCommand;
import tunnelers.network.GameCommand;
import tunnelers.network.LobbyCommand;
import tunnelers.network.MessageCommand;

/**
 *
 * @author Stepan
 */
public class NetCmdFormatter {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Class[] groups = { ConnectionCommand.class, LobbyCommand.class,
			GameCommand.class, MessageCommand.class
		};
		for(Class c : groups){
			scanAndFormat(c);
			Runtime.getRuntime().exit(0);
		}		
	}
	
	private static void scanAndFormat(Class c){
		char aspect = '#';
		try{
			Field fAspect = c.getField("ASPECT_LETTER");
			aspect = (char)fAspect.get(null);
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		System.out.format("%s (povaha %s)\n", c.getSimpleName(), aspect);
		printCommands(c, false);
		System.out.println("\n");
		
	}
	private static void printCommands(Class c, boolean recievable){
		Class[] commands = c.getClasses();
		Class rec = null;
		for(Class cmd : commands){
			if("Recievable".equals(cmd.getSimpleName())){
				rec = cmd;
			} else {
				String commandString = formatSingleCommand(cmd);
				System.out.println(commandString);
			}
		}
		if(rec!= null){
			System.out.println("\n\t Recievable");
			printCommands(rec, true);
		}
		else if (!recievable) {
			System.err.println(c.getSimpleName() + " doesn't have recievable");
		}
	}
	
	private static String formatSingleCommand(Class c){
		try{
			String name = c.getSimpleName(),
					cmdType = (String)c.getField("CMD_TYPE").get(null);
			Constructor constructor = c.getConstructors()[0];
			String constrParams = getConstructorParameters(constructor);
			return String.format("%s\t%s(%s)", cmdType, name, constrParams);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e ){
			System.err.println(e.getMessage());
		}
		return null;
	}

	private static String getConstructorParameters(Constructor constructor) {
		
		AnnotatedType[] params = constructor.getAnnotatedParameterTypes();
		if(params.length < 1){
			return "";
		}
		StringBuilder res = new StringBuilder();
		
		
		for(int i = 0; i < params.length; i++){
			if(i>0)
				res.append(", ");
			
			String type = params[i].getType().getTypeName();
			String[] ts = type.split("\\.");
			res.append(String.format("%s", ts[ts.length-1]));
		}
		return res.toString();
	}
	
}
