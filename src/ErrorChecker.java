
/**
 * The class ErrorChecker is used to contain all of the methods used for checking
 * and validation that do NOT relate to SQL.
 */
public class ErrorChecker
{
    /**
     * Taken from http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
     */
    public static boolean checkNumLength(String s, int length)
    {
        if(s.length() != length){
            return false;
        }
        String[] snums = s.split("");
        for(String a : snums){
            try{
                Integer.parseInt(a);
            }catch(NumberFormatException e){
                return false;
            }
        }
        return true;
    }
    
    /**
     * @author Chazz
     */
    public static boolean isValidCategory(String s)
    {
        if(s.toLowerCase().equals("rock")){
            return true;
        }else if(s.toLowerCase().equals("pop")){
            return true;
        }else if(s.toLowerCase().equals("rap")){
            return true;
        }else if(s.toLowerCase().equals("country")){
            return true;
        }else if(s.toLowerCase().equals("classical")){
            return true;
        }else if(s.toLowerCase().equals("new age")){
            return true;
        }else if(s.toLowerCase().equals("instrumental")){
            return true;
        }
        return false; //if it is not one of the above
    }
    
    /**
     * @author Chazz
     */
    public static boolean isCDDVD(String s)
    {
        if(s.equals("CD") || s.equals("DVD")){
            return true;
        }
        return false;
    }
    
        
    /**
     * Boolean check to ensure that the number entered is a valid positive integer
     * @return the nummber if it is a positive integer, or -1 otherwise
     * @author Chazz
     */
    public static int getNum(String s)
    {
        int toReturn = 0;
        try{
            toReturn = Integer.parseInt(s);
            if(toReturn > 0){
                return toReturn;
            }
        }catch(NumberFormatException e){
            
        }
        return -1; //if the number is not a positive integer
    }
    
    public static float getPrice(String s)
    {
        float toReturn = 0;
        try{
            toReturn = Float.parseFloat(s);
        }catch(NumberFormatException e){
            return -1;
        }
        return (float)Math.round(toReturn * 100)/100;
    }
    
    /**
     * Returns the UPC code if it has been formatted correctly, or null otherwise
     * @author Chazz Young
     */
    public static String getUPC(String input)
    {
        try{
            Long.parseLong(input);
            if(input.length() != 12){
                return null;
            }
        }catch(NumberFormatException e){
            return null;
        }
        return input;
    }
}
