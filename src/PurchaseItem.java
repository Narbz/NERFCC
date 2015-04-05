
/**
 * Class to represent the PurchaseItem(receiptId,  upc, quantity) table
 * @author Chazz Young
 * @version 0.0
 */
public class PurchaseItem extends Record
{
    private int receiptID, quantity;
    private String upc;
    
    public PurchaseItem(int rid, int qty, String upc)
    {
        setReceiptID(rid);
        setQuantity(qty);
        setUPC(upc);
    }
    
    public PurchaseItem()
    {
    	//Default Constructor
    }
    
    public int getReceiptID()
    {
        return receiptID;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public String getUPC()
    {
        return upc;
    }
    
    public void setReceiptID(int rid)
    {
        receiptID = rid;
    }
    
    public void setQuantity(int qty)
    {
        quantity = qty;
    }
    
    public void setUPC(String upc)
    {
        this.upc = upc;
    }
}
