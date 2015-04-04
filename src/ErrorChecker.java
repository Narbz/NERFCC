
/**
 * The class ErrorChecker is used to contain all of the methods used for checking
 * and validation that do NOT relate to SQL.
 */
public class ErrorChecker
{
    /**
     * Taken from http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
     */
    public static boolean checkPhoneNum(String s)
    {
        try { 
            Long.parseLong(s); 
        } catch(NumberFormatException e) { 
            return false; 
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        //Added length check as well
        if(s.length() != 10){
            return false;
        }
        return true;
    }
}
