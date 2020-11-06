
package org.bridegelabz.addressbookservice;

import static org.junit.Assert.*;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bridegelabz.addressbookjdbc.AddressBookData;
import org.bridegelabz.addressbookjdbc.AddressBookJDBCException;
import org.bridegelabz.addressbookjdbc.AddressBookJDBCService;
import org.bridegelabz.addressbookjdbc.AddressBookService;
import org.junit.Assert;
import org.junit.Test;

public class AddressBookJDBCTest {
	
	@Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchAddressBookEntriesCount() throws AddressBookJDBCException
    {
    	List<AddressBookData> addressbookdata;
    	AddressBookService addressBookService = new AddressBookService();
		addressbookdata =addressBookService.readAddressBookData();
		Assert.assertEquals(3, addressbookdata.size());
    }
	@Test
    public void givenAddressBookInDB_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() throws AddressBookJDBCException
    {
    	List<AddressBookData> addressbookdata;
    	AddressBookService addressBookService = new AddressBookService();
		addressbookdata =addressBookService.readAddressBookData();
		addressBookService.updateContactDetails("Sumit","Bhagalpur",896542);
		boolean result=addressBookService.checkAddressBookInSyncWithDB("Sumit");
		Assert.assertTrue(result);
    }
	@Test
    public void givenAddressBookDataInDB_WhenRetrievedForGivenDate_ShouldSyncWithDB() throws AddressBookJDBCException
    {
    	List<AddressBookData> addressbookdata;
    	AddressBookService addressBookService = new AddressBookService();
		addressbookdata =addressBookService.readAddressBookData();
		LocalDate startDate = LocalDate.parse("2018-11-01");
		LocalDate endDate = LocalDate.parse("2019-11-04");
		List<AddressBookData> matchingRecords = addressBookService
				.getAddressBookDataByStartDate(startDate, endDate);
		Assert.assertEquals(matchingRecords.get(0),addressBookService.getAddressBookData("Kanishk"));
    }
	@Test
	public void givenContacts_RetrieveNumberOfContacts_ByCityOrState() throws AddressBookJDBCException {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBookData();
		Map<String, Integer> contactByCityOrStateMap = addressBookService.readContactByCityOrState();
		Assert.assertEquals(true, contactByCityOrStateMap.get("California").equals(5));
	}
	@Test
	public void givenNewContact_WhenAdded_ShouldSyncWithDB() throws AddressBookJDBCException {
		AddressBookJDBCService addressBookJDBCService = new AddressBookJDBCService();
		AddressBookService addressBookService = new AddressBookService();
		AddressBookJDBCService.addContact("Uttam", "Singh", "Karol Bagh", "Delhi", "Delhi", 400012,
				99087454, "asgsh45@gmail.com","addressBook1","Family",Date.valueOf("2020-11-01"));
		boolean result = addressBookService.checkAddressBookInSyncWithDB("Uttam");
		Assert.assertTrue(result);
	}
	    @Test
	    public void givenAddressBookMultipleData_ShouldAddToAdddressBook() throws AddressBookJDBCException{
	    	AddressBookData[] arrayOfDetails= {
	    			new AddressBookData("Tushar","Raj","Terwer","Patna","Bihar",
	    	    			45698,9835864,"swgwgg@gmail.com","addressbook1","Friends",Date.valueOf("2020-08-04")),
	    			new AddressBookData("Mrinal","wgwgwg","ojwogo","Patna","Bihar",
	    	    			6464,46464834,"eeneb@wggwgw.com","addressBook1","Friends",Date.valueOf("2020-11-04"))};
	    	AddressBookJDBCService addressBookJDBCService=new AddressBookJDBCService();
	    	addressBookJDBCService.readData();
	    	Instant startThread=Instant.now();
	    	addressBookJDBCService.addContactToDBWithThreads(Arrays.asList(arrayOfDetails));
	    	Instant endThread=Instant.now();
	    	System.out.println("Duration With Thread:"+java.time.Duration.between(startThread, endThread));
	    	Assert.assertEquals(10,addressBookJDBCService.countEntries());
	    }
}
