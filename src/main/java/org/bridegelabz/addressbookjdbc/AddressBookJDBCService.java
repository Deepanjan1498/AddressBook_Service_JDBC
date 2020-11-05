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
		List<AddressBookData> employeePayrollList = new ArrayList<>();
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
				employeePayrollList.add(new AddressBookData(first_name, last_name, address, city, state, zip, phone_number, email_id, addressbook_name, addressbook_type));
			}
		} catch (SQLException e) {
			throw new AddressBookJDBCException("Unable to retrieve data");
		}
		return employeePayrollList;
	}

}