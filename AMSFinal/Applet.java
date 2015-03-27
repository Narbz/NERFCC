import java.util.ArrayList;
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
/**
 * This will be the main class for the AMS catalogue. Most of the logic 
 * and inputs will be collected and implemented 
 */
public class Applet
{
    //System console control objects
    private InputReader reader;
    private MessageHandler msgout;
    
    //**STATE REPRESENTATIONS**!!**
    private int state;
    private final int INITIAL   = 0;
    private final int LOGIN     = 1;
    private final int EXIT      = 666;

    /**
     * Constructor for objects of class Applet
     */
    public Applet()
    {
        msgout = new MessageHandler();
        reader = new InputReader();
        state = 0;
        start();
    }

    public static void main(String[] args){
        new Applet();
    }
    
    public void start()
    {
        
        boolean fin = false;
        //**MAIN LOOP***
        while(!fin){
            String input = reader.getInput().trim();
            if(state == INITIAL){ //or state = 0
                printToScreen(msgout.printWelcome());
            }else if(state == 1){
                
            }else if(state == 2){
                
            }else if(state == EXIT){
                printToScreen(msgout.printExit());
                fin = true;
            }else{
                printToScreen("  Internal error, state not defined, now closing...");
                fin = true;
            }
        }
    }
    
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
