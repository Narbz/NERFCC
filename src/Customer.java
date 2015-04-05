
/**
 * Class to represent the Customer(cid, password, customerName, address, phone) table
 * @author Chazz Young
 * @version 0.0
 */
public class Customer
{
    private String cid, customerName, password, address, phone;

    public Customer(String cid, String customerName, String password, String address, String phone)
    {
        setCID(cid);
        setCustomerName(customerName);
        setPassword(password);
        setAddress(address);
        setPhone(phone);
    }
    public Customer()
    {
    	//default constructor
    }
    public String getCID()
    {
        return cid;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setCID(String newID)
    {
        cid = newID;
    }
    
    public void setCustomerName(String newName)
    {
        customerName = newName;
    }
    
    public void setPassword(String newPass)
    {
        password = newPass;
    }
    
    public void setAddress(String newAddress)
    {
        address = newAddress;
    }
    
    public void setPhone(String newPhone)
    {
        phone = newPhone;
    }
}
