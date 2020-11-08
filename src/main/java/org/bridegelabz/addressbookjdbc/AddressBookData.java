package org.bridegelabz.addressbookjdbc;

import java.sql.Date;

public class AddressBookData {
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private int zip;
	private long phoneNumber;
	private String email;
	private String addressbook_Name;
	private String addressbook_Type;
	private Date date;

	public AddressBookData(String firstName, String lastName, String address, String city, String state, int zip,
			long phoneNumber, String email, String addressbook_Name, String addressbook_Type, Date date) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.addressbook_Name = addressbook_Name;
		this.addressbook_Type = addressbook_Type;
		this.date = date;
	}

	@Override
	public String toString() {
		return "AddressBookData [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address
				+ ", city=" + city + ", state=" + state + ", zip=" + zip + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + ", addressbook_Name=" + addressbook_Name + ", addressbook_Type="
				+ addressbook_Type + ", date=" + date + "]";
	}

	public String getfirstName() {
		return firstName;
	}

	public void setfirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getlastName() {
		return lastName;
	}

	public void setlastName(String lastName) {
		this.lastName = lastName;
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

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public long getphoneNumber() {
		return phoneNumber;
	}

	public void setphoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getemail() {
		return email;
	}

	public void setemail(String email) {
		this.email = email;
	}

	public String getaddressbook_Name() {
		return addressbook_Name;
	}

	public void setaddressbook_Name(String addressbook_Name) {
		this.addressbook_Name = addressbook_Name;
	}

	public String getaddressbook_Type() {
		return addressbook_Type;
	}

	public void setaddressbook_Type(String addressbook_Type) {
		this.addressbook_Type = addressbook_Type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public AddressBookData(String firstName, String lastName, String address, String city, String state, int zip,
			long phoneNumber, String email, String addressbook_Name, String addressbook_Type) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.addressbook_Name = addressbook_Name;
		this.addressbook_Type = addressbook_Type;
	}
	public AddressBookData(String firstName, String lastName, String address, String city, String state, int zip,
			long phoneNumber, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

}