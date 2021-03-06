import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.*;
import java.math.*;
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
    private ArrayList<SearchItem> VSB, searchedItems;
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
        
        searchedItems = new ArrayList<SearchItem>();
        VSB = new ArrayList<SearchItem>();
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
        printToScreen("  You're only a few steps away from creating your AMS account!");  
        printToScreen("  Firstly, please enter your first and last name:");
        String name = getInput(40);
        printToScreen("  Please enter your address: ");
        String address = getInput(40);
        printToScreen("  Please enter your phone number(xxxxxxxxxx): ");
        String phonenum = getInput(10);
        boolean validNum = Pattern.matches("[0-9]{10}", phonenum);//ec.checkNumLength(phonenum, 10);
        while(!validNum){
            printToScreen("  The phone number you enter is not a valid phone number.");  
            printToScreen("  A valid number consists of digits 0-9 i.e 1234567890.");  
            printToScreen("  Please re-enter your phone number: (xxxxxxxxxx)");
            phonenum = getInput(10);
            validNum = Pattern.matches("[0-9]{10}", phonenum);//ec.checkNumLength(phonenum, 10);
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
    	printHeader();
        printToScreen("  If you wish to process a return, please enter 'r'");
        String choice = getInput(1);
        
        State headerAct = headerActions(choice);
        if(headerAct != null) {
        	return headerAct;
        }
        
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
        State headerAct = headerActions(rid);
        if(headerAct != null) {
        	return headerAct;
        }
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
           // printReturnItems(retItems);
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
            State headerAct = headerActions(in);
            if(headerAct != null) {
            	return headerAct;
            }
            if(in.equalsIgnoreCase("d")){
                allItems = true;
            }else{
                PurchaseItem toReturn = null;
                while(toReturn == null){
                    
                    for(int i = 0; i < orderItems.size(); i++){
                        if(in.equals(orderItems.get(i).getUPC())){
                            toReturn = orderItems.get(i);
                        }
                    }
                    in = ec.getUPC(getInput(12));
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
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                
                printToScreen("  Do you wish to include more items in this return? Y/N");
                in = getInput(1);
                headerAct = headerActions(in);
                if(headerAct != null) {
                	return headerAct;
                }
                boolean moreItems = yesno(in);
                if(!moreItems){
                    allItems = true;
                }else{ //continue
                }
            
                int receiptId = toReturn.getReceiptId();
                
                java.util.Date currentday = new java.util.Date();
                java.sql.Date date = new java.sql.Date(currentday.getTime());
                
                try {
                    Return ret = new Return(retItems.get(0).getRetid() , receiptId , date);
                    database.insertReturn(ret);
                    for(int j = 0; j < retItems.size(); j++){
                        database.insertReturnItem(retItems.get(j));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
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
     * TODO: incomplete
     */
    public State RETURNCONFIRM()
    {
        printToScreen("  Attempting to process the return for the given items!");
        boolean success = true;//above
        if(success){
            printToScreen("  The return order has been processed successfully.");
            float amountReturned = 0;
            for(int i = 0; i < retItems.size(); i++){
            	Item item;
				try {
					item = database.selectItemByUpc(retItems.get(i).getUpc());
					amountReturned += item.getPrice();
					database.updateItemStock(item.getUpc(), item.getStock() + retItems.get(i).getQuantity());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
            printToScreen("  "+amountReturned+" has been refunded to the Customer");
        }else{
            printToScreen("  The database has failed to process the return order."); 

        }
        return st.CLERKSTART;
    }
    
    /**
     * @author Chazz
     */
    public State MGRSTART()
    {
    	printHeader();
    	
        printToScreen("  Please select one of the following options:");
        printToScreen("  To increase the quantity of an existing item or to add "); 
        printToScreen("  a new item to the database, please enter 'a'.");
        printToScreen("  To process the delivery of an item, please enter 'p'.");
        printToScreen("  To generate a sales report for a given date, please enter 'd'.");
        printToScreen("  To generate a sales report for the top selling items, please enter 'n'.");
        String choice = getInput(1);
        
        State headerAct = headerActions(choice);
        if(headerAct != null) {
        	return headerAct;
        }
        
        if(choice.equalsIgnoreCase("a")){
            return st.ADDITEMS;
        }else if(choice.equalsIgnoreCase("p")){
            return st.PROCDELIVERY;
        }else if(choice.equalsIgnoreCase("d")){
            return st.DSRINIT;
        }else if(choice.equalsIgnoreCase("n")){
            return st.NTSRINIT;
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
        State headerAct = headerActions(upc);
        boolean validUPC = Pattern.matches("[0-9]{12}", upc);
        if(headerAct != null) {
        	return headerAct;
        }
        while(!validUPC)
        {
        	printToScreen("  Invalid upc entered.  Please re-enter the upc as 16 digits: ");
        	upc = getInput(12);
        	validUPC = Pattern.matches("[0-9]{12}", upc);
        }
        
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
            printToScreen("  Would you like to update the price as well? Y/N");
            String in = getInput(1);
            headerAct = headerActions(in);
            if(headerAct != null) {
            	return headerAct;
            }
            
            boolean updatePrice = yesno(in);
            String price = "";
            if(updatePrice){
            	printToScreen("  Please enter the new price: ");
            	price = getInput(12);//ec.getPrice(getInput(12));
                boolean validPrice = Pattern.matches("[0-9]+[.][0-9]{2}", price);
                while(!validPrice){
                	printToScreen("  The price you entered is invaild. Please enter a new price in form x.xx");
                    price = getInput(12);//ec.getPrice(getInput(12));
                    validPrice = Pattern.matches("[0-9]+[.][0-9]{2}", price);
                }
            }
            boolean successful = false;
            try{
            	int isSuccessful = 0;
                if(updatePrice){
                	isSuccessful = database.updateItemStockAndPrice(itemToAdd.getUpc(), Integer.toString(itemToAdd.getStock()), Double.parseDouble(price));                
                	}
                else
                {
                	isSuccessful = database.updateItemStock(itemToAdd.getUpc(), itemToAdd.getStock());
                }
                successful = (isSuccessful > 0) ? true:false;
            }catch(Exception e){
                printToScreen(e.getMessage());
            }
            
            if(successful){
                printToScreen("  The database was successfully updated!");
                printToScreen("  Would you like to add more items? Y/N");
                in = getInput(1);
                headerAct = headerActions(in);
                if(headerAct != null) {
                	return headerAct;
                }
                boolean moreItems = yesno(getInput(1));
                if(!moreItems){
                    return st.MGRSTART;
                }
                if(moreItems)
                {
                	return st.ADDITEMS;
                }
            }else{
                printToScreen("  The database failed to update correctly");
            }
            
        }else{//this means the item is a new one
            printToScreen("  Could not update.  This is a new item not currently in the inventory."); 
            printToScreen("  Would you like to add it to the inventory? Y/N");
            String in = getInput(1);
            headerAct = headerActions(in);
            if(headerAct != null) {
            	return headerAct;
            }
            boolean addToInv = yesno(in);
            if(addToInv){//Adding a new item to the inventory
                printToScreen("  Please enter the TITLE of the item: ");
                String itemTitle = getInput(40);
                headerAct = headerActions(itemTitle);
                if(headerAct != null) {
                	return headerAct;
                }
                printToScreen("  Please enter the TYPE of the item: CD/DVD");
                
                //Valid type
                boolean validType = false;
                String type = "";
                while(!validType){
                    type = getInput(3);
                    //validType = type.equalsIgnoreCase("CD")||type.equalsIgnoreCase("DVD");//Pattern.matches("CD", type);
                    headerAct = headerActions(type);
                    if(headerAct != null) {
                    	return headerAct;
                    }
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
                    headerAct = headerActions(category);
                    if(headerAct != null) {
                    	return headerAct;
                    }
                    validCategory = ec.isValidCategory(category);
                    if(!validCategory){
                        printToScreen("  This is not a valid category. Please enter 'rock', 'pop',");
                        printToScreen("  'rap', 'country', 'classical', 'new age' or  'instrumental'");
                    }
                }

                printToScreen("  Please enter the COMPANY the item came from:  ");
                String company = getInput(20);
                headerAct = headerActions(company);
                if(headerAct != null) {
                	return headerAct;
                }
                
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
                in = getInput(1);
                headerAct = headerActions(in);
                if(headerAct != null) {
                	return headerAct;
                }
                boolean hasLeadSinger = yesno(in);
                String sname = "";
                if(hasLeadSinger){
                    printToScreen("  Please enter name of the lead singers.");
                    sname = getInput(40);
                    headerAct = headerActions(sname);
                    if(headerAct != null) {
                    	return headerAct;
                    }
                }
                
                printToScreen("  Does this item have an associated list of songs? Y/N");
                boolean hasSongs = yesno(getInput(1));
                ArrayList<String> songs = new ArrayList<String>();
                if(hasSongs){
                    boolean allSongs = false;
                    while(!allSongs){
                        printToScreen("  Please enter the title of the next song");
                        String nextSong = getInput(50);
                        headerAct = headerActions(nextSong);
                        if(headerAct != null) {
                        	return headerAct;
                        }
                        if(songs.contains(nextSong)){
                            printToScreen("  This song has already been added to the song list");
                        }else{
                        	printToScreen("  The song was added to the song list");
                            songs.add(nextSong);
                        }
                        printToScreen("  Are there any more songs that you would like to add? Y/N");
                        in = getInput(1);
                        headerAct = headerActions(in);
                        if(headerAct != null) {
                        	return headerAct;
                        }
                        allSongs = !(yesno(in)); //If there are more songs, then yes, keep adding
                    }
                }
                
                printToScreen("  Now attempting to add the new item into the database.");
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
                    printToScreen("  The item has been successfully added to the database!");
                }else{
                    printToScreen("  Error: the item was not added to the database");
                }
                printToScreen("  Would you like to add more items? Y/N");
                in = getInput(1);
                headerAct = headerActions(in);
                if(headerAct != null) {
                	return headerAct;
                }
                boolean moreItems = yesno(in);
                if(!moreItems){
                    return st.MGRSTART;
                }
                if(moreItems)
                {
                	return st.ADDITEMS;
                }
            
            }//else do nothing and cycle back     
            return st.MGRSTART;
        }
        return null;//dummy statement, unreachable
    }
    
    /**
     * @author Narbeh
     * @throws SQLException 
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public State PROCDELIVERY() throws NumberFormatException, IOException, SQLException
    {
        printToScreen("  Please enter the ReceiptID of the order you wish to update: ");
        String receiptID = getInput();
        State headerAct = headerActions(receiptID);
        if(headerAct != null) {
        	return headerAct;
        }
        boolean isReceiptIDValid;
        //if(true /* (select * from Order where receiptId = _receiptID) returns empty*/){
            isReceiptIDValid = (database.selectOrderExists(Integer.parseInt(receiptID)) > 0) ? true:false;
        //}else{
        //    isReceiptIDValid = true;
        //}
        while(!isReceiptIDValid){
            printToScreen("  Invalid receiptId entered. ");
            printToScreen("  Please enter the ReceiptID of the order you wish to update: ");
            receiptID = getInput();
            isReceiptIDValid = (database.selectOrderExists(Integer.parseInt(receiptID)) > 0) ? true:false;
            headerAct = headerActions(receiptID);
            if(headerAct != null) {
            	return headerAct;
            }
        }
        printToScreen("  Has this order been shipped? Y/N");
        String in = getInput(1);
        headerAct = headerActions(in);
        if(headerAct != null) {
        	return headerAct;
        }
        boolean wasUpdated = yesno(in);
        if(wasUpdated){
        	String expectedPattern = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
            java.util.Date currentday = new java.util.Date();
            String date = formatter.format(currentday.getTime());
           // Date date  = formatter.parse(currentday);//new Date(currentday.getTime());
            try{
                int didUpdate = database.updateOrderDate(date, receiptID);
                if(didUpdate > 0)
                {
                	printToScreen("  Order updated!");   
                }
                else
                {
                	printToScreen("  Unable to update Order");   
                }
            }catch(Exception e){
                printToScreen(e.getMessage());
            }
        }else{
            printToScreen("  Order not updated");   
        }
    
        printToScreen("  Would you like to update another? Y/N");
        in = getInput(1);
        headerAct = headerActions(in);
        if(headerAct != null) {
        	return headerAct;
        }
        boolean another = yesno(in);
        if(!another){
           return st.MGRSTART;
        }
        return st.PROCDELIVERY;
    }
    
    /**
     * @author Narbeh
     */
    public State DSRINIT() throws SQLException, IOException
    {
        String date = null;
        boolean invalidDate = true;
        
        while(invalidDate){
            printToScreen("  Please enter the DATE in the format yyyy-mm-dd:");
            
            date = getInput(10);
            State headerAct = headerActions(date);
            if(headerAct != null) {
            	return headerAct;
            }
            invalidDate = Pattern.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}", date);
            //invalidDate = (validate date)
            if(!invalidDate){
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

        //execute the daily sales report function
        List<DailySalesReport> dsp = database.selectDailySalesReport(date);
        
        boolean foundDate = (!dsp.isEmpty()) ? true : false;
        if(foundDate){
            //Build the report
            printToScreen("  UPC, Category, Price, Sold, Total");
            printToScreen(" -----------------------------------");
            for(int i = 0; i < dsp.size()-1; i++ )
            {
                printToScreen("  " + dsp.get(i).getUpc() + ", " + dsp.get(i).getCategory() + ", $" + dsp.get(i).getPrice() + ", " + dsp.get(i).getSold() + ", " + dsp.get(i).getTotal() );
            }
            printToScreen(" -----------------------------------");
            printToScreen("  Total Quantity Sold:  " + dsp.get(dsp.size()-1).getSold());
            printToScreen("  Total :  $" + Math.round(dsp.get(dsp.size()-1).getTotal()*100.0)/100.0);
            printToScreen("  Press enter when you are finished.");
            String dummy = getInput();
            return st.MGRSTART;
        }else{
            printToScreen("  Sorry, the date you specified contains no sales information.");
            printToScreen("  Would you like to enter another date? Y/N:");
            String in = getInput(1);
            State headerAct = headerActions(in);
            if(headerAct != null) {
            	return headerAct;
            }
            boolean again = yesno(in);
            if(again){
                return st.DSRINIT;
            }else{
                return st.MGRSTART;
            }
        }
    }
    
    /**
     * @author Narbeh REVISIONS
     * @throws SQLException 
     * @throws IOException 
     * @throws NumberFormatException 
     */
    public State NTSRINIT() throws NumberFormatException, IOException, SQLException
    {
        String date = null;
        boolean invalidDate = false;
                
        while(!invalidDate){
            printToScreen("  Please enter the DATE in the format yyyy-mm-dd:");
            
            date = getInput(10);
            State headerAct = headerActions(date);
            if(headerAct != null) {
            	return headerAct;
            }
            invalidDate = Pattern.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}", date);
            //invalidDate = (validate date)
            if(!invalidDate){
                printToScreen("  This is not a valid date");
            }
        }
        String num = null;;
        boolean invalidNumber = false;
        while(!invalidNumber){
            printToScreen("  Please enter the number of items you would like to see:");
            num = getInput();
            State headerAct = headerActions(num);
            if(headerAct != null) {
            	return headerAct;
            }
            //a valid number is a number with at least one digit beginning with [1-9] followed be 0 to many other digits[0-9]
            invalidNumber = Pattern.matches("[1-9]+[0-9]*", num);
            //Check to ensure number is an int, set invalidNumber
            if(!invalidNumber){
                printToScreen("  This is not a valid number");
                //loop back
            }
            
        }
    
        //<execute selectTopNItems>, set success here
        List<SearchTopNitems> topN = database.selectTopNItems(Integer.parseInt(num), date);
        
        boolean success = true;
        if(success){
            //-Format the result of the query records separated by newline characters- 
            printToScreen("  UPC, Title, Category, Stock, Sold");
            printToScreen(" -----------------------------------");
            for(int i = 0; i < topN.size(); i++)
            {
                printToScreen("  " + topN.get(i).getUpc() + ", " + topN.get(i).getTitle() + ", " + topN.get(i).getCategory() + ", " + topN.get(i).getStock() +  ", " + topN.get(i).getSold() );
            }
            printToScreen(" -----------------------------------");
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
        printToScreen("  Please press enter to continue.");
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
    	printHeader();
        printToScreen("  Search - 's', View Basket - 'v'");
        String in = getInput(1);
        //HEADER TRANSITIONS
        
        State headerAct = headerActions(in);
        if(headerAct != null) {
        	return headerAct;
        }
        
        if(in.equalsIgnoreCase("s")){
            return st.SEARCHSTATE;
        }else if(in.equalsIgnoreCase("v")){
            return st.VIEWVSB;
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
        String category = null;
        String in = getInput(1);
        
        State headerAct = headerActions(in);
        if(headerAct != null) {
        	return headerAct;
        }
        
        boolean sbc = yesno(in);
        
        if(sbc){
            printToScreen("  Please enter one of the following categories: 'pop', 'rock', ");
            printToScreen("  'rap', 'country', 'classical', 'new age', or 'instrumental'/");
            boolean validCategory = false;
            while(!validCategory){
                category = getInput(12);
                headerAct = headerActions(category);
                if(headerAct != null) {
                	return headerAct;
                }
                validCategory = ec.isValidCategory(category);
                if(!validCategory){
                    printToScreen("  This is not a valid category.");
                }
            }
        }
        
        //Search item by title
        printToScreen("Would you like to search by item title? Y/N");
        in = getInput(1);
        
        headerAct = headerActions(in);
        if(headerAct != null) {
        	return headerAct;
        }
        
        boolean sbt = yesno(in);
        String title = null;
        if(sbt){
            printToScreen("  Please enter the title of the item that you would like to search for.");
            title = getInput(50);
            title = '%'+title+'%';
            headerAct = headerActions(title);
            if(headerAct != null) {
            	return headerAct;
            }
            
        }

        printToScreen("  Would you like to search by the leading singer? Y/N");
        in = getInput(40);
        
        headerAct = headerActions(in);
        if(headerAct != null) {
        	return headerAct;
        }
        
        boolean sbl = yesno(in);
        
        String leadSinger = null;
        if(sbl){
            printToScreen("  Please enter the lead singer of the item that you would like to search for");
            leadSinger = getInput(20);
            headerAct = headerActions(leadSinger);
            if(headerAct != null) {
            	return headerAct;
            }
        }
        
        if(category == null && title == null && leadSinger == null){
            printToScreen("  Could not complete search as no search critera given.  Returning to start page...");
        	return st.CUSTSTART;//st.SEARCHFAILED;
        }
        
        try{
            searchedItems.clear();
            List<SearchItem> it = database.selectItemSearch(category, title, leadSinger);
            searchedItems.addAll(it);
        }catch(Exception e){
            printToScreen(e.getMessage());
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
        printItems(searchedItems);
        printToScreen("  Please enter the UPC of the item that you wish to add or 's' if you want to search again.");
        String upc = getInput(12);
        
        State headerAct = headerActions(upc);
        if(headerAct != null) {
        	return headerAct;
        }
        
        if(upc.equalsIgnoreCase("s")){
            return st.SEARCHSTATE;
        }else{
            SearchItem toSelect = null;
            for(SearchItem s : searchedItems){
                if(s.getUpc().equals(upc)){
                    toSelect = s;
                }
            }
            
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
                printToScreen(toSelect.getItemTitle() + " to the basket? Y/N");
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

            if(!moreItems){
                searchedItems.clear();
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
            State headerAct = headerActions(choice);
            if(headerAct != null) {
            	return headerAct;
            }
            if(choice.equalsIgnoreCase("c")){
                return st.CLEARVSB;
            }else if(choice.equalsIgnoreCase("o")){
                return st.PLACEORDER;
            }else if(choice.equalsIgnoreCase("s")){
                return st.SEARCHSTATE;
            }else{
                printToScreen("  This is not a valid choice.");
                return st.VIEWVSB;
            }
        }else{
            printToScreen("  Your basket is squeaky clean and shiny, but empty!");
            printToScreen("  Would you like to search for items to fill it up? Y/N");
            String in = getInput(1);
            State headerAct = headerActions(in);
            if(headerAct != null) {
            	return headerAct;
            }
            boolean fill = yesno(in);
            
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
    public State PLACEORDER()
    {
        if(VSB.isEmpty()){
            ORDERFAIL(0);
        }
        float total = 0;
        for(SearchItem i : VSB){
            total += i.getStock() * i.getPrice();
        }
        printToScreen("  The total price for your order is: $" + total);
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
        ArrayList<SearchItem> toRemove = new ArrayList<SearchItem>(); 
        //This is to prevent a concurrent modificatio nexception
        for(SearchItem i : VSB){
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
            for(SearchItem i : toRemove){
                VSB.remove(i);
            }
        }
        return st.PAYFORORDER;
    }
    
    /**
     * @author 
     * @throws ParseException 
     */
    public State PAYFORORDER() throws ParseException
    {
        boolean validCard = false;
        String cardnum = null;
        while(!validCard){
            printToScreen("  Please enter your credit card number.");
            cardnum = getInput(16);
            validCard = Pattern.matches("[0-9]{16}", cardnum);//ec.checkNumLength(cardnum, 16); //THIS IS A HACK!!!

            State headerAct = headerActions(cardnum);
            if(headerAct != null) {
            	return headerAct;
            };

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
            State headerAct = headerActions(cardDate);
            if(headerAct != null) {
            	return headerAct;
            }
            //Validate that the date is valid 
            validDate = Pattern.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}", cardDate);
            if(!validDate){
                printToScreen("  This is not a valid date.");
            }
        }
        //create a date based on the user's input
        String expectedPattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        Date _cardDate = formatter.parse(cardDate);
        
        //Validate credit card process via ensuring expiration date is after the present date
        boolean validated =  _cardDate.after(new Date());//Pattern.matches("[0-9]{16}", cardnum) &&
        if(validated){
        	order = new Order();
        	order.setCid(cust.getCID());
        	order.setExpiryDate(_cardDate);
        	order.setCardNum(Long.parseLong(cardnum));
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
        String in = getInput(1);
        boolean fconfirm = yesno(in);
        State headerAct = headerActions(in);
        if(headerAct != null) {
        	return headerAct;
        }
        if(fconfirm){
            return st.RECEIPT;
        }else{
            return ORDERFAIL(1);
        }
    }
    
    /**
     * @author Narbeh
     * TODO: 
     * @throws ParseException 
     * @throws SQLException 
     * @throws IOException 
     */
    public State RECEIPT() throws ParseException, IOException, SQLException
    {
        //Generate order receipt number
        //Generate receiptID and expected date
    	String datePattern = "yyyy-MM-dd";
    	SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
    	String orderDate = formatter.format(new Date());
    	Date _orderDate = formatter.parse(orderDate);//new SimpleDateFormat(datePattern).parse(orderDate);
    	
    	//expectedDeliveryDate
    	Calendar cal = Calendar.getInstance();
    	//make delivery date 7 days from the current time
    	cal.add(Calendar.DATE, 7);
    	String expectedDate = formatter.format(cal.getTime());
    	Date _expectedDate = formatter.parse(expectedDate);//new SimpleDateFormat(datePattern).parse(expectedDate);
    	
    	order.setDate(_orderDate);
    	order.setExpectedDate(_expectedDate);
    	
    	order.setDeliveredDate(_expectedDate);
    	
    	int receiptId = database.selectLatestPurchaseReceiptId() + 1;
    	order.setReceiptId(receiptId);
    	
    	//if insert is successful, didInsert should be 1, 0 otherwise
    	int didInsert = database.insertOrder(order);
    	
        //Attempt to add to the database, set boolean successful
        boolean successful = (didInsert > 0) ? true:false;
        if(successful){
        	//begin to fill the PurchaseItem table with the items from the basket
        	PurchaseItem p = new PurchaseItem();
        	for(int i = 0; i < VSB.size(); i++)
        	{
        		p.setReceiptId(receiptId);
        		p.setUPC(VSB.get(i).getUpc());
        		p.setQuantity(VSB.get(i).getStock());
        		database.insertPurchaseItem(p);
        	}
        	
            printToScreen("  Thank you for your order! Your order number is: " + receiptId);
            printToScreen("  Your order will arrive on approximately " + expectedDate );
            VSB.clear();
            orderItems.clear();
            searchedItems.clear();
            return st.CUSTSTART;
        }else{
            return ORDERFAIL(2);
        }
        //return null; //dummy return, unreachable
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
        	order = null;
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
    public  void printItems(ArrayList<SearchItem> items)
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
    private SearchItem searchByUPC(ArrayList<SearchItem> items, String upc)
    {
        for(SearchItem i : items){
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

    /**
     * @return prints the header displayed at the beginning of input states
     * @author Curtis
     */
	private void printHeader() {
		printToScreen("  --------------------------------------------------");
		printToScreen("  If you would like to log out, enter 'q' at any input.");
		printToScreen("  If you would like to exit, enter 'x' at any input.");
		printToScreen("  --------------------------------------------------");
	}
	
	
	/**
     * @return handles input for logging out and exiting
     * @author Curtis
     */
	private State headerActions(String in) {
	 if(in.equalsIgnoreCase("q")){
         return st.INITIAL;
     }
	 else if(in.equalsIgnoreCase("x")){
         return st.EXIT;
	 }
     else{
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
}