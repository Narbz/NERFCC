
public class DailySalesReport {
	private String upc;
	private String category;
	private float price;
	private int Sold;
	private float  Total;
	
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getSold() {
		return Sold;
	}
	public void setSold(int sold) {
		Sold = sold;
	}
	public float getTotal() {
		return Total;
	}
	public void setTotal(float total) {
		Total = total;
	} 
}
