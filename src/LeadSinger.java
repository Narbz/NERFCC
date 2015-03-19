
/**
 * Class to represent the LeadSinger(upc, singerName)
 * @author Chazz Young
 * @version 0.0
 */
public class LeadSinger extends Record
{
    private String upc, singerName;
    
    public LeadSinger(String upc, String sname)
    {
        setUPC(upc);
        setSingerName(sname);
    }
    
    public String getUPC()
    {
        return upc;
    }
    
    public String getSingerName()
    {
        return singerName;
    }
    
    public void setUPC(String newUPC)
    {
        upc = newUPC;
    }
    
    public void setSingerName(String newSinger)
    {
        singerName = newSinger;
    }
}
