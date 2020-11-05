package org.bridegelabz.addressbookjdbc;

import java.util.List;

public class AddressBookService {
public AddressBookJDBCService addressbookJDBCService;
	
	public AddressBookService(){
		this.addressbookJDBCService = new AddressBookJDBCService();
	}
	
	public List<AddressBookData> readAddressBookData() throws AddressBookJDBCException{
		return this.addressbookJDBCService.readData();
	}
}