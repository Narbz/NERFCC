
/**
 * Class to represent the PurchaseItem(receiptId,  upc, quantity) table
 * @author Chazz Young
 * @version 0.0
 */
public class PurchaseItem extends Record
{
    private int receiptId, quantity;
    private String upc;
    
    public PurchaseItem(int rid, int qty, String upc)
    {
        setReceiptId(rid);
        setQuantity(qty);
        setUPC(upc);
    }
    
    public PurchaseItem()
    {
    	//Default Constructor
    }
    
    public int getReceiptId()
    {
        return receiptId;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public String getUPC()
    {
        return upc;
    }
    
    public void setReceiptId(int rid)
    {
        receiptId = rid;
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
