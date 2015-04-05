import org.apache.ibatis.session.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;


public class ams {
    // public static void main(String[] args)
        //         throws IOException,SQLException{
        //Bind the configuration file with a newly built sql session object
        // Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        // SqlSessionFactory smc =  new SqlSessionFactoryBuilder().build(rd);;
        // String test = "123456789123";
        // int t = 0;
        /*create a dummy item to insert*/
        //Item i = new Item("111122223333", "testItem", "CD", "testCat",
        //      "testComp", 1991, 10, 10);
        
        //Customer c = new Customer("HJones", "Harrold Jones", "azure", "1234 noname street, nowhereland", "6041478952");
        
        /*open s database session*/  
        //SqlSession session = smc.openSession();
        // t =  session.selectOne("ams.selectItemStock", test);
        /* This would insert one record in Item table. Adjust as you want to play around. First parameter is 
         * highlighting what sql statement we want(remember those special id's we defined).  ams is a namespace i defined 
         * in the xml. Second parameter is the object to give to myBatis*/
        // session.insert("ams.insertCustomer", c);
        
        //commit to the insertion
        //session.commit();
        //System.out.print(t);
        // session.close();
    //  }
    //************************INSERT STATEMENTS*******************************
    /**
    * IT F*CKING WORKS!!!!
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
    public void insertOrder(Order order) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        session.insert("ams.insertOrder", order);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
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
      
    //********************************SELECT STATEMENTS******************************
    
    
    
    //********************************END SELECT STATEMENTS*******************************
    
    //********************************UPDATE STATEMENTS************************************
    /**
     * @author Narbeh //INCOMPLETE
     */
    public void updateItemStock(String upc, int qtyToAdd) throws IOException, SQLException
    {
        //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        //Item item = session.select
        //session.update("ams.updateItemStock", upc, qtyToAdd);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    /**
     * @author Curtis //INCOMPLETE
     */
    public void updateOrderDate(Date date) throws IOException, SQLException
    {
  	  //HEADER
        Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
        SqlSessionFactory smc = new SqlSessionFactoryBuilder().build(rd);
        SqlSession session = smc.openSession();
        //END HEADER
        
        //QUERY TO EXECUTE
        //Order order = session.select
        //session.update("ams.updateOrderDate", receiptID, date);
        //FOOTER
        session.commit();
        session.close();
        //END FOOTER
    }
    
    
    //********************************END UPDATE STATEMENTS*********************************

    //********************************OTHER METHODS***************************************
    
}
