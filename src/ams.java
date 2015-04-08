import org.apache.ibatis.session.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;


public class ams {

	
       //public static void main(String[] args)
       //        throws IOException,SQLException{
           //Bind the configuration file with a newly built sql session object
           //Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
           //SqlSessionFactory smc =  new SqlSessionFactoryBuilder().build(rd);
           //String test = "123456789123";
           //Customer c = null;
           //int exists = 0;
           /*create a dummy item to insert*/
           //Item i = new Item("111122223333", "testItem", "CD", "testCat",
            //      "testComp", 1991, 10, 10);

           //Customer c = new Customer("HJones", "Harrold Jones", "azure", "1234 noname street, nowhereland", "6041478952");
           
           /*open s database session*/  
          // SqlSession session = smc.openSession();
           //exists = session.selectOne("ams.selectReceiptToVerifyDate", '1');
         //  List<DailySalesReport> item_list = selectDailySalesReport("2015-03-09");
           //Map<String, Object> map = new HashMap<String, Object>();
           //map.put("date", "2015-03-09");
     	   //map.put("n", 1);
           // Map<String, String> map = new HashMap<String, String>();
     	 // map.put("deliveredDate", "2015-04-24");
     	 // map.put("receiptId", "5");
     	  //exists =  session.selectOne("ams.selectItemStock", map);
           //exists = session.selectOne("ams.selectCustomerExists", "Jooper");
           //item_list = session.selectList("ams.selectTopNItems", map);
           //item_list =  session.selectList("ams.selectItemByCategory", "pop");
           //c =  (Customer)session.selectOne("ams.selectCustomer", "Cooper");
           //i =  (Item)session.selectOne("ams.selectItemSearch", test);
           /* This would insert one record in Item table. Adjust as you want to play around. First parameter is highlighting what sql statement we want(remember those special id's we defined).  ams is a namespace i defined in the xml. Second parameter is the object to give to myBatis*/
           // session.insert("ams.insertCustomer", c);
           
           //commit to the insertion
           //session.commit();
           //System.out.print(selectRetunUpcExists("345678912345"));
    	   //updateItemStock("123456789123", "40");
    	   //session.close();
      //}
	
	/***************************************Select Statements **********************/
    
	   /**
       * Handling a single Record
       * **/
      public Customer selectCustsomerByCid(String cid) throws IOException, SQLException
      {
    	  //HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  Customer c = null;
    	  
    	  c =  (Customer)session.selectOne("ams.selectCustomer", cid);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return c;
    	     	  
      }
      
      public Item selectItemByUpc(String upc) throws IOException, SQLException
      {
    	  //HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  Item i = null;
    	  
    	  i =  (Item)session.selectOne("ams.selectItem", upc);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return i;
    	     	  
      }
      //Since we are using a 'like' in the actual sql, any title input by the user must have a '%' concatenated to the front and end of that string
      public List<Item> selectItemByTitle(String itemTitle) throws IOException, SQLException
      {
    	  //HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  List<Item> i = null;
    	  
    	  i =  session.selectList("ams.selectItemByTitle", itemTitle);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return i;
    	     	  
      }
      
      
      /**
       * Handling a list of records
       * **/
      public List<Item> selectItemByCategory(String category) throws IOException, SQLException
      {
    	  //HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  List<Item> item_list;
    	  
    	  item_list =  session.selectList("ams.selectItemByCategory", category);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return item_list;
      }
      
      /**
       * Handling a count(*)
       * **/
      public int verifyUserExists(String cid) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int userExists = 0;
    	  
    	  userExists =  session.selectOne("ams.selectCustomerExists", cid);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return userExists;
    	  
      }
      
      /**
       * Handling a count(*) with multiple parameters sent to the sql.  Always use a hashMap like below and enter the values to be passed into the xml as they appear in the query
       */
      public int verifyUserLogin(String cid, String pass) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int userExists = 0;
    	  Map<String, String> map = new HashMap<String, String>();
    	  map.put("cid", cid);
    	  map.put("password", pass);
    	  userExists =  session.selectOne("ams.selectCustomerLogin", map);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return userExists;
    	  
      }
      
      public int selectItemStock(String upc) throws IOException, SQLException
      {
    	  //HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int stock = 0;
    	  
    	  stock =  session.selectOne("ams.selectItemStock", upc);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return stock;
    	     	  
      }
      
      public int selectReceiptToVerifyDate(String receiptId) throws IOException, SQLException
      {
    	  //HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int isAcceptableDate = 0;
    	  
    	  isAcceptableDate =  session.selectOne("ams.selectReceiptToVerifyDate", receiptId);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return isAcceptableDate;
    	     	  
      }
      
      public int selectRetId(int receiptId) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int retid = 0;
    	  
    	  retid =  session.selectOne("ams.selectRetId", receiptId);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return retid;
      }
      
      public List<SearchItem> selectItemSearch(String category, String itemTitle, String singerName)  throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  List<SearchItem> item_list;
    	  Map<String, String> map = new HashMap<String, String>();
    	  map.put("category", category);
    	  map.put("itemTitle", itemTitle);
    	  map.put("singerName", singerName);
    	  item_list =  session.selectList("ams.selectItemSearch", map);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return item_list;
      }
      
      public List<SearchTopNitems> selectTopNItems(int n, String date)  throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
          List<SearchTopNitems> item_list;
    	  Map<String, Object> map = new HashMap<String, Object>();
    	  map.put("date", date);
    	  map.put("n", n);
    	  item_list =  session.selectList("ams.selectTopNItems", map);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return item_list;
      }
      
      public List<DailySalesReport> selectDailySalesReport(String date)  throws IOException, SQLException
      {
    	  float runningTotalForTheDay = 0;
    	  int totalQuantitySold = 0;
    	  //HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
          List<DailySalesReport> item_list;
    	  item_list =  session.selectList("ams.selectDailySalesReport", date);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          //Now we must sum the total sales from each record, and insert it as an item in the list before returning 
          for(int i = 0; i < item_list.size(); i++)
          {
        	  runningTotalForTheDay += item_list.get(i).getTotal();
        	  totalQuantitySold += item_list.get(i).getSold();
          }
          DailySalesReport s = new DailySalesReport();
          s.setTotal(runningTotalForTheDay);
          s.setSold(totalQuantitySold);
          //add to the end of the list
          item_list.add(s);
          return item_list;
      }
      
      public List<PurchaseItem> selectPurchases(int receiptId) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  List<PurchaseItem> purchase_list;
    	  
    	  purchase_list =  session.selectList("ams.selectPurchases", receiptId);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return purchase_list;
      }
      
      public List<ReturnItem> selectReturnItems(int retid) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  List<ReturnItem> return_list;
    	  
    	  return_list =  session.selectList("ams.selectReturnItems", retid);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return return_list;
      }
      
      public int selectLatestPurchaseReceiptId() throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int receiptid = 0;
    	  
    	  receiptid =  session.selectOne("ams.selectLatestPurchaseReceiptId", null);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return receiptid;
      }
      
      public int selectLatestReturnRetId() throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int retid = 0;
    	  
    	  retid =  session.selectOne("ams.selectLatestReturnRetId", null);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return retid;
      }
      
      public int selectLatestReturnReceiptId() throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int receiptId = 0;
    	  
    	  receiptId =  session.selectOne("ams.selectLatestReturnReceiptId", receiptId);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return receiptId;
      }
      
      public int selectOrderExists(int receiptId) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int exists = 0;
    	  
    	  exists =  session.selectOne("ams.selectOrderExists", receiptId);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return exists;
      }
      
      public Order selectOrder(int receiptId) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  Order o;
    	  
    	  o =  session.selectOne("ams.selectOrder", receiptId);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return o;
      }
      
      public static int selectRetunUpcExists(String upc) throws IOException, SQLException
      {
    	//HEADER
          Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
          SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
          SqlSession session = smc.openSession();
          //END HEADER
    	  int exists = 0;
    	  
    	  exists =  session.selectOne("ams.selectRetunUpcExists", upc);
    	  
    	  //FOOTER
          session.commit();
          session.close();
          //END FOOTER
          
          return exists;
      }
      
    //************************INSERT STATEMENTS*******************************
    /**
    * 
    * @author Chazz Young
    */
    public void insertCustomer(Customer toAdd) throws IOException, SQLException
    {
        
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertCustomer", toAdd);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    /**
     * @author Narbeh
     */
    public int insertOrder(Order order) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        int status;
        //QUERY TO EXECUTE
        status = session.insert("ams.insertOrder", order);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
        return status;
    }
    
    /**
     * @author Narbeh
     */
    public void insertItem(Item item) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertItem", item);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    /**
     * @author Narbeh
     */
    public void insertLeadSinger(LeadSinger leadSinger) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertLeadSinger", leadSinger);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    /**
     * @author Narbeh
     */
    public void insertHasSong(HasSong hasSong) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertHasSong", hasSong);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    /**
     * @author Narbeh
     */
    public void insertPurchaseItem(PurchaseItem purchase) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertPurchaseItem", purchase);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    /**
     * @author Narbeh
     */
    public void insertReturn(Return ret) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertReturn", ret);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    /**
     * @author Narbeh
     */
    public void insertReturnItem(ReturnItem returnItem) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertReturnItem", returnItem);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    //********************************END INSERT STATEMENTS*******************************

    
    //********************************UPDATE STATEMENTS************************************
    /**
     * @author Narbeh
     */
    public int updateItemStock(String upc, int qtyToAdd) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        //Item item = session.select
        Map<String, Object> map = new HashMap<String, Object>();
  	  	map.put("upc", upc);
  	  	map.put("stock", qtyToAdd);
        int success = session.update("ams.updateItemStock", map);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
        
        return success;
    }
    
    /**
     * @author Curtis 
     */
    public int updateOrderDate(String userDate, String receiptId) throws IOException, SQLException
    {
  	  //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        //Order order = session.select
        Map<String, String> map = new HashMap<String, String>();
  	  	map.put("deliveredDate", userDate);
  	  	map.put("receiptId", receiptId);
        int success = session.update("ams.updateOrderDate", map);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
        
        return success;
    }
    
    /**
     * @author Narbeh
     */
    public int updateItemStockAndPrice(String upc, String stock, double price) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        //Item item = session.select
        Map<String, Object> map = new HashMap<String, Object>();
  	  	map.put("stock", stock);
  	  	map.put("price", price);
  	  	map.put("upc", upc);
        int success = session.update("ams.updateItemStockAndPrice", map);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
        return success;
    }
    
    
    //********************************END UPDATE STATEMENTS*********************************

    //********************************OTHER METHODS***************************************
    
}
