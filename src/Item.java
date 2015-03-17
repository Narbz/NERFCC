
public class Item extends Record{
	private String upc;
	private String itemTitle;
	private String type;
	private String category;
	private String company;
	private int year;
	private float price;
	private int stock;

	//Constructor - we will use this in the future to begin a insertion. i.e we first will create an item object and use myBatis to insert this object
	public Item(String upc, String itemTitle, String type, String category,
			String company, int year, float price, int stock) {
		this.upc = upc;
		this.itemTitle = itemTitle;
		this.type = type;
		this.category = category;
		this.company = company;
		this.year = year;
		this.price = price;
		this.stock = stock;
	}
	/**Standard setters and getters **/
	public String getUpc() {
		return upc;
	}
	
	public void setUpc(String upc) {
		this.upc = upc;
	}
	
	public String getItemTitle() {
		return itemTitle;
	}
	
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}

	
}
