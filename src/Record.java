
/**Class to extend every other class from.  Normally we would also include fields for the creator of the record, the date of when the record was created, and updated; however, for this setting we can ignore those(as we would have to extend every table to have these fields as well.) **/
public class Record{
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
