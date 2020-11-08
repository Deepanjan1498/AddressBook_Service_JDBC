
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
import org.bridegelabz.addressbookjdbc.AddressBookRestService;
import org.bridegelabz.addressbookjdbc.AddressBookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookJDBCTest {
	
	@Before
	public void setup()
	{
		RestAssured.baseURI="http://localhost";
		RestAssured.port=3000;
	}

	public AddressBookData[] getAddressBookContactList()
	{
		Response response=RestAssured.get("/Contacts");
		System.out.println("Address Book Contact Entries In Json Server:\n"+response.asString());
		AddressBookData[] contactsArray=new Gson().fromJson(response.asString(),AddressBookData[].class);
		return contactsArray;
	}
    @Test
    public void givenAddressBookInJsonServer_WhenCalled_ShouldMatchTheCount()
    {
        AddressBookData[] contactsArray=getAddressBookContactList();
        AddressBookRestService addressBookRestService;
        addressBookRestService=new AddressBookRestService(Arrays.asList(contactsArray));
        long entries=addressBookRestService.countEntries();
        Assert.assertEquals(3,entries);
    }
    @Test
    public void givenEmployeeWhenAddedShouldMatch201ResponseAndCount()
    {
    	AddressBookService addressBookService;
    	AddressBookData[] contactsArray=getAddressBookContactList();
    	addressBookService=new AddressBookService(Arrays.asList(contactsArray));
    	AddressBookData addressBookData=new AddressBookData("Sumit","Sharma","ghhee","Kolkata","West Bengal",235698,8965395,"dmfmf@gmail.com");
    	Response response=addContactsToJsonServer(addressBookData);
    	int HTTPstatusCode=response.getStatusCode();
    	Assert.assertEquals(201,HTTPstatusCode);
    	addressBookData=new Gson().fromJson(response.asString(),AddressBookData.class);
    	addressBookService.addContactToAddressBookUsingRestServices(addressBookData);
    	long entries=addressBookService.countEntries();
    	Assert.assertEquals(4,entries);
    }
    public Response addContactsToJsonServer(AddressBookData addressBookData) {
		String contacts=new Gson().toJson(addressBookData);
		RequestSpecification request=RestAssured.given();
		request.header("Content-Type","application/json");
		request.body(contacts);
		return request.post("/Contacts");
	}
	
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
