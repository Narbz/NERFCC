/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import java.util.ArrayList;
import java.util.Arrays;
/**
 * This class is used to handle all messages that are printed to the console
 * Please remember to use printWelcome as a template. Please note the two spaces
 * at the beginning. This is to separate the printed output from the input.
 * Also, remember to include your name in the @author tag
 * @author Chazz Young
 */
public class MessageHandler {
    
    public MessageHandler()
    {
        
    }
    
    /**
     * @author Chazz Youn
     */
    public static ArrayList<String> printWelcome()
    {
        ArrayList<String> welcome = new ArrayList<String>();
        welcome.add("  Welcome to the AMS Directory!");
        welcome.add("  Have you visited us before?");
        welcome.add("  Please type 'yes' of you have, or 'no' if you have not");
        return welcome;
    }
    
    /**
     * @author Chazz Youn
     */
    public static ArrayList<String> printExit()
    {
        ArrayList<String> exit = new ArrayList<String>();
        exit.add("  Thank you for visiting the AMS Directory!");
        exit.add("  Now closing...");
        return exit;
    }
}
