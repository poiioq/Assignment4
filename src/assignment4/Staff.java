package assignment4;

public class Staff {
	private int id;
	private String lastName;
	private String firstName;
	private String mi;
	private String address;
	private String city;
	private String state;
	private String telephone;

	public Staff() {
	};

	public Staff(int id, String lastName, String firstName, String mi, String address, String city, String state,
			String telephone) {
		setId(id);
		setLastName(lastName);
		setFirstName(firstName);
		setMi(mi);
		setAddress(address);
		setCity(city);
		setState(state);
		setTelephone(telephone);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMi() {
		return mi;
	}

	public void setMi(String mi) {
		this.mi = mi;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
