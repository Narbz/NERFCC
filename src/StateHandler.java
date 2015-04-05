import java.util.ArrayList;
import java.io.*;
import java.util.Calendar;
import java.util.Scanner;
/**
 * The StateHandler class is used to handle all methods that occur in a given state.
 * 
 * @author Chazz Young, credits to other writers given in methods
 * @version (a version number or a date)
 */
public class StateHandler
{
    //System objects
    private State st;
    private InputReader reader;
    private ErrorChecker ec;
    private ams database;
    
    //User entity persistent through al lstates
    private Customer cust;
    
    //hardcoded manager and clerk accounts
    private final Customer clerk1 = new Customer("Clerk1", "clrk", "1234", null, null);
    private final Customer mgr1 = new Customer("Manager1", "mgr1", "1337", null, null);
    
    //Lists for item management
    private ArrayList<Item> VSB, searchedItems;
    private ArrayList<String> searchItemStrings;
    private ArrayList<PurchaseItem> orderItems, INSFStock;
    private ArrayList<ReturnItem> retItems;
    
    
    private Order order;
    private Item itemToAdd;
    
    

    public StateHandler()
    {
        reader = new InputReader();
        ec = new ErrorChecker();
        cust = null;
        database = new ams();
        
        VSB = new ArrayList<Item>(); searchedItems = new ArrayList<Item>();
        searchItemStrings = new ArrayList<String>();
        resetSearchItemStrings();
        orderItems = new ArrayList<PurchaseItem>();INSFStock = new ArrayList<PurchaseItem>(); 
        retItems = new ArrayList<ReturnItem>();
        order = null;
        itemToAdd = null;
        
        
        
    }

    //STATE METHODS
    /**
     * @author Chazz
     */
    public State INITIAL()
    {
        printToScreen("  Welcome to the AMS Directory!");
        printToScreen("  Have you visited us before? Y/N");
        boolean hasVisited = yesno(getInput(1));
        if(hasVisited){
            return st.LOGIN;
        }else{
            return st.REGISTER;
        }
    }
    
    /**
     * @author 
     */
    public State LOGIN()
    {
        printToScreen("  Please enter your username.");
        String username = getInput(50);
        printToScreen("  Please enter your password.");
        String password = getInput(40);
        if(username.equals(clerk1.getCID()) && password.equals(clerk1.getPassword())){
            cust = clerk1;
            return st.CLERKSTART;
        }else if(username.equals(mgr1.getCID()) && password.equals(mgr1.getPassword())){
            cust = mgr1;
            return st.MGRSTART;
        }
        //Method to retrieve username from SQL database 
        //Assume an id and type is returned
        if(true/*c == null*/){
           //Print invalid user login
           return st.LOGIN;
        }else{
            //Print successful login message
            printToScreen("  Welcome " + username + "!");
            if(true/* c.type == MANAGER*/){
                return st.MGRSTART;
            }else if(true/*c.type == CLERK*/){
                return st.CLERKSTART;
            }else if(true/*c.type == CUSTOMER*/){
                return st.CUSTSTART;
            }else{
                //print error message, invalid user type
            }
        }
        return null; //dummy return, it will never get here
    }
    
    /**
     * @author Narbeh
     */
    public State REGISTER()
    {
        printToScreen("  You’re only a few steps away from creating your AMS account!");  
        printToScreen("  Firstly, please enter your first and last name:");
        String name = getInput(40);
        printToScreen("  Please enter your address: ");
        String address = getInput(40);
        printToScreen("  Please enter your phone number(xxxxxxxxxx): ");
        String phonenum = getInput(10);
        boolean invalidNum = ec.checkPhoneNum(phonenum);
        while(invalidNum/*improperPhoneNumber*/){
            printToScreen("  The phone number you enter is not a valid phone number.");  
            printToScreen("  A valid number consists of digits 0-9 i.e 1234567890.");  
            printToScreen("  Please re-enter your phone number: (xxxxxxxxxx)");
            phonenum = getInput(12);
            invalidNum = ec.checkPhoneNum(phonenum);
        }
        printToScreen("  Please enter your username that you will use to log in: ");
        String username = getInput(50);
        boolean invalidUser = false;
        //TODO sql query to check if user name already exists
        while(invalidUser /*userNameExists*/){ 
            printToScreen("  Sorry, that username is already taken. Please enter another: ");
            username = getInput(50);
        }
        printToScreen("  Please enter your password: ");
        String password = getInput(40);
        Customer newCust = new Customer(username, name, password, address, phonenum);
        try{
            database.insertCustomer(newCust);
        }catch(Exception e){
            printToScreen(e.getMessage());
        }
        /*
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
        //Add customer to db
        cust = newCust;
        return st.CUSTSTART;
         
    }
    
    /**
     * @author Chazz
     */
    public State CLERKSTART()
    {
         printToScreen("  If you wish to process a return, please enter 'r'");
        printToScreen("  If you wish to log out, please enter 'q'");
        String choice = getInput(1);
        if(choice.toLowerCase().equals("r")){
            return st.RETURNITEMS;  
        }else if(choice.toLowerCase().equals("q")){
            return st.INITIAL;
        }else{//do nothing
            printToScreen("  This is not a valid operation.");
            return st.CLERKSTART;
        }
    }
    
    /**
     * @author Chazz
     */
    public State PROCESSRETURN()
    {
        printToScreen("  You have chosen to process a return.");
        printToScreen("  Please enter the receipt ID of the item(s) the customer wishes to return:");
        String rid = getInput();
        //<query database for valid receipt id and whether or not items can be returned>
        boolean canReturn = true;//<aboveanswer;
        if(canReturn){
            printToScreen("  The items for this receipt number can be returned.");
            printToScreen("  Here are the items on the receipt:");
            //<print list of items>
            return st.RETURNITEMS;
        }else{
            printToScreen("  The items in this receipt cannot be returned because");
            printToScreen("  it either doesn't exist or it is over 15 days old");
            return st.CLERKSTART;
        }
    }
    
    /**
     * @author Chazz
     */
    public State RETURNITEMS()
    {
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
        return st.RETURNCONFIRM;
    }
    
    /**
     * @author Chazz
     */
    public State RETURNCONFIRM()
    {
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
        return st.CLERKSTART;
    }
    
    /**
     * @author Chazz
     */
    public State MGRSTART()
    {
        printToScreen("  Please select one of the following options:");
        printToScreen("  To increase the quantity of an existing item or to add "); 
        printToScreen("  a new item to the database, please enter 'a'.");
        printToScreen("  To process the delivery of an item, please enter 'p'.");
        printToScreen("  To generate a sales report for a given date, please enter 'd'.");
        printToScreen("  To generate a sales report for the top selling items, please enter 'n'.");
        printToScreen("  To log out, please enter 'q'");
        printToScreen("  To exit the program, please enter 'x'");
        String choice = getInput(1);
        if(choice.toLowerCase().equals("a")){
            return st.ADDITEMS;
        }else if(choice.toLowerCase().equals("p")){
            return st.PROCDELIVERY;
        }else if(choice.toLowerCase().equals("d")){
            return st.DSRINIT;
        }else if(choice.toLowerCase().equals("n")){
            return st.NTSRINIT;
        }else if(choice.toLowerCase().equals("q")){
            return st.INITIAL;
        }else if(choice.toLowerCase().equals("x")){
            return st.EXIT;
        } else{//Do nothing
            printToScreen("  This is not a valid option. Please try again.");   
            return st.MGRSTART;
        }
    }
    
    /**
     * @author Narbeh
     */
    public State ADDITEMS()
    {
        printToScreen("  Please enter the UPC of the item you would like to add to stock: ");
        String upc = getInput(12);
        //check to ensure that the upc exists
        boolean upcExists = false;
        if(upcExists/*isExistUPC*/){
            boolean invalidNum = true;
            while(invalidNum){
                printToScreen("  Please enter the quantity: ");
                int qty = ec.getNum(getInput(9));//no more than 1 billion of one item
                if(qty == -1){
                    printToScreen("  Please enter a positive integer");
                }else{
                    invalidNum = false;
                }
            }
            //execute updateItemStock
            if(true/*successful*/){
                printToScreen("  The database was successfully updated!");
                printToScreen("  Would you like to add more items? Y/N");
                boolean moreItems = yesno(getInput(1));
                if(!moreItems){
                    return st.MGRSTART;
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
                printToScreen("  Please enter the TYPE of the item: CD/DVD");
                
                //Valid type
                boolean validType = false;
                String type = "";
                while(!validType){
                    type = getInput(3);
                    validType = ec.isCDDVD(type);
                    if(!validType){
                        printToScreen("  The is not a valid type. Please enter 'CD' or 'DVD'");
                    }
                }
                
                //valid category
                printToScreen("  Please enter the CATEGORY of the item: ");
                String category = "";
                boolean validCategory = false;
                while(!validCategory){
                    category = getInput(20);
                    validCategory = ec.isValidCategory(category);
                    if(!validCategory){
                        printToScreen("  This is not a valid category. Please enter 'rock', 'pop',");
                        printToScreen("  'rap', 'country', 'classical', 'new age' or  'instrumental'");
                    }
                }

                printToScreen("  Please enter the COMPANY the item came from:  ");
                String company = getInput(20);
                
                //check price
                printToScreen("  Please enter the selling PRICE: ");
                float price = -1;
                boolean validPrice = false;
                while(!validPrice){
                    price = ec.getPrice(getInput(20))   ;
                    if(price == -1){
                        printToScreen("  This is not a valid price. Please enter a positive price ");
                    }else{
                        validPrice = true;
                    }
                }
                //Check to ensure that it is parsed correctly
                printToScreen("  Please enter the YEAR the item was created: ");
                boolean validYear = false;
                int year = -1;
                while(!validYear){
                    year = ec.getNum(getInput(4));
                    //Check to make sure the year is not in the future?
                    if(year == -1){
                        printToScreen("  Please enter a valid year.");
                    }else{
                        validYear = true;
                    }
                }
                
                printToScreen("  Please enter the initial quantity of the item.");
                int qty = -1;
                boolean validQty = false;
                while(!validQty){
                    qty = ec.getNum(getInput(9));
                    if(qty == -1){
                        printToScreen("  Please enter a valid quantity");
                    }else{
                        validQty = true;
                    }
                }
                
                printToScreen("  Now attemting to add the new item into the database.");
                //String upc, String itemTitle, String type, String category,
                //String company, int year, float price, int stock
                Item newItem = new Item(upc, itemTitle, type, category, company, year, price, qty);
                boolean success = false;
                try{
                    database.insertItem(newItem);
                    success = true;
                }catch(Exception e){
                    printToScreen(e.getMessage());
                }
                
                if(success){
                    printToScreen("  The item has been successfully aded to the database!");
                }else{
                    printToScreen("  Error: the item was not added to the database");
                }
                printToScreen("  Would you like to add more items?Y/N");
                boolean moreItems = yesno(getInput(1));
                if(!moreItems){
                    return st.MGRSTART;
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
            
                */
            return st.MGRSTART;
        }
        return null;//dummy statement, unreachable
    }
    
    /**
     * @author Narbeh
     */
    public State PROCDELIVERY()
    {
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
           return st.MGRSTART;
        }
        return st.PROCDELIVERY;
    }
    
    /**
     * @author Narbeh
     */
    public State DSRINIT()
    {
        String date = null;
        boolean invalidDate = true;
        
        while(invalidDate){
            printToScreen("  Please enter the DATE in the format yyyy-mm-dd:");
            
            date = getInput(10);
            //invalidDate = (validate date)
            if(invalidDate){
                printToScreen("  This is not a valid date");
            }else{
                //Method to determine whether the date given is in the future
                boolean inFuture = false;
                if(inFuture){
                    printToScreen("  The date that you entered is in the future.");
                }else{
                    invalidDate = false;
                }
            }
        }
        
        //execute the daily ales report function
        
        boolean foundDate = true;
        if(foundDate){
            //Build the report
            printToScreen("  Press enter when you are finished.");
            String dummy = getInput();
            return st.MGRSTART;
        }else{
            printToScreen("  Sorry, the date you specified contains no sales information.");
            printToScreen("  Would you like to enter another date? Y/N:");
            boolean again = yesno(getInput(1));
            if(again){
                return st.DSRINIT;
            }else{
                return st.MGRSTART;
            }
        }
    }
    
    /**
     * @author Narbeh REVISIONS
     */
    public State NTSRINIT()
    {
        String date = null;
        boolean invalidDate = true;
                
        while(invalidDate){
            printToScreen("  Please enter the DATE in the format yyyy-mm-dd:");
            
            date = getInput(10);
            //invalidDate = (validate date)
            if(invalidDate){
                printToScreen("  This is not a valid date");
            }
        }
        String num = null;;
        boolean invalidNumber = true;
        while(invalidNumber){
            printToScreen("  Please enter the number of items you would like to see:");
            num = getInput(); //Limits?
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
            return st.NTSRSUCCESS;
        }else{
            return st.NTSRFAILURE;
        }
    }
    
    /**
     * @author Chazz
     */
    public State NTSRSUCCESS()
    {
        printToScreen("  Please press enter/preturn to continue.");
        String dummy = getInput();
        return st.MGRSTART;
    }
    
    /**
     * @author Chazz
     */
    public State NTSRFAILURE()
    {
        printToScreen("  The system failed to generate the rqueted report");
        return st.MGRSTART;
    }
    
    /**
     * @author Chazz
     */
    public State CUSTSTART()
    {
        printToScreen("  Search - 's', View Basket - 'v', logout - 'q', exit - 'x'");
        String in = getInput(1);
        //HEADER TRANSITIONS

        if(in.toLowerCase().equals("s")){
            return st.SEARCHSTATE;
        }else if(in.toLowerCase().equals("v")){
            return st.VIEWVSB;
        }else if(in.toLowerCase().equals("q")){
            return st.INITIAL;
        }else if(in.toLowerCase().equals("x")){
            return st.EXIT;
        }else{//Do nothing
            return st.CUSTSTART;
        }
    }
    
    /**
     * @author 
     */
    public State SEARCHSTATE()
    {
        printToScreen("  What category is the item you're searching for? CD/DVD/Music Book/Music Sheet");
        String category = getInput(2);
        printToScreen("  What is the title of the item you're search for?");
        String title = getInput(20);
        if(category.toLowerCase().equals("cd")){
            printToScreen("  Who is the lead singer of the item you're search for?");
            String leadsinger = getInput(20);
        }
        if(category.equals("") && title.equals("")){
            return st.SEARCHFAILED;
        }
        
        //LIST ITEMS - IMCOMPLETE!!!
        
        //IF NO ITEMS TRANSITION TO SEARCHFAILED
        /*if(itemlist.length <= 0){
            return st.SEARCHFAILED;
        }*/
        
        return st.SELECTITEM;
    }
    
    /**
     * @author Curtis
     */
    public State SELECTITEM()
    {
        printToScreen("  Please enter the UPC of the item that you wish to add or 's' if you want to search again.");
        String upc = getInput(12);
        if(upc.toLowerCase().equals("s")){
            return st.SEARCHSTATE;
        }
        //Search for item with the upc specified
        printToScreen("  Please enter the quantity that you wish to purchase (between 1 and "/* + itemqty*/);
        //Check that the entered qty is an integer between 1 and itemqty
        //Transition
        return st.CHECKQTY;
    }
    
    /**
     * @author Curtis
     */
    public State CHECKQTY()
    {
        if(true/*itemqty <= item.stock*/){
            return st.ADDTOVSB;
        }
        else{
            return st.INSFSTOCK;
        }
        
    }
    
    /**
     * @author Curtis
     */
    public State INSFSTOCK()
    {
        printToScreen("  Sorry there is not enough of that item in stock. ");
        if(true/*item.stock != 0*/){
            printToScreen("  Would you like to accept "+/*item.stock+*/" of this item instead? Y/N");
            boolean accept = yesno(getInput(1));
            if(accept){
                return st.ADDTOVSB;
            }
            else{
                return st.SEARCHSTATE;
            }
        }
        else{
            printToScreen("  Sorry, this item is no longer in stock.");
            return st.SEARCHSTATE;
        }
        
    }
    
    /**
     * @author Curtis
     */
    public State SEARCHFAILED()
    {
        printToScreen("  No items were found. Would you like to try again? Y/N");
        boolean again = yesno(getInput(1));
        if(again){
            return st.SEARCHSTATE;
        }else{
            return st.CUSTSTART;
        }
    }
    
    /**
     * @author Curtis (merged VBS SUCCESS/FAIL states in as well)
     */
    public State ADDTOVSB()
    {
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
            return st.SEARCHSTATE;
        }else{
            return st.VIEWVSB;
        }
    }
    
    /**
     * @author Curtis
     */
    public State VIEWVSB()
    {
        printToScreen("  Here are the items that are currently in your shopping basket:");
        //Check to make sure the basket is not empty
        //Generate report print for the list and print it
        printToScreen("  If you would like to clear the basket, please enter 'c'.");
        printToScreen("  If you would like to place an order for these items, please enter 'o'");
        printToScreen("  If you would like to continue searching for items, please enter 's'.");
        String choice = getInput(1);
        if(choice.toLowerCase().equals("c")){
            return st.CLEARVSB;
        }else if(choice.toLowerCase().equals("o")){
            return st.PLACEORDER;
        }else if(choice.toLowerCase().equals("s")){
            return st.SEARCHSTATE;
        }else{
            printToScreen("  This is not a valid coice.");
            return st.VIEWVSB;
        }
    }
    
    /**
     * @author Farhoud
     */
    public State CLEARVSB()
    {
        //clear the basket //VSB.clear();
        printToScreen("  The basket is now squeaky clean and spotless!");
        return st.VIEWVSB;
    }
    
    /**
     * @author 
     */
    public State PLACEHOLDER()
    {
        return null;
    }
    
    /**
     * @author Curtis REVISIONS
     */
    public State CHECKQTYFINAL()
    {
        //done later
        return null;
    }
    
    /**
     * @author 
     */
    public State PAYFORORDER()
    {
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
            return st.ORDERFINAL;
        }else{
            printToScreen("  The credit card that you entered could not be validated.");
            return st.PAYFORORDER;
        }
    }
    
    /**
     * @author Curtis
     */
    public State ORDERFINAL()
    {
        printToScreen("  Are you sure you would like to place this order?"); 
        printToScreen("  Once confirmed, the order will be final. Y/N"); 
        boolean fconfirm = yesno(getInput(1));
        if(fconfirm){
            return st.RECEIPT;
        }else{
            return st.ORDERFAIL;
        }
    }
    
    /**
     * @author 
     */
    public State RECEIPT()
    {
        //Generate the order for placement ionto the DB
        //Generate receiptID and expected date
        //Attempt to add to the database, set boolean successful
        boolean successful = true;
        if(successful){
            printToScreen("  Thank you for your order! Your order number is: " /*+ <receiptID>*/);
            printToScreen("  Your order will arrive in approximately " + /*<shipTime + */ " days");
        }else{
            return st.ORDERFAIL;
        }
        return null; //dummy return, unreachable
    }
    
    /**
     * @author Chazz
     */
    public State ORDERFAIL(int failtype)
    {
        //doing later
        return null;
    }
    
    /**
     * @author 
     */
    public State EXIT()
    {
        printToScreen("  Thank you for visiting the AMS Directory!");
        printToScreen("  Now closing...");
        return st.EXIT;
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
    
    public void resetSearchItemStrings()
    {
        searchItemStrings.clear();
        String s = "UPC          Totle:              Type  Category   Company             ";
        s = s + "Year  Price   Stock   ";
        searchItemStrings.add(s);
    }
    
    public static void printSearch()
    {
        System.out.println(" UPC< title, Type, Category, Company, Year, Price, Stock");
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("123456789123", "Michael Jackson: The Essentials", "CD", "Pop", "Epic", 2005, (float)8.99, 20));
        items.add(new Item("345678912345", "Duran Duran Classic Albums: Rio", "DVD", "Pop", "Eagle Rock Ent", 2008, (float)9.99, 23));
        items.add(new Item("456789123456", "Billy Idol: Rebel Yell", "CD", "Rock", "Chrysalis", 1982, (float)8.99, 31));
        items.add(new Item("234567891234", "Aerosmith: Big Ones", "CD", "Rock", "Geffen", 1994, (float)6.99, 30));
        for(Item i : items){
            String s = (i.getUpc() + ", " + i.getItemTitle() + ", " + i.getType() + ", " + i.getCategory()); 
            s = s + ", " + i.getCompany() + ", " + i.getYear() + ", " + i.getPrice() + ", " + i.getStock();
            System.out.println(s);
        }
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