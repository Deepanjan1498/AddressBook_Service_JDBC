package org.bridegelabz.addressbookjdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressBookService {
public AddressBookJDBCService addressbookJDBCService;
private List<AddressBookData> addressBookDataList;
	
	public AddressBookService(){
		this.addressbookJDBCService = AddressBookJDBCService.getInstance();
	}
	
	public List<AddressBookData> readAddressBookData() throws AddressBookJDBCException{
		return this.addressbookJDBCService.readData();
	}
	public AddressBookService(List<AddressBookData> addressBookList) {
		// TODO Auto-generated constructor stub
		addressBookDataList =new ArrayList<>(addressBookList);
	}
	public void updateContactDetails(String name,String state,int zip) throws AddressBookJDBCException
	{
		int result=new AddressBookJDBCService().updateAddressBookDataUsingPreparedStatement(state,zip,name);
		if(result==0)
			return;
		AddressBookData addressBookData=this.getAddressBookData(name);
		if(addressBookData!=null) 
			{
			addressBookData.setZip(zip);
			addressBookData.setState(state);
			}
	}
	public List<AddressBookData> getAddressBookDataByStartDate(LocalDate startDate, LocalDate endDate)throws AddressBookJDBCException {
		return this.addressbookJDBCService.getAdressBookDataByStartingDate(startDate, endDate);
	}
	public AddressBookData getAddressBookData(String name) {
		return this.addressBookDataList.stream()
				.filter(addressBookDataListObject->addressBookDataListObject.getfirstName().equals(name))
				.findFirst().orElse(null);
	}

	public boolean checkAddressBookInSyncWithDB(String name) throws AddressBookJDBCException {
		List<AddressBookData> addressBookDataList=new AddressBookJDBCService().getAddressBookDataFromDB(name);
		return addressBookDataList.get(0).equals(getAddressBookData(name));
	}
	public long countEntries() {
		return addressBookDataList.size();
	}
	public void addContactToAddressBookUsingRestServices(AddressBookData addressBookData) {
		addressBookDataList.add(addressBookData);
	}
	public Map<String, Integer> readContactByCityOrState() throws AddressBookJDBCException {
		return this.addressbookJDBCService.getContactsByCityOrState();	
	}

}