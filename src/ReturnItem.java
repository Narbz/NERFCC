/**
 * Class to represent the ReturnItem(retid, upc, quantity) table
 * @author Narbeh Shahnazarian
 * @version 0.0
 */
public class ReturnItem extends Record {
	private int retid, quantity;
	private String upc;
	
	public int getRetid() {
		return retid;
	}
	public void setRetid(int retid) {
		this.retid = retid;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
}
