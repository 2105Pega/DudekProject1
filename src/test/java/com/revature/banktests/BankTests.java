package com.revature.banktests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.revature.dao.AdminDAOImpl;
import com.revature.dao.UserDAOImpl;
import com.revature.model.Account;
import com.revature.model.Admin;
import com.revature.model.User;
import com.revature.service.UserService;
import com.revature.util.ConnectionUtils;

class BankTests {
	
//	@Test
//	void failTest() {
//		fail("Not yet implemented");
//	}
	
	//tests for Bank Driver Class
	
	
	

	//tests for User Class
	@Test
	void userConstructor() {

		User user1 = new User("John", "Smith", "Johny52", "pass123");
		assertEquals("Johny52", user1.getUsername());
		assertEquals("John", user1.getFirstName());
		assertEquals("Smith", user1.getLastName());
		assertEquals("pass123", user1.getPassword());
	}
	
	
	//tests for Account Class
	@Test
	void accountConstructor() {

		Account account1 = new Account(500, "Checking");
		assertEquals(500, account1.getBalance());
		assertEquals("Checking", account1.getType());
	}
	
	
	// tests for Admin Class
	@Test
	void adminConstructor() {

		Admin admin1 = new Admin("Adam", "Jones", "Admin1", "pass");
		assertEquals("Adam", admin1.getFirstName());
		assertEquals("Jones", admin1.getLastName());
		assertEquals("Admin1", admin1.getUsername());
		assertEquals("pass", admin1.getPassword());
		
	}
	
	@Test
	void adminToString() {

		Admin admin1 = new Admin("Adam", "Jones", "Admin1", "pass");

		
		assertEquals("Admin [id=" + admin1.getId() + ", firstName=" + admin1.getFirstName() + ", lastName=" + admin1.getLastName() + ", username=" + admin1.getUsername()
				+ ", password=" + admin1.getPassword() + "]", admin1.toString());
	}
	
	
	// tests for uDao Impl Class
	
	@Test
	void logUser() {
		final UserDAOImpl uDao = new UserDAOImpl();

		User user2 = new User(60, "Shiri", "Smith", "Shiri123", "123");
		
		User loggedUser = uDao.loginUser(user2.getUsername(), user2.getPassword());

		assertEquals(loggedUser.toString(), user2.toString());
	}
	
	
	
	//tests for UserService
//	@Test
//	void showAllUsers() {
//		final AdminDAOImpl aDao = new AdminDAOImpl();
//		
//		UserService userServ = new UserService();
//
//
//
//		assertEquals(userServ.viewUsers(), aDao.viewUsers());
//	}
	
	
	//tests for Connection Utils
//	@Test
//	void connectionTest() throws SQLException {
//		String url = "jdbc:postgresql://sql-demo-database-1.cgelwtzldsxw.us-east-2.rds.amazonaws.com:5432/Sqldemo";
//		String username = "postgres";
//		String password = "1410Wilno";
//		
//		try {
//			ConnectionUtils.getConnection();
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//		}
//		
//		assertEquals(ConnectionUtils.getConnection(), DriverManager.getConnection(url, username, password));
//		
//		
//	}
	@Test
	public void connectionTest()
	{
		try {
			Connection connection = ConnectionUtils.getConnection();
			
			assertEquals(connection.isValid(0), true);
			assertEquals(connection.getSchema(), "public");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

	
	
	
	
	
}
