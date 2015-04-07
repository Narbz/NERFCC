import java.util.Date;


public class Order extends Record {
	private int receiptId;
	private Date date;
	private String cid;
	private long cardNum; 
	private Date expiryDate;
	private Date expectedDate;
	private Date deliveredDate;
	
	//Constructor - we will use this in the future to begin a insertion. i.e we first will create an Order object and use myBatis to insert this object
	public Order(int receiptId, Date date, String cid, long cardNum, Date expiryDate,
			Date expectedDate, Date deliveredDate) {
		super();
		this.receiptId = receiptId;
		this.date = date;
		this.cid = cid;
		this.cardNum = cardNum;
		this.expiryDate = expiryDate;
		this.expectedDate = expectedDate;
		this.deliveredDate = deliveredDate;
	}
	
	public Order()
	{
		//Default Constructor
	}
	
	/**Standard setters and getters **/
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
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Date getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}
	public Date getDeliveredDate() {
		return deliveredDate;
	}
	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public long getCardNum() {
		return cardNum;
	}

	public void setCardNum(long cardNum) {
		this.cardNum = cardNum;
	}
	
}
