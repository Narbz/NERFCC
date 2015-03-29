import java.util.ArrayList;
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
/**
 * This will be the main class for the AMS catalogue. Most of the logic 
 * and inputs will be collected and implemented 
 * @author Chazz Young, (remember to add your names as well as to methods you contirbute to)
 */
public class Applet
{
    //System console control objects
    private InputReader reader;
    private MessageHandler msgout;
    
    //**STATE REPRESENTATIONS**!!**
    private int state;
    
    //**INITIAL STATES!!!
    private final int INITIAL       = 0;
    private final int LOGIN         = 1;
    private final int REGISTER      = 2;
    
    //**CLERK STATES!!!
    private final int CLERKSTART    = 3;
    private final int PROCESSRETURN = 4;
    private final int RETURNITEMS   = 5;
    private final int RETURNCONFIRM = 6;
    private final int RETURNFAILURE = 7;
    
    //**MANAGER STATES!!!
    private final int MGRSTART      = 10;
    private final int ADDITEMS      = 11;
    private final int ADDSUCCESS    = 12;
    private final int ADDFAILURE    = 13;
    private final int PROCDELIVERY  = 21;
    private final int PROCSUCCESS   = 22;
    private final int PROCFAILURE   = 23;
    private final int DSIRINIT      = 31;
    private final int DSRSUCCESS    = 32;
    private final int DSRFAILURE    = 33;
    private final int NTSRINIT      = 41;
    private final int NTSRSUCCESS   = 42;
    private final int NTSRFAILURE   = 43;
    
    //**CUSTOMER STATES
    private final int CUSTSTART     = 50;
    private final int SEARCHSTATE   = 51;
    private final int SELECTITEM    = 52;
    private final int CHECKQTY      = 53;
    private final int INSFSTOCK     = 54;
    private final int SEARCHFAILED  = 55;
    private final int ADDTOVSB      = 61;
    private final int VSBSUCCESS    = 62;
    private final int VSBFAILURE    = 63;
    private final int VIEWVSB       = 64;
    private final int CLEARVSB      = 65;
    private final int PLACEORDER    = 70;
    private final int CONFIRMORDER  = 71;
    private final int PAYFORORDER   = 72;
    private final int ORDERFINAL    = 73;
    private final int RECEIPT       = 74;
    private final int PRDERFAIL     = 75;
    
    
    
    
    
    
    //**OTHER STATES
    private final int EXIT          = 666;
    
    private final int PREVSTATE     = 999;

    /**
     * Constructor for objects of class Applet
     */
    public Applet()
    {
        msgout = new MessageHandler();
        reader = new InputReader();
        setState(INITIAL);
        start();
    }
    
    /**
     * Main loop method. This method contains the logical flow of the 
     * program. The order of messages and inputs are determined here.
     * Basically, this method will work using a simple while loop. First, 
     * it will check (through al lpossible states) for the given state, the n
     * print out messages and receive responses from the directions of the state.
     * At the end of each state, there is a setState() function which will 
     * determine the next state. Note that this may be teh same state as before.
     * @author Chazz Young
     */
    public void start()
    {
        
        boolean fin = false;
        //**MAIN LOOP***
        while(!fin){
            String input = getInput();
            if(state == INITIAL){ //or state = 0
                printToScreen(msgout.printWelcome());
                if(input.startsWith("yes")){
                    setState(LOGIN);
                }else if(input.startsWith("no")){
                    setState(REGISTER);
                }else{
                    //Print error message
                }
            }else if(state == LOGIN){
                printToScreen("  Please enter your username.");
                String username = getInput();
                printToScreen("  Please enter your password.");
                String password = getInput();
                
                //Method to retrieve username from SQL database 
                //Assume an id and type is returned
                if(true/*c == null*/){
                   //Print invalid user login
                   setState(LOGIN);
                }else{
                    //Print successful login message
                    if(true/* c.type == MANAGER*/){
                        setState(MGRSTART);
                    }else if(true/*c.type == CLERK*/){
                        setState(CLERKSTART);
                    }else if(true/*c.type == CUSTOMER*/){
                        setState(CUSTSTART);
                    }else{
                        //print error message, invalid user type
                    }
                }
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
//             
//         }else if(state == ){
            
                printToScreen(msgout.printExit());
                fin = true;
            }else{
                printToScreen("  Internal error, state not defined, now closing...");
                fin = true;
            }
        }
    }
    
    //**SIMPLIFYING HELPER METHODS**
    
    /**
     * Method to print to System.out. 
     * @param msg: The messages to be printed out. This is obtained 
     * form msgout, the MessageHandler
     * @author Chazz Young
     */
    private void printToScreen(ArrayList<String> msg)
    {
        for(String s : msg){
            System.out.println(s);
        }
    }
    
    /**
     * Method to print a single string to System.out. 
     * @param msg: The messages to be printed out. This is obtained 
     * from the main method
     * @author Chazz Young
     */
    private void printToScreen(String msg)
    {
        System.out.println(msg);
    }
    
    /**
     * Method to retrieve input from user
     * @author Chazz Young
     */
    private String getInput()
    {
        return reader.getInput().trim();
    }
    
    private void setState(int newState)
    {
        state = newState;
    }
    
    
    
    public static void main(String[] args){
        new Applet();
    }
}

/**
 * InputReader reads typed text input from the standard text terminal. 
 * The text typed by a user is then chopped into words, and a set of words 
 * is provided.
 * 
 * @author     Michael Kolling and David J. Barnes
 * @version    0.1
 */
class InputReader
{
    private Scanner reader;

    /**
     * Create a new InputReader that reads text from the text terminal.
     */
    public InputReader()
    {
        reader = new Scanner(System.in);
    }

    /**
     * Read a line of text from standard input (the text terminal),
     * and return it as a String.
     *
     * @return  A String typed by the user.
     */
    public String getInput()
    {
        System.out.print("> ");         // print prompt
        String inputLine = reader.nextLine();
        return inputLine;
    }
}
