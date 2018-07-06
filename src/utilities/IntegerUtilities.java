package utilities;

public class IntegerUtilities {

	public static int getInt(String str){
		int i=0;
		try{
		 i = Integer.parseInt(str);
		
		}
		catch(NumberFormatException n){
			i= -1;
		}
		return i;
	}
	
}
