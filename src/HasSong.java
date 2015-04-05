
/**
 * Class to represent the hasSong(upc, songTitle) table
 * @author Chazz Young
 * @version 0.0
 */
public class HasSong extends Record
{
    private String upc, songTitle;
    
    public HasSong(String upc, String stitle)
    {
        setUPC(upc);
        setSongTitle(stitle);
    }
    
    public HasSong()
    {
    	//default constructor
    }
    
    public String getUPC()
    {
        return upc;
    }
    
    public String getSongTitle()
    {
        return songTitle;
    }
    
    public void setUPC(String newUPC)
    {
        upc = newUPC;
    }
    
    public void setSongTitle(String newTitle)
    {
        songTitle = newTitle;
    }
}
