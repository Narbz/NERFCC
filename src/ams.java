import org.apache.ibatis.session.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;


public class ams {
	  public static void main(String[] args)
			   throws IOException,SQLException{
			   //Bind the configuration file with a newly built sql session object
		  	   Reader rd = Resources.getResourceAsReader("amsSqlMap/amsConfig.xml");
			   SqlSessionFactory smc =  new SqlSessionFactoryBuilder().build(rd);;

			   /*create a dummy item to insert*/
			   Item i = new Item("111122223333", "testItem", "CD", "testCat",
						"testComp", 1991, 10, 10);

			   /*open s database session*/	
			   SqlSession session = smc.openSession();
			   /* This would insert one record in Item table. Adjust as you want to play around. First parameter is highlighting what sql statement we want(remember those special id's we defined).  ams is a namespace i defined in the xml. Second parameter is the object to give to myBatis*/
			   session.insert("ams.insertItem", i);
			   
			   //commit to the insertion
			   session.commit();
			  }
}
