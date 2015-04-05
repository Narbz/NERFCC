import java.util.Date;


public class Order extends Record {
	private int recieptId;
	private Date date;
	private String cid;
	private Date expriyDate;
	private Date expectedDate;
	private Date deliveredDate;
	
	//Constructor - we will use this in the future to begin a insertion. i.e we first will create an Order object and use myBatis to insert this object
	public Order(int recieptId, Date date, String cid, Date expriyDate,
			Date expectedDate, Date deliveredDate) {
		super();
		this.recieptId = recieptId;
		this.date = date;
		this.cid = cid;
		this.expriyDate = expriyDate;
		this.expectedDate = expectedDate;
		this.deliveredDate = deliveredDate;
	}
	
	public Order()
	{
		//Default Constructor
	}
	
	/**Standard setters and getters **/
	public int getRecieptId() {
		return recieptId;
	}
	public void setRecieptId(int recieptId) {
		this.recieptId = recieptId;
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
	public Date getExpriyDate() {
		return expriyDate;
	}
	public void setExpriyDate(Date expriyDate) {
		this.expriyDate = expriyDate;
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
	
}
