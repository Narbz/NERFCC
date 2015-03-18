
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
        this.upc = upc;
        songTitle = stitle;
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
    
    public void setSingerName(String newTitle)
    {
        songTitle = newTitle;
    }
}
