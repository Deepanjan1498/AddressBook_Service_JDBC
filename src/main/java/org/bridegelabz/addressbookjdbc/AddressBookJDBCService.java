package org.bridegelabz.addressbookjdbc;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookJDBCService {
	List<AddressBookData> addressBookData=null;
	private static AddressBookJDBCService addressBookJDBCService;
	public static AddressBookJDBCService getInstance() {
		if(addressBookJDBCService==null) {
			addressBookJDBCService=new AddressBookJDBCService();
		}
		return addressBookJDBCService;
	}
	public Connection getconnection() throws AddressBookJDBCException{
		String jdbcURL = "jdbc:mysql://localhost:3306/AddressBook_Service?useSSL=false";
		String user = "root";
		String password = "Deepa@43";
		Connection connect;
		System.out.println("Connecting to database: " + jdbcURL);
		try{
			connect = DriverManager.getConnection(jdbcURL, user, password);
			System.out.println("Connection is SuccessFull! " + connect);
			return connect;
		}
		catch(SQLException e)
		{
			throw new AddressBookJDBCException("Unable to establish the connection!");
		}
	}

	public List<AddressBookData> readData() throws AddressBookJDBCException{
		String sql = "SELECT * FROM addressbook;";
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection connect = this.getconnection()) {
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String firstName=resultSet.getString("firstName");
				String lastName=resultSet.getString("lastName");
				String address=resultSet.getString("address");
				String city=resultSet.getString("city");
				String state=resultSet.getString("state");
				int zip=resultSet.getInt("zip");
				long phoneNumber=resultSet.getLong("phoneNumber");
				String email=resultSet.getString("email");
				String addressbook_Name=resultSet.getString("addressbook_Name");
				String addressbook_Type=resultSet.getString("addressbook_Type");
				java.sql.Date date=resultSet.getDate("dateAdded");
				addressBookList.add(new AddressBookData(firstName, lastName, address, city, state, zip, phoneNumber, email, addressbook_Name, addressbook_Type,date));
			}
		} catch (SQLException e) {
			throw new AddressBookJDBCException("Unable to retrieve data");
		}
		return addressBookList;
	}
	public int updateAddressBookDataUsingPreparedStatement(String state,int zip,String name) throws AddressBookJDBCException 
	{
		String query = "UPDATE addressbook SET state = ? , zip = ? where firstName = ?";
		try(Connection connect = this.getconnection()) {
			PreparedStatement preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1, state);
			preparedStatement.setInt(2, zip);
			preparedStatement.setString(3, name);
			int result = preparedStatement.executeUpdate();
			addressBookData=getAddressBookDataFromDB(name);
			if(result > 0 &&  addressBookData!= null)
			{
				((AddressBookData) addressBookData).setState(state);
				((AddressBookData) addressBookData).setZip(zip);
			}	
		}catch (SQLException e) {
			throw new AddressBookJDBCException("Error occured");
		}
		return addressBookData.size();
	}
	public List<AddressBookData> getAddressBookDataFromDB(String name) throws AddressBookJDBCException{
		String sql = "SELECT * FROM addressbook WHERE firstName=?;";
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection connect = this.getconnection()) {
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String firstName=resultSet.getString("firstName");
				String lastName=resultSet.getString("lastName");
				String address=resultSet.getString("address");
				String city=resultSet.getString("city");
				String state=resultSet.getString("state");
				int zip=resultSet.getInt("zip");
				long phoneNumber=resultSet.getLong("phoneNumber");
				String email=resultSet.getString("email");
				String addressbook_Name=resultSet.getString("addressbook_Name");
				String addressbook_Type=resultSet.getString("addressbook_Type");
				java.sql.Date date=resultSet.getDate("dateAdded");
				addressBookList.add(new AddressBookData(firstName, lastName, address, city, state, zip, phoneNumber, email, addressbook_Name, addressbook_Type,date));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
	}
	private List<AddressBookData> getAddressBookListFromResultset(ResultSet resultSet) throws AddressBookJDBCException {
		List<AddressBookData> addressBookList = new ArrayList<AddressBookData>();
		try {
			while (resultSet.next()) {
				String firstName=resultSet.getString("firstName");
				String lastName=resultSet.getString("lastName");
				String address=resultSet.getString("address");
				String city=resultSet.getString("city");
				String state=resultSet.getString("state");
				int zip=resultSet.getInt("zip");
				long phoneNumber=resultSet.getLong("phoneNumber");
				String email=resultSet.getString("email");
				String addressbook_Name=resultSet.getString("addressbook_Name");
				String addressbook_Type=resultSet.getString("addressbook_Type");
				java.sql.Date date=resultSet.getDate("date_added");
				addressBookList.add(new AddressBookData(firstName, lastName, address, city, state, zip, phoneNumber, email, addressbook_Name, addressbook_Type,date));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
	}
		
	public List<AddressBookData> getAdressBookDataByStartingDate(LocalDate startDate, LocalDate endDate)
			throws AddressBookJDBCException {
		String sql = String.format("SELECT * FROM addressbook WHERE date_added BETWEEN cast('%s' as date) and cast('%s' as date);",
				startDate.toString(), endDate.toString());
		try (Connection connect = this.getconnection()) {
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getAddressBookListFromResultset(resultSet);
		} catch (SQLException e) {
			throw new AddressBookJDBCException("Could not connect!");
		}
	}
	public int countEntries() {
		// TODO Auto-generated method stub
		return addressBookData.size();
	}
	public Map<String, Integer> getContactsByCityOrState() throws AddressBookJDBCException {
		Map<String, Integer> contactByCityOrStateMap = new HashMap<>();
		ResultSet resultSet;
		String sqlCity = "SELECT city, count(firstName) as count FROM address_book GROUP BY city; ";
		String sqlState = "SELECT state, count(firstName) as count FROM address_book GROUP BY state; ";
		try (Connection connect = this.getconnection()) {
			Statement statement = connect.createStatement();
			resultSet = statement.executeQuery(sqlCity);
			while (resultSet.next()) {
				String city = resultSet.getString("city");
				Integer count = resultSet.getInt("count");
				contactByCityOrStateMap.put(city,count);
			}
			resultSet = statement.executeQuery(sqlState);
			while (resultSet.next()) {
				String state = resultSet.getString("state");
				Integer count = resultSet.getInt("count");
				contactByCityOrStateMap.put(state,count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactByCityOrStateMap;
	}
	public void addContact(String firstName, String lastName, String address, String city, String state, int zip,
			long phone, String email, String addressBookName,String addressBookType,Date dateAdded) throws AddressBookJDBCException {
		Connection connect = null;
		connect = this.getconnection();
		try {
			connect.setAutoCommit(false);
		} catch (SQLException e) {
			throw new AddressBookJDBCException("Unable to AutoCommit");
		}

		try(Statement statement = connect.createStatement())
		{	
			String sql = String.format(
					"INSERT INTO addressbook_sevice "
					+ "VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
					firstName, lastName, address, city, state, zip, phone, email, dateAdded);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			try {
				connect.rollback();
			} catch (SQLException e1) {
				throw new AddressBookJDBCException("Unable to Roll back");
			}
			throw new AddressBookJDBCException("Unable to get data.Please check table for updation");
		}

		try(Statement statement = connect.createStatement())
		 { String sql = String.format("INSERT INTO address_details_and_bookname " + 
					"VALUES ('%s', '%s')", email, addressBookName);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			try {
				connect.rollback();
			} catch (SQLException e1) {
				throw new AddressBookJDBCException("Unable to Roll Back in addressbook_Name");
			}
			throw new AddressBookJDBCException("Unable to retrieve data");
		} 
		try {
			connect.commit();
		} catch (SQLException e) {
			throw new AddressBookJDBCException("Unable to commit");
		} finally {
			if(connect!= null)
				try {
					connect.close();
				} catch (SQLException e) {
					throw new AddressBookJDBCException("Could not Retrieve data!");
				}
		}
		addressBookData.add(new AddressBookData(firstName, lastName, address, city, state, zip, phone, email, addressBookName, addressBookType,dateAdded));
	}
	public void addContactToDBWithThreads(List<AddressBookData> asList) {
		Map<Integer,Boolean> addressAdditionStatus=new HashMap<Integer,Boolean>();
		addressBookData.forEach(addressbookdata->
		{
			Runnable task=()->{
				addressAdditionStatus.put(addressbookdata.hashCode(),false);
				System.out.println("Contact Being Added:"+Thread.currentThread().getName());
				try {
					this.addContact(addressbookdata.getfirstName(),addressbookdata.getlastName(),addressbookdata.getAddress(),
							addressbookdata.getCity(),addressbookdata.getState(),addressbookdata.getZip(),addressbookdata.getphoneNumber(),
							addressbookdata.getemail(),addressbookdata.getaddressbook_Name(),addressbookdata.getaddressbook_Type(),addressbookdata.getDate());
				} 
				catch (AddressBookJDBCException e) {
					e.printStackTrace();
				}
				addressAdditionStatus.put(addressbookdata.hashCode(),true);
				System.out.println("Contact Added:"+Thread.currentThread().getName());
			};
			Thread thread=new Thread(task,addressbookdata.getfirstName());
			thread.start();
		});
		while(addressAdditionStatus.containsValue(false))
		{
			try {
				Thread.sleep(10);
			}
			catch(InterruptedException e) {}
		}
		System.out.println(this.addressBookData);
	}	
}