package org.bridegelabz.addressbookjdbc;

import java.util.ArrayList;
import java.util.List;

public class AddressBookRestService {
	List<AddressBookData> addressBookDataList;
    public AddressBookRestService(List<AddressBookData> contactList) {
		addressBookDataList=new ArrayList<>(contactList);
	}
	public long countEntries() {
		return addressBookDataList.size();
	}
}
