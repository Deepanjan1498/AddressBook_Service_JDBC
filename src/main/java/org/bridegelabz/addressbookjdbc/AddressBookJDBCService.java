package org.bridegelabz.addressbookjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookJDBCService {
	List<AddressBookData> addressBookDataObj=null;
	private static AddressBookJDBCService addressBookJDBCService;
	public static AddressBookJDBCService getInstance() {
		if(addressBookJDBCService==null) {
			addressBookJDBCService=new AddressBookJDBCService();
		}
		return addressBookJDBCService;
	}
	public Connection getConnection() throws AddressBookJDBCException{
		String jdbcURL = "jdbc:mysql://localhost:3306/AddressBook_Service?useSSL=false";
		String user = "root";
		String password = "Deepa@43";
		Connection connection;
		System.out.println("Connecting to database: " + jdbcURL);
		try{
			connection = DriverManager.getConnection(jdbcURL, user, password);
			System.out.println("Connection is SuccessFull! " + connection);
			return connection;
		}
		catch(SQLException e)
		{
			throw new AddressBookJDBCException("Unable to establish the connection");
		}
	}

	public List<AddressBookData> readData() throws AddressBookJDBCException{
		String sql = "SELECT * FROM addressbook;";
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String first_name=resultSet.getString("first_name");
				String last_name=resultSet.getString("last_name");
				String address=resultSet.getString("address");
				String city=resultSet.getString("city");
				String state=resultSet.getString("state");
				int zip=resultSet.getInt("zip");
				long phone_number=resultSet.getLong("phone_number");
				String email_id=resultSet.getString("email_id");
				String addressbook_name=resultSet.getString("addressbook_name");
				String addressbook_type=resultSet.getString("addressbook_type");
				addressBookList.add(new AddressBookData(first_name, last_name, address, city, state, zip, phone_number, email_id, addressbook_name, addressbook_type));
			}
		} catch (SQLException e) {
			throw new AddressBookJDBCException("Unable to retrieve data");
		}
		return addressBookList;
	}
	public int updateAddressBookDataUsingPreparedStatement(String state,int zip,String name) throws AddressBookJDBCException 
	{
		String query = "update addressbook set state = ? , zip = ? where first_name = ?";
		try(Connection con = this.getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, state);
			preparedStatement.setInt(2, zip);
			preparedStatement.setString(3, name);
			int result = preparedStatement.executeUpdate();
			addressBookDataObj=getAddressBookDataFromDB(name);
			if(result > 0 &&  addressBookDataObj!= null)
			{
				((AddressBookData) addressBookDataObj).setState(state);
				((AddressBookData) addressBookDataObj).setZip(zip);
			}	
		}catch (SQLException e) {
			throw new AddressBookJDBCException("Error in updation");
		}
		return addressBookDataObj.size();
	}
	public List<AddressBookData> getAddressBookDataFromDB(String name) throws AddressBookJDBCException{
		String sql = "select * from addressbook where first_name=?;";
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String first_name=resultSet.getString("first_name");
				String last_name=resultSet.getString("last_name");
				String address=resultSet.getString("address");
				String city=resultSet.getString("city");
				String state=resultSet.getString("state");
				int zip=resultSet.getInt("zip");
				long phone_number=resultSet.getLong("phone_number");
				String email_id=resultSet.getString("email_id");
				String addressbook_name=resultSet.getString("addressbook_name");
				String addressbook_type=resultSet.getString("addressbook_type");
				addressBookList.add(new AddressBookData(first_name, last_name, address, city, state, zip, phone_number, email_id, addressbook_name, addressbook_type));
			}
		} catch (SQLException e) {
			throw new AddressBookJDBCException("Unable to get data.Please check table for updation");
		}
		return addressBookList;
	}
}