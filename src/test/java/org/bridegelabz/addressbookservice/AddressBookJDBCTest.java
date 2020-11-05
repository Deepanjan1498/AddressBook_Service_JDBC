








package org.bridegelabz.addressbookservice;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.bridegelabz.addressbookjdbc.AddressBookData;
import org.bridegelabz.addressbookjdbc.AddressBookJDBCException;
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
}
