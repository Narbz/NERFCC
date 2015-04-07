import java.sql.SQLException;
import java.util.ArrayList;
import java.io.*;
import java.util.Calendar;
import java.sql.Date;
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
    
    //User entity persistent through all states
    private Customer cust;
    
    //hardcoded manager and clerk accounts
    private final Customer clerk1 = new Customer("Clerk", "Clerk Alice", "123", "1233 ams Street", "6036036030");
    private final Customer mgr1 = new Customer("Manager", "Manager Bob", "password", "1233 ams Street", "6046046040");
    
    //Lists for item management
    private ArrayList<Item> VSB, searchedItems;
    private ArrayList<PurchaseItem> orderItems, INSFStock;
    private ArrayList<ReturnItem> retItems;
    
    
    private Order order;
    private Item itemToAdd;
    private Return ret;
    
    

    public StateHandler()
    {
        reader = new InputReader();
        ec = new ErrorChecker();
        cust = null;
        database = new ams();
        
        searchedItems = new ArrayList<Item>();
        VSB = new ArrayList<Item>();
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
        if(username.equalsIgnoreCase(clerk1.getCID()) && password.equals(clerk1.getPassword())){
            cust = clerk1;
            return st.CLERKSTART;
        }else if(username.equalsIgnoreCase(mgr1.getCID()) && password.equals(mgr1.getPassword())){
            cust = mgr1;
            return st.MGRSTART;
        }
        
        int log = -1;
        try{
            log = database.verifyUserLogin(username, password); 
        }catch(Exception e){
            printToScreen(e.getMessage());
        }
        
        if(log == 0){
            printToScreen("  Sorry, but the username and password doesn't match in the database.");
            return st.LOGIN;
        }else if(log == 1){
            printToScreen("  Welcome " + username + "!");
            try{
                cust = database.selectCustsomerByCid(username);
            }catch(Exception e){
                printToScreen(e.getMessage());
                return st.EXIT;
            }
            return st.CUSTSTART;
        }else{
            printToScreen("  The system failed to connect to the database. Now exsiting");
            return st.EXIT;
        }
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
        boolean validNum = ec.checkNumLength(phonenum, 10);
        while(!validNum){
            printToScreen("  The phone number you enter is not a valid phone number.");  
            printToScreen("  A valid number consists of digits 0-9 i.e 1234567890.");  
            printToScreen("  Please re-enter your phone number: (xxxxxxxxxx)");
            phonenum = getInput(10);
            validNum = ec.checkNumLength(phonenum, 10);
        }
        printToScreen("  Please enter your username that you will use to log in: ");
        boolean validUser = false;
        
        String username = "";
        
        
        while(!validUser){ 
            username = getInput(50);
            int exists = -1;
            try{
                exists = database.verifyUserExists(username);
            }catch(Exception e){
                printToScreen(e.getMessage());
            }
            
            if(exists == 1){
                printToScreen("  Sorry, that username is already taken. Please enter another: ");;
            }else if(exists == 0){
                printToScreen("  Excellent, this username is free for the taking!");
                validUser = true;
            }else{
                printToScreen("  The system failed t oconnect to the database");
                return st.EXIT;
            }
        }
        printToScreen("  Please enter your password: ");
        String password = getInput(40);
        Customer newCust = new Customer(username, name, password, address, phonenum);
        try{
            database.insertCustomer(newCust);
        }catch(Exception e){
            printToScreen(e.getMessage());
        }
        
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
        if(choice.equalsIgnoreCase("r")){
            return st.PROCESSRETURN;  
        }else if(choice.equalsIgnoreCase("q")){
            return st.INITIAL;
        }else{//do nothing
            printToScreen("  This is not a valid operation.");
            return st.CLERKSTART;
        }
    }
    
    /**
     * @author Chazz, Curtis
     */
    public State PROCESSRETURN()
    {
        printToScreen("  You have chosen to process a return.");
        printToScreen("  Please enter the receipt ID of the item(s) the customer wishes to return:");
        String rid = getInput();
        int canReturn = -1;
        
        try{
            int receiptId = Integer.parseInt(rid);
            canReturn = database.selectReceiptToVerifyDate(rid);
            orderItems = (ArrayList<PurchaseItem>) database.selectPurchases(receiptId);
        }catch(Exception e){
            printToScreen(e.getMessage());
        }

        if(canReturn == 1){
            printToScreen("  The items for this receipt number can be returned.");
            printToScreen("  Here are the items on the receipt:");
            printReturnItems(retItems);
            return st.RETURNITEMS;
        }else if(canReturn == 0){
            printToScreen("  The items in this receipt cannot be returned because");
            printToScreen("  it either doesn't exist or it is over 15 days old");
            return st.CLERKSTART;
        }else{
            printToScreen("  The program failed to connect to the database.");
            return st.EXIT;
        }
    }
    
    /**
     * @author Chazz, Curtis
     */
    public State RETURNITEMS()
    {
        boolean allItems = false;
        while(!allItems){//Add more items to return
            printPurchaseItems(orderItems);
            printToScreen("  Please select the upc of the item that you"); 
            printToScreen("  wish to return, or 'd' if you are finished");
            String in = getInput(12); //instead of UPC
            if(in.equalsIgnoreCase("d")){
                allItems = true;
            }else{
                while(ec.getUPC(in) == null){
                    printToScreen("  Please enter a valid UPC.");
                    in = ec.getUPC(getInput(12));
                }
                PurchaseItem toReturn = null;
                while(toReturn == null){
                    /*toReturn = searchUPC(orderItems, in);*/
                    for(int i = 0; i < orderItems.size(); i++){
                        if(in.equals(orderItems.get(i).getUPC())){
                            toReturn = orderItems.get(i);
                        }
                    }
                }
            
            
                printToScreen("  Please enter the quantity of items that you wish ");
                printToScreen("  to return, or 'a' for the entire quantity");
                
                int qty = -1;
                while(qty == -1){ //invalid quantity
                    in = getInput(12);
                    if(in.equalsIgnoreCase("a")){
                        qty = toReturn.getQuantity();
                    }else{
                        qty = ec.getNum(in);
                        if(qty == -1){//invalid number
                            printToScreen("  Please enter a valid number");
                        }else if(qty > toReturn.getQuantity()){
                            printToScreen("  Please enter a quantity between 1 and " + toReturn.getQuantity());
                            qty = -1;
                        }else{
                            //valid, break loop
                        }
                    }
                }
                
                int reid;
                try {
                    reid = database.selectLatestReturnRetId();
                    ReturnItem i = new ReturnItem(reid+1, qty, toReturn.getUPC());
                    retItems.add(i);
                    orderItems.remove(toReturn);
                    toReturn.setQuantity(toReturn.getQuantity() - i.getQuantity());
                    if(toReturn.getQuantity() > 0){//Add it back as there are still more 
                        orderItems.add(toReturn);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                
                printToScreen("  Do you wish to include more items in this return? Y/N");
                boolean moreItems = yesno(getInput(1));
                if(!moreItems){
                    allItems = true;
                }else{ //continue
                }
            
                int receiptId = toReturn.getReceiptID();
            
                java.util.Date currentday = new java.util.Date();
                Date date = new Date(currentday.getTime());
                try {
                    Return ret = new Return(retItems.get(0).getRetid() ,receiptId, date);
                    database.insertReturn(ret);
                    for(int j = 0; j < retItems.size(); j++){
                        database.insertReturnItem(retItems.get(j));
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
                
            }
            if(orderItems.isEmpty()){
                allItems = true;
            }
        }
        //After all items are completed
        return st.RETURNCONFIRM;
    }
    
    /**
     * @author Curtis, Chazz
     */
    public State RETURNCONFIRM()
    {
        printToScreen("  Attempting to process the return for the given items!");
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
        if(choice.equalsIgnoreCase("a")){
            return st.ADDITEMS;
        }else if(choice.equalsIgnoreCase("p")){
            return st.PROCDELIVERY;
        }else if(choice.equalsIgnoreCase("d")){
            return st.DSRINIT;
        }else if(choice.equalsIgnoreCase("n")){
            return st.NTSRINIT;
        }else if(choice.equalsIgnoreCase("q")){
            return st.INITIAL;
        }else if(choice.equalsIgnoreCase("x")){
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
        itemToAdd = null;
        try{
            itemToAdd = database.selectItemByUpc(upc);
        }catch(Exception e){
            printToScreen(e.getMessage());
        }
        //check to ensure that the upc exists
        
        if(itemToAdd != null){
            boolean validNum = false;
            int qty = -1;
            while(!validNum){
                printToScreen("  Please enter the quantity that you would like to add: ");
                qty = ec.getNum(getInput(9));//no more than 1 billion of one item
                if(qty == -1){
                    printToScreen("  Please enter a positive integer");
                }else{
                    validNum = true;
                }
            }
            itemToAdd.setStock(itemToAdd.getStock() + qty);
            
            //Update the rpice
            printToScreen("  Would you like to update teh rpice as well? Y/N");
            boolean updatePrice = yesno(getInput(1));
            float price = 0;
            if(updatePrice){
                boolean validPrice = false;
                while(!validPrice){
                    price = ec.getPrice(getInput(12));
                }
            }
            boolean successful = false;
            try{
                database.updateItemStock(itemToAdd.getUpc(), Integer.toString(itemToAdd.getStock()));
                if(updatePrice){
                    //
                }
                successful = true;
            }catch(Exception e){
                printToScreen(e.getMessage());
            }
            
            if(successful){
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
            if(addToInv){//Adding a new item to the inventory
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
                
                //Check to see fi teh item has an associated lead singer
                printToScreen("  Does this item have an associated lead singer? Y/N");
                boolean hasLeadSinger = yesno(getInput(1));
                String sname = "";
                if(hasLeadSinger){
                    printToScreen("  Please enter name of the lead singers.");
                    sname = getInput(40);
                }
                
                printToScreen("  Does this item have an associated list of songs? Y/N");
                boolean hasSongs = yesno(getInput(1));
                ArrayList<String> songs = new ArrayList<String>();
                if(hasSongs){
                    boolean allSongs = false;
                    while(!allSongs){
                        printToScreen("  Please enter the title of the next song");
                        String nextSong = getInput(50);
                        if(!songs.contains(nextSong)){
                            printToScreen("  This song has already been added to the song list");
                        }else{
                            songs.add(nextSong);
                        }
                        printToScreen("  Are there any more songs that you would like to add? Y/N");
                        allSongs = !(yesno(getInput(1))); //If there are more songs, then yes, keep adding
                    }
                }
                
                printToScreen("  Now attemting to add the new item into the database.");
                //String upc, String itemTitle, String type, String category,
                //String company, int year, float price, int stock
                Item newItem = new Item(upc, itemTitle, type, category, company, year, price, qty);
                boolean success = false;
                try{
                    database.insertItem(newItem); //First the item
                    if(hasLeadSinger){//next, the singer
                        LeadSinger singer = new LeadSinger(upc, sname);
                        database.insertLeadSinger(singer);
                    }
                    
                    if(hasSongs){//finally, the lsit of songs
                        for(String s : songs){
                            HasSong newSong = new HasSong(upc, s);
                            database.insertHasSong(newSong);
                        }
                    }
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
            java.util.Date currentday = new java.util.Date();
            Date date = new Date(currentday.getTime());
            try{
                database.updateOrderDate(date.toString(), receiptID);
            }catch(Exception e){
                printToScreen(e.getMessage());
            }
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

        if(in.equalsIgnoreCase("s")){
            return st.SEARCHSTATE;
        }else if(in.equalsIgnoreCase("v")){
            return st.VIEWVSB;
        }else if(in.equalsIgnoreCase("q")){
            return st.INITIAL;
        }else if(in.equalsIgnoreCase("x")){
            return st.EXIT;
        }else{//Do nothing
            return st.CUSTSTART;
        }
    }
    
    /**
     * @author Curtis INCOMPLETE
     */
    public State SEARCHSTATE()
    {
        printToScreen("  Would you like to search by category? Y/N");
        String category = "";
        boolean sbc = yesno(getInput(1));
        if(sbc){
            printToScreen("  Please enter one of the following categories: 'pop', 'rock', ");
            printToScreen("  'rap', 'country', 'classical', 'new age', or 'instrumental'/");
            boolean validCategory = false;
            while(!validCategory){
                category = getInput(12);
                validCategory = ec.isValidCategory(category);
                if(!validCategory){
                    printToScreen("  This is not a vali dcategory.");
                }
            }
        }
        
        //Search item by title
        printToScreen("Would you like to search by item title? Y/N");
        boolean sbt = yesno(getInput(1));
        String title = "";
        if(sbt){
            printToScreen("  Please enter the title of the item that you would like to search for.");
            title = getInput(50);
        }

        
        if(category.equals("") && title.equals("")){
            return st.SEARCHFAILED;
        }
        
        //LIST ITEMS - IMCOMPLETE!!!
        /*
        searchedItems.clear():
        try{
            
        }catch(Exception e){
        }
        searchedItems = returned
        printItems(searchedItems); 
        */
        
        //IF NO ITEMS TRANSITION TO SEARCHFAILED
        /*if(itemlist.length <= 0){
            return st.SEARCHFAILED;
        }*/
        
        return st.SELECTITEM;
    }
    
    /**
     * @author Curtis, Farhoud  //MERGED CHECKQTY AND INSFSTOCK STATES
     */
    public State SELECTITEM()
    {
        printToScreen("  Please enter the UPC of the item that you wish to add or 's' if you want to search again.");
        String upc = getInput(12);
        if(upc.equalsIgnoreCase("s")){
            return st.SEARCHSTATE;
        }else{
            Item toSelect = searchByUPC(searchedItems, upc);
            if(toSelect != null){
                printToScreen("  Please enter the quantity that you wish to purchase");
                boolean validQty = false;
                int qty = -1;
                while(!validQty){
                    qty = ec.getNum(getInput(20));
                    if(qty == -1){
                        printToScreen("  Please enter a valid quantity");
                    }else{
                        validQty = true;
                    }
                }
                if(toSelect.getStock() < qty){
                    printToScreen("  Sorry there is not enough of that item in stock. ");
                    printToScreen("  Would you like to accept "+ toSelect.getStock() + " of this item instead? Y/N");
                    boolean accept = yesno(getInput(1));
                    if(accept){
                        qty = toSelect.getStock();
                    }
                    else{
                        return st.SEARCHSTATE;
                    }
                }
                //If there is sufficient stock or the user accepts the available stock
                printToScreen("  Would you like to add " + qty + " of the item with title: ");
                printToScreen(toSelect.getItemTitle() + "to the basket? Y/N");
                boolean addToBasket = yesno(getInput(1));
                if(addToBasket){
                    toSelect.setStock(qty);
                    VSB.add(toSelect);
                    printToScreen("  The item was added to your basket successfully");
                }else{
                    printToScreen("  The item was not added to your basket.");
                }
            }else{
                printToScreen("  No items were found.");
            }
            printToScreen("  Would you like to search for more items? Y/N");
            boolean moreItems  = yesno(getInput(1));
            searchedItems.clear();
            if(!moreItems){
                return st.CUSTSTART;
            }
        }
        
       
        //Transition
        return st.SEARCHSTATE;
    }
    
    /**
     * @author Curtis
     */
    public State VIEWVSB()
    {
        if(!VSB.isEmpty()){
            printToScreen("  Here are the items that are currently in your shopping basket:");
            printItems(VSB);
            printToScreen("  If you would like to clear the basket, please enter 'c'.");
            printToScreen("  If you would like to place an order for these items, please enter 'o'");
            printToScreen("  If you would like to continue searching for items, please enter 's'.");
            String choice = getInput(1);
            if(choice.equalsIgnoreCase("c")){
                return st.CLEARVSB;
            }else if(choice.equalsIgnoreCase("o")){
                return st.PLACEORDER;
            }else if(choice.equalsIgnoreCase("s")){
                return st.SEARCHSTATE;
            }else{
                printToScreen("  This is not a valid coice.");
                return st.VIEWVSB;
            }
        }else{
            printToScreen("  Your basket is squeaky clean and shiny, but empty!");
            printToScreen("  Would you like to search for items to fill it up? Y/N");
            boolean fill = yesno(getInput(1));
            if(fill){
                return st.SEARCHSTATE;
            }else{
                return st.CUSTSTART;
            }
        }
    }
    
    /**
     * @author Farhoud
     */
    public State CLEARVSB()
    {
        VSB.clear();
        printToScreen("  The basket is now squeaky clean and spotless!");
        return st.VIEWVSB;
    }
    
    /**
     * @author Curtiis 
     */
    public State PLACEHOLDER()
    {
        if(VSB.isEmpty()){
            ORDERFAIL(0);
        }
        float total = 0;
        for(Item i : VSB){
            total += i.getStock() * i.getPrice();
        }
        printToScreen("  The total price fo ryour order is: " + total);
        printToScreen("  Would you like to place this order? Y/N");
        boolean place = yesno(getInput(1));
        if(place){
            return st.CHECKQTYFINAL;
        }else{
            return st.CUSTSTART;
        }
    
    }
    
    /**
     * @author Curtis REVISIONS //INCOMPLETE!!!
     */
    public State CHECKQTYFINAL()
    {
        ArrayList<Item> toRemove = new ArrayList<Item>(); 
        //This is to prevent a concurrent modificatio nexception
        for(Item i : VSB){
            Item compare = null;
            try{
                compare = database.selectItemByUpc(i.getUpc());
                
            }catch(Exception e){
                
            }
            
            if(compare != null){
                if(i.getStock() > compare.getStock()){
                    printToScreen("  There is no longer enough stock to complete your order for this item.");
                    printToScreen("  Please enter 'y' if you will accept " + compare.getStock() + " of the item");
                    printToScreen("  Please enter 'n' if you would like to remove the item from the order.");
                    boolean choice = yesno(getInput(1));
                    if(choice){
                        i.setStock(compare.getStock());
                    }else{
                        toRemove.add(i);
                    }
                }
                
            }
        }
        
        //Remove the bad items from the order
        if(!toRemove.isEmpty()){
            for(Item i : toRemove){
                VSB.remove(i);
            }
        }
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
            validCard = ec.checkNumLength(cardnum, 16); //THIS IS A HACK!!!
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
            return ORDERFAIL(1);
        }
    }
    
    /**
     * @author 
     */
    public State RECEIPT()
    {
        //Generate order receipt number
        //Generate receiptID and expected date
        //Attempt to add to the database, set boolean successful
        boolean successful = true;
        if(successful){
            printToScreen("  Thank you for your order! Your order number is: " /*+ <receiptID>*/);
            printToScreen("  Your order will arrive in approximately " + /*<shipTime + */ " days");
            VSB.clear();
            orderItems.clear();
            searchedItems.clear();
        }else{
            return ORDERFAIL(2);
        }
        return null; //dummy return, unreachable
    }
    
    /**
     * @author Chazz
     */
    public State ORDERFAIL(int failtype)
    {
        if(failtype == 0){
            printToScreen("  There are no items in your basket. You must have at least 1");
            printToScreen("  item in you basket to place an order");
            return st.CUSTSTART;
        }else if(failtype == 1){
            printToScreen("  The order has been cancelled successfully!");
        }else if(failtype == 2){
            printToScreen("  The database failed to receive your order.");
        }
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
    
    /**
     * @author Chazz Young
     */
    public  void printItems(ArrayList<Item> items)
    {
        printToScreen(" UPC, title, Type, Category, Company, Year, Price, Stock");
        
        for(Item i : items){
            String s = (i.getUpc() + ", " + i.getItemTitle() + ", " + i.getType() + ", " + i.getCategory()); 
            s = s + ", " + i.getCompany() + ", " + i.getYear() + ", " + i.getPrice() + ", " + i.getStock();
            printToScreen(s);
        }
    }
    
    /**
     * @author Chazz Young
     */
    public void printPurchaseItems(ArrayList<PurchaseItem> items)
    {
        printToScreen("  UPC, qty");
        for(PurchaseItem i : items){
            String s = "  " + i.getUPC() + ", " + i.getQuantity();
            printToScreen(s);
        }
    }
    
    /**
     * @author Chass Young
     */
    public void printReturnItems(ArrayList<ReturnItem> items)
    {
        printToScreen("  UPC, qty");
        for(ReturnItem i : items){
            String s = "  " + i.getUpc() + ", " + i.getQuantity();
            printToScreen(s);
        }
    }
    
    /**
     * @return the item that matches the UPC specified or null
     * @author Chazz Young
     */
    private Item searchByUPC(ArrayList<Item> items, String upc)
    {
        for(Item i : items){
            if(i.getUpc() == upc){
                return i;
            }
        }
        return null;
    }
    
    /**
     * @return the PurchaseItem that matches the UPC specified or null
     * @author Chazz Young
     */
    private PurchaseItem searchUPC(ArrayList<PurchaseItem> items, String upc)
    {
        for(PurchaseItem i : items){
            if(i.getUPC() == upc){
                return i;
            }
        }
        return null;
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