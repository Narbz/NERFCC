import java.sql.SQLException;
import java.text.ParseException;
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
    private State st;
    private StateHandler sh;

    /**
     * Constructor for objects of class Applet
     * @throws IOException 
     * @throws SQLException 
     * @throws ParseException 
     */
    public Applet() throws SQLException, IOException, ParseException
    {
        st = st.INITIAL;
        sh = new StateHandler();
        
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
     * @throws IOException 
     * @throws SQLException 
     * @throws ParseException 
     */
    public void start() throws SQLException, IOException, ParseException
    {
        
        boolean fin = false;
        //**MAIN LOOP***
        while(!fin){
            if(st == State.INITIAL){/**@author Chazz*/
                st = sh.INITIAL();
            }else if(st == State.LOGIN){
                st = sh.LOGIN();
            }else if(st == State.REGISTER){/**@author Narbeh */
                st = sh.REGISTER();
            }else if(st == State.CLERKSTART){/**@author Chazz*/
               st = sh.CLERKSTART();
            }else if(st == State.PROCESSRETURN){/**@author Chazz */
                st = sh.PROCESSRETURN();
            }else if(st == State.RETURNITEMS){/**@author Chazz*/
                st = sh.RETURNITEMS();
            }else if(st == State.RETURNCONFIRM){/**@author Chazz */
                st = sh.RETURNCONFIRM();
            }else if(st == State.MGRSTART){/**@author Chazz*/
                st = sh.MGRSTART();
            }else if(st == State.ADDITEMS){/**@author Narbeh*/
                st = sh.ADDITEMS();
            }else if(st == State.PROCDELIVERY){/**@author narbeh*/
                st = sh.PROCDELIVERY();
            }else if(st == State.DSRINIT){
                st = sh.DSRINIT();
            	//System.out.println("***********INCOMPLETE****************");
                //fin = true;
            }else if(st == State.DSRSUCCESS){
                System.out.println("***********INCOMPLETE****************");
                fin = true;
            }else if(st == State.DSRFAILURE){
                System.out.println("***********INCOMPLETE****************");
                fin = true;
            }else if(st == State.NTSRINIT){/**@author narbeh REVISION*/
                st = sh.NTSRINIT();
            }else if(st == State.NTSRSUCCESS){/**@author Chazz*/
                st = sh.NTSRSUCCESS();
            }else if(st == State.NTSRFAILURE){/**@author Chazz*/
                st = sh.NTSRFAILURE();
            }else if(st == State.CUSTSTART){/**@author Chazz*/
                st = sh.CUSTSTART();
            }else if(st == State.SEARCHSTATE){/**@author Curtis*/
                //no point in writign this without the search method
                st = sh.SEARCHSTATE();
            }else if(st == State.SELECTITEM){/**@author Curtis MERGED insf AND checkqty*/
               st = sh.SELECTITEM();
            }else if(st == State.VIEWVSB){/**@author Curtis*/
                st = sh.VIEWVSB();
            }else if(st == State.CLEARVSB){/**@author Farhoud*/
                st = sh.CLEARVSB();
            }else if(st == State.PLACEORDER){/**@author Curtis */
                st = sh.PLACEORDER();
            }else if(st == State.CHECKQTYFINAL){
                st = sh.CHECKQTYFINAL();
            }else if(st == State.PAYFORORDER){/**@author Curtis REVISIONS */
                st = sh.PAYFORORDER();
            }else if(st == State.ORDERFINAL){/**@author Curtis*/
                st = sh.ORDERFINAL();
            }else if(st == State.RECEIPT){
                st = sh.RECEIPT();
            }else if(st == State.ORDERFAIL){
                //tO BE COMPLETED LATER!!!
                st = sh.ORDERFAIL(1);
            }else if(st == State.EXIT){
                st = sh.EXIT();
                fin = true;
            }else{
                System.out.println("  Internal error, state not defined, now closing...");
                fin = true;
            }
        }
    }
    
    public static void main(String[] args) throws SQLException, IOException, ParseException{
        new Applet();
    }
}


