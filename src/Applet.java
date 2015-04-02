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
    private final int INVOP         = 3;
    
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
    
    //** SYSTEM VARIABLES!
    private final boolean y = true; 
    private final boolean n = false;

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
            if(state == INITIAL){/**@author Chazz*/
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
            }else if(state == REGISTER){/**@author Narbeh */
                printToScreen("  You’re only a few steps away from creating your AMS account!");  
                printToScreen("  Firstly, please enter your first and last name:");
                String name = getInput();
                /*printToScreen("  Please enter your username: ");
                String username = getInput();*/ //done later 
                printToScreen("  Please enter your address: ");
                String address = getInput();
                printToScreen("  Please enter your city of residence: ");
                String city = getInput();
                printToScreen("  Please enter your phone number(xxx-xxx-xxxx): ");
                String phonenum = getInput();
                //TODO make a validate phone number check
                boolean b = false;
                while(b = true/*improperPhoneNumber*/){
                    printToScreen("  The phone number you enter is not a valid phone number.");  
                    printToScreen("  A valid number consists of digits 0-9 i.e 123-456-7891.");  
                    printToScreen("  Please re-enter your phone number: (xxx-xxx-xxxx)");
                    phonenum = getInput();
                }
                printToScreen("  Please enter your username that you will use to log in: ");
                String username = getInput();
                //TODO sql query to check if user name already exists
                while(b = true /*userNameExists*/){ 
                    printToScreen("  Sorry, that username is already taken. Please enter another: ");
                    username = getInput();
                }
                printToScreen("  Please enter your password: ");
                String password = getInput();
                printToScreen("  Please review your information to ensure it is correct.");
                printToScreen("  If amendments need to be made please enter the number corresponding");
                printToScreen("  to the field you would like to edit. Enter fin if you are satisfie");
                String toEdit = getInput();
                //<system displays fields enter by user in a numbered list format>
                /*boolean fin = false;
                if(true/*user enters fin){
                    fin = true;
                }
                while(!fin){
                    <user enters field number>
                    getFieldName + prevEnteredField
                    printToScreen(" please enter the new information: ");
                    <user enters new information>
                    printToScreen("  Do you want to make any more changes" Enter fin if"); 
                    printToScreen("  satisfied, otherwise enter N if you have further edits to make");
                    if(user entered fin)
                    fin = true
                }*/
                setState(CUSTSTART);
                 
            }else if(state == INVOP){
                
            }else if(state == CLERKSTART){
                
            }else if(state == PROCESSRETURN){/**@author Chazz */
                printToScreen("  You have chosen to process a return.");
                printToScreen("  Please enter the receipt ID of the item(s) the customer wishes to return:");
                String rid = getInput();
                //<query database for valid receipt id and whether or not items can be returned>
                boolean canReturn = true;//<aboveanswer;
                if(canReturn){
                    printToScreen("  The items for this receipt number can be returned.");
                    printToScreen("  Here are the items on the receipt:");
                    //<print enumerated list of items>
                    setState(RETURNITEMS);
                }else{
                    printToScreen("  The items in this receipt cannot be returned because");
                    printToScreen("  it either doesn't exist or it is over 15 days old");
                    setState(CLERKSTART);
                }
            }else if(state == RETURNCONFIRM){/**@author Chazz */
                printToScreen("  Attempting to process the return for the given items…");
                //<add items to return table, remove from order???>
                boolean success = true;//above
                if(success){
                	printToScreen("  The return order has been processed successfully.");
                	//<print amount to return>
                }else{
                	printToScreen("The database has failed to process the return order."); 
                }
                setState(CLERKSTART);
            }else if(state == MGRSTART){
                
            }else if(state == ADDITEMS){/**@author Narbeh*/
                printToScreen("  Please enter the UPC of the item you would like to add to stock: ");
                String upc = getInput();
                printToScreen("  Please enter the quantity: ");
                int qty = Integer.parseInt(getInput());
                //Check to ensure that this parses correctly
                if(true/*isExistUPC*/){
                	//execute updateItemStock
                }else{//this means the item is a new one
                    printToScreen("  Could not update.  This is a new item not currently in the inventory."); 
                    printToScreen("  Would you like to add it to the inventory? Y/N");
                    boolean addToInv = yesno(getInput());
                    if(addToInv){
                		printToScreen("  Please enter the TITLE of the item: ");
                		String itemTitle = getInput();
                		printToScreen("  Please enter the TYPE of the item: ");
                		String type = getInput();
                		printToScreen("  Please enter the CATEGORY of the item: ");
                		String category = getInput();
                		//Check to ensure it is a particular type? Perhaps enum?

                		printToScreen("  Please enter the COMPANY the item came from:  ");
                		String company = getInput();
                		printToScreen("  Please enter the selling PRICE: ");
                		double price = Double.parseDouble(getInput());
                		//Check to ensure that it is parsed correctly
                		printToScreen("  Please enter the YEAR the item was created: ");
                		int year = Integer.parseInt(getInput());
                    }
                		
                    
                }
                printToScreen("  Please review the information you have entered.  Would like to make edits? Y/N: ");
                //<print off information>
                boolean done = yesno(getInput());
                /*
                while(!done){
                	<user enters field number>
                	getFieldName + prevEnteredField
                	" please enter the new information: "
                	<user enters new information>
                	"  do you want to make any more changes? Y/N"
                	if(user entered Y){
                		done= true;
                    }
                }
                    */
                setState(MGRSTART);
            }else if(state == ADDSUCCESS){
                
            }else if(state == ADDFAILURE){
                
            }else if(state == PROCDELIVERY){
                printToScreen("  Please enter the ReceiptID of the order you wish to update: ");
            	String receiptID = getInput();
            	boolean isReceiptIDValid;
            	if(true /* (select * from Order where receiptId = _receiptID) returns empty*/){
            		isReceiptIDValid = false;
                }else{
                    isReceiptIDValid = true;
                }
            	while(!isReceiptIDValid){
            	    printToScreen("  Invalid receiptId entered. ");
            	    printToScreen("Please enter the ReceiptID of the order you wish to update: ");
            		receiptID = getInput();
                }
                printToScreen("  Has this order been shipped? Y/N");
                boolean wasUpdated = yesno(getInput());
            	if(wasUpdated){
            		//<execute updateOrderDate>
                }else{
            		printToScreen("  Order not updated");	
                }
            
                printToScreen("  Would you like to update another? Y/N");
                boolean another = yesno(getInput());
            	if(another){
            	   setState(PROCDELIVERY);
                }else{
                	setState(MGRSTART);
                }
            }/*else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                
            }else if(state == ){
                */
            else if(state == EXIT){
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
    
    /**
     * Helper method to assist with the Y/N queries ade by the system. 
     * @param yn = getInput9)
     * @return true if the user inputs 'y' or 'Y', false if user enters 'n' or 'N', 
     * and asks for a valid version if it is not one of the above characters
     * @author Chazz Young
     */
    private boolean yesno(String yn)
    {
        boolean done = false;
        while(!done){
            if(yn.toUpperCase().equals("Y")){
                return true;
            }else if(yn.toUpperCase().equals("N")){
                return false;
            }else{
                printToScreen("  This is not a valid choice. Please enter 'y' or 'n'");
            }
        }
        return done;
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
