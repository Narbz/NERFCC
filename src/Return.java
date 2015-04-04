/**
 * Class to represent the Return(retud, date, receiptId) table
 * @author Narbeh Shahnazarian
 * @version 0.0
 */
import java.util.Date;
public class Return extends Record {
	private int retid, receiptId;
	private Date date;
	
	public int getRetid() {
		return retid;
	}
	public void setRetid(int retid) {
		this.retid = retid;
	}
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
