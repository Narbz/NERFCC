
public class SearchTopNitems extends Item {
	private String upc;
	private String Title;
	private String category;
	private int stock;
	private int Sold;
	
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getSold() {
		return Sold;
	}
	public void setSold(int sold) {
		Sold = sold;
	} 
}
