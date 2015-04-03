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
    
    //**MANAGER STATES!!!
    private final int MGRSTART      = 10;
    private final int ADDITEMS      = 11;
    private final int ADDSUCCESS    = 12;
    private final int ADDFAILURE    = 13;
    private final int PROCDELIVERY  = 21;

    private final int DSRINIT      = 31;
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
    private final int VIEWVSB       = 64;
    private final int CLEARVSB      = 65;
    private final int PLACEORDER    = 70;
    private final int CHECKQTYFINAL = 71;
    private final int PAYFORORDER   = 72;
    private final int ORDERFINAL    = 73;
    private final int RECEIPT       = 74;
    private final int ORDERFAIL     = 75;
    
    
    
    
    
    
    //**OTHER STATES
    private final int EXIT          = 666;

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
            if(state == INITIAL){/**@author Chazz*/
                printToScreen(msgout.printWelcome());
                boolean hasVisited = yesno(getInput(1));
                if(hasVisited){
                    setState(LOGIN);
                }else{
                    setState(REGISTER);
                }
            }else if(state == LOGIN){
                printToScreen("  Please enter your username.");
                String username = getInput(50);
                printToScreen("  Please enter your password.");
                String password = getInput(40);
                
                //Method to retrieve username from SQL database 
                //Assume an id and type is returned
                if(true/*c == null*/){
                   //Print invalid user login
                   setState(LOGIN);
                }else{
                    //Print successful login message
                    printToScreen("  Welcome " + username + "!");
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
                String name = getInput(40);
                printToScreen("  Please enter your address: ");
                String address = getInput(40);
                printToScreen("  Please enter your phone number(xxx-xxx-xxxx): ");
                String phonenum = getInput(12);
                //TODO make a validate phone number check
                boolean b = false;
                while(b = true/*improperPhoneNumber*/){
                    printToScreen("  The phone number you enter is not a valid phone number.");  
                    printToScreen("  A valid number consists of digits 0-9 i.e 123-456-7891.");  
                    printToScreen("  Please re-enter your phone number: (xxx-xxx-xxxx)");
                    phonenum = getInput(12);
                }
                printToScreen("  Please enter your username that you will use to log in: ");
                String username = getInput(50);
                //TODO sql query to check if user name already exists
                while(b = true /*userNameExists*/){ 
                    printToScreen("  Sorry, that username is already taken. Please enter another: ");
                    username = getInput(50);
                }
                printToScreen("  Please enter your password: ");
                String password = getInput(40);/*
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
                 
            }else if(state == CLERKSTART){
                printToScreen("  If you wish to process a return, please enter 'r'");
                printToScreen("  If you wish to log out, please enter 'q'");
                String choice = getInput(1);
                if(choice.toLowerCase().equals("r")){
                setState(RETURNITEMS);  
                }else if(choice.toLowerCase().equals("q")){
                    setState(INITIAL);
                }else{//do nothing
                    printToScreen("  This is not a valid operation.");
                }
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
            }else if(state == RETURNITEMS){/**@author Chazz*/
                boolean allItems = false;
                while(!allItems){//Add more items to return
                    //<print items in the order>
                    printToScreen("  Please select the upc of the item that you"); 
                    printToScreen("  wish to return, or 'd' if you are finished");
                    String in = getInput(12); //instead of UPC
                    boolean done = (in.toLowerCase().equals("d"));
                    //Search the orders list by upc for a given item
                    if(!done){//If input is NOT equal to d
                        //Validate it is a valdi upc until a valid one is selected
                    }
                    if(!done /*number is in the list*/){
                        printToScreen("  Please enter the quantity of items that you wish ");
                        printToScreen("  to return, or ‘a’ for the entire quantity");
                        String qty = getInput();
                        //convert qty to int, success etc
                        
                        //<add to the list of returned items>
                        //<remove items from the order list>
                        printToScreen("  Do you wish to include more items in this return? Y/N");
                        boolean yn = yesno(getInput(1));
                        if(!yn){
                            allItems = true;
                        }else{ //continue
                        }
                    }else{//Done was selected
                        allItems = true;
                    }
                    
                        
                }
                //After all items are completed
                setState(RETURNCONFIRM);
            }else if(state == RETURNCONFIRM){/**@author Chazz */
                printToScreen("  Attempting to process the return for the given items…");
                //<add items to return, returnItem table, increase quantity>
                boolean success = true;//above
                if(success){
                    printToScreen("  The return order has been processed successfully.");
                    //<calculate amount to return>
                    printToScreen("  <amount> has been refunded to the Customer");
                }else{
                    printToScreen("  The database has failed to process the return order."); 
                    //Print the amo
                }
                setState(CLERKSTART);
            }else if(state == MGRSTART){/**@author Chazz*/
                printToScreen("  Please select one of the following options:");
                printToScreen("  To increase the quantity of an existing item or to add "); 
                printToScreen("  a new item to the database, please enter 'a'.");
                printToScreen("  To process the delivery of an item, please enter 'p'.");
                printToScreen("  To generate a sales report for a given date, please enter 'd'.");
                printToScreen("  To generate a sales report for the top selling items, please enter 'n'.");
                printToScreen("  tO log out, please enter 'q'");
                String choice = getInput(1);
                if(choice.toLowerCase().equals("a")){
                    setState(ADDITEMS);
                }else if(choice.toLowerCase().equals("p")){
                    setState(PROCDELIVERY);
                }else if(choice.toLowerCase().equals("d")){
                    setState(DSRINIT);
                }else if(choice.toLowerCase().equals("n")){
                    setState(NTSRINIT);
                }else if(choice.toLowerCase().equals("q")){
                    setState(INITIAL);
                }else{//Do nothing
                    printToScreen("  This is not a valid option. Please try again.");   
                }
            }else if(state == ADDITEMS){/**@author Narbeh*/
                printToScreen("  Please enter the UPC of the item you would like to add to stock: ");
                String upc = getInput(12);
                printToScreen("  Please enter the quantity: ");
                
                if(true/*isExistUPC*/){
                    int qty = Integer.parseInt(getInput());
                    //Check to ensure that this parses correctly
                    //execute updateItemStock
                    if(true/*successful*/){
                        printToScreen("  The database was successfully updated!");
                        printToScreen("  Would you like to add more items? Y/N");
                        boolean moreItems = yesno(getInput(1));
                        if(!moreItems){
                            setState(MGRSTART);
                        }
                    }else{
                        printToScreen("  The database failed to update correctly");
                    }
                    
                }else{//this means the item is a new one
                    printToScreen("  Could not update.  This is a new item not currently in the inventory."); 
                    printToScreen("  Would you like to add it to the inventory? Y/N");
                    boolean addToInv = yesno(getInput(1));
                    if(addToInv){//Adding a ne witem to the inventory
                        printToScreen("  Please enter the TITLE of the item: ");
                        String itemTitle = getInput(40);
                        printToScreen("  Please enter the TYPE of the item: ");
                        String type = getInput(3);
                        printToScreen("  Please enter the CATEGORY of the item: ");
                        String category = getInput(10);
                        //Check to ensure it is a particular type? Perhaps enum?

                        printToScreen("  Please enter the COMPANY the item came from:  ");
                        String company = getInput(20);
                        printToScreen("  Please enter the selling PRICE: ");
                        double price = Double.parseDouble(getInput());
                        //Check to ensure that it is parsed correctly
                        printToScreen("  Please enter the YEAR the item was created: ");
                        int year = Integer.parseInt(getInput(4));
                        printToScreen("  Now attemting to add the new item into the database.");
                        //insert new item into db
                        boolean success = true;
                        if(success){
                            printToScreen("  The item has been successfully aded to the database!");
                        }else{
                            printToScreen("  Error: the item was not added to the database");
                        }
                        printToScreen("  Would you like to add more items?Y/N");
                        boolean moreItems = yesno(getInput(1));
                        if(!moreItems){
                            setState(MGRSTART);
                        }
                        
                    
                    }//else do nothing and cycle back     
                    /*printToScreen("  Please review the information you have entered.  Would like to make edits? Y/N: ");
                    //<print off information>
                    boolean done = yesno(getInput());
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
                    setState(MGRSTART);
                        */
                }
                
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
                    printToScreen("  Please enter the ReceiptID of the order you wish to update: ");
                    receiptID = getInput();
                }
                printToScreen("  Has this order been shipped? Y/N");
                boolean wasUpdated = yesno(getInput(1));
                if(wasUpdated){
                    //<execute updateOrderDate>
                }else{
                    printToScreen("  Order not updated");   
                }
            
                printToScreen("  Would you like to update another? Y/N");
                boolean another = yesno(getInput(1));
                if(!another){
                   setState(MGRSTART);
                }
            }else if(state == DSRINIT){
                
            }else if(state == DSRSUCCESS){
                
            }else if(state == DSRFAILURE){
                
            }else if(state == NTSRINIT){/**@author narbeh HEAVY REVISION*/
                boolean invalidDate = true;
                
                while(invalidDate){
                    printToScreen("  Please enter the DATE in the format yyyy-mm-dd:");
                    
                    String date = getInput(10);
                    //invalidDate = (validate date)
                    if(invalidDate){
                        printToScreen("  This is not a valid date");
                    }
                }
                boolean invalidNumber = true;
                while(invalidNumber){
                    printToScreen("  Please enter the number of items you would like to see:");
                    String num = getInput(); //Limits?
                    //convert num to int
                    //Check to ensure number is an int, set invalidNumber
                    if(invalidNumber){
                        printToScreen("  This is not a valid number");
                        //loop back
                    }
                    
                }
            
                //<execute selectTopNItems>, set success here
                boolean success = true;
                if(success){
                    //-Format the result of the query records separated by newline characters- 
                    //<display the data>
                    setState(NTSRSUCCESS);
                }else{
                    setState(NTSRFAILURE);
                }

            }else if(state == NTSRSUCCESS){/**@author Chazz*/
                printToScreen("  Please press enter/preturn to continue.");
                String dummy = getInput();
                setState(MGRSTART);
            }else if(state == NTSRFAILURE){/**@author Chazz*/
                printToScreen("  The system failed to generate the rqueted report");
            }else if(state == CUSTSTART){/**@author Chazz*/
                printToScreen("  Search - 's', View Basket - 'v', logout - 'q', exit - 'x'");
                String in = getInput(1);
                //HEADER TRANSITIONS

                if(in.toLowerCase().equals("s")){
                    setState(SEARCHSTATE);
                }else if(in.toLowerCase().equals("v")){
                    setState(VIEWVSB);
                }else if(in.toLowerCase().equals("q")){
                    setState(INITIAL);
                }else if(in.toLowerCase().equals("x")){
                    setState(EXIT);
                }else{//Do nothing
                }
            }else if(state == SEARCHSTATE){/**@author Curtis*/
                //no point in writign this without the search method
            }else if(state == SELECTITEM){/**@author Curtis INCOMPLETE!!!!*/
                printToScreen("  Please enter the UPC of the item that you wish to add");
                String upc = getInput(12);
                //Search for item with the upc specified
                printToScreen("  Please enter the quantity that you wish to purchase (between 1 and "/* + itemqty*/);
                //Check that the entered qty is an integer between 1 and itemqty
                //Transition
            }else if(state == SEARCHFAILED){/**@author Farhoud*/
                printToScreen("  No items were found. Would you like to try again? Y/N");
                boolean again = yesno(getInput(1));
                if(again){
                    setState(SEARCHSTATE);
                }else{
                    setState(CUSTSTART);
                }
            }else if(state == ADDTOVSB){/**@author Curtis (merged VBS SUCCESS/FAIstates in as well)*/
                //printToScreen("  Do you wish to add " +/*qty*/ + " of the item " +/*itemTitle*/ + "? Y/N");
                boolean add = yesno(getInput(1));
                if(add){
                    //add item to VSB
                    printToScreen("  The item(s) were successfully added to your basket");
                }else{
                    printToScreen("  No items were added to your basket.");
                }   
                printToScreen("  Would you like to search for more items? Y/N");
                boolean more = yesno(getInput(1));
                if(more){
                    setState(SEARCHSTATE);
                }else{
                    setState(VIEWVSB);
                }
            }else if(state == VIEWVSB){/**@author Curtis*/
                printToScreen("  Here are the items that are currently in your shopping basket:");
                //Check to make sure the basket is not empty
                //Generate report print for hte list and print it
                printToScreen("  If you would like to clear the basket, please enter 'c'.");
                printToScreen("  If you would like to place an order for these items, pelase enter 'o'");
                printToScreen("  If you would like to continue searching for items, please enter 's'.");
                String choice = getInput(1);
                if(choice.toLowerCase().equals("c")){
                    setState(CLEARVSB);
                }else if(choice.toLowerCase().equals("o")){
                    setState(PLACEORDER);
                }else if(choice.toLowerCase().equals("s")){
                    setState(SEARCHSTATE);
                }else{
                    printToScreen("  This is not a valid coice.");
                    //Loop back
                }
            }else if(state == CLEARVSB){
                //clear the basket //VSB.clear();
                printToScreen("  The basket is now squeaky clean and spotless!");
                setState(VIEWVSB);
            }else if(state == PLACEORDER){/**@author Curtis */
                //check to make sure the VSB is not empty
                //calculate total for the order
                //printToScreen("  The total amount for this order is: " + <total> +".");
                
            }else if(state == CHECKQTYFINAL){
                //done later
            }else if(state == PAYFORORDER){/**@author Curtis REVISIONS */
                boolean validCard = false;
                String cardnum = null;
                while(!validCard){
                    printToScreen("  Please enter your credit card number.");
                    cardnum = getInput(16);
                    //validate credit card is the right length, authenticatio
                    validCard = true;
                    if(!validCard){
                        printToScreen("  This is not a valid credit card number.");
                    }
                }
                boolean validDate = false;
                String cardDate = null;
                while(!validDate){
                    printToScreen("  Please enter your credit card's expiration date");
                    printToScreen("  The format for the date is yyyy-mm-dd.");
                    cardDate = getInput(10);
                    //Validate that the date is valid 
                    validDate = true;
                    if(!validDate){
                        printToScreen("  This is not a valid date.");
                    }
                }
                //Validate credit card process???
                boolean validated = true;
                if(validated){
                    setState(ORDERFINAL);
                }else{
                    printToScreen("  The credit card that you entered could not be validated.");
                    //loop back
                }
            }else if(state == ORDERFINAL){/**@author Curtis*/
                printToScreen("  Are you sure you would like to place this order?"); 
                printToScreen("  Once confirmed, the order will be final. Y/N"); 
                boolean fconfirm = yesno(getInput(1));
                if(fconfirm){
                    setState(RECEIPT);
                }else{
                    setState(ORDERFAIL);
                }
            }else if(state == RECEIPT){
                //Generate the order for placement ionto the DB
                //Generate receiptID and expected date
                //Attempt to add to the database, set boolean successful
                boolean successful = true;
                if(successful){
                    printToScreen("  Thank you for your order! Your order number is: " /*+ <receiptID>*/);
                    printToScreen("  Your order will arrive in approximately " + /*<shipTime + */ " days");
                }else{
                    setState(ORDERFAIL);
                }
            }else if(state == ORDERFAIL){
                //tO BE COMPLETED LATER!!!
            }else if(state == EXIT){
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
    
    /**
     * Pverloaded method to retrieve a String with a defined max length
     * @param max the max length
     * @return a String of length <= n
     * @author Chazz Young
     */
    public String getInput(int maxLength)
    {
        String input = getInput();
        if(input.length() > maxLength){
            return input.substring(0, maxLength);
        }//else
        return input;
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
            }else{//Invalid response
                printToScreen("  This is not a valid choice. Please enter 'y' or 'n'");
                yn = getInput(1);
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
