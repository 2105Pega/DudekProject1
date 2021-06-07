package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

//import com.revature.model.Account;
import com.revature.model.User;
import com.revature.util.ConnectionUtils;
import com.revature.model.Account;
import com.revature.model.Admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PSQLException;

public class AdminDAOImpl implements AdminDAO {

	private static final Logger logger = LogManager.getLogger(User.class);


	@Override
	public List<User> viewUsers() {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "select * from user_table";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			List<User> userList = new ArrayList<User>();

			while (result.next()) {
				User u = new User(result.getInt("user_id"), result.getString("user_fname"),
						result.getString("user_lname"), result.getString("user_username"),
						result.getString("user_password"), result.getInt("user_status"));

				userList.add(u);
			}
			System.out.println("\n");
			for (int i = 0; i < userList.size(); i++) {
				System.out.println(
						"---------------------------------------------------------------------------------------");
				System.out.println(userList.get(i));
			}
			System.out.println(
					"---------------------------------------------------------------------------------------\n");
			return userList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public boolean createUsers(String firstName, String lastName, String username, String password) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "insert into user_table(user_fname, user_lname, user_username, user_password, user_status)"
					+ "values(?, ?, ?, ?, 1)";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, username);
			statement.setString(4, password);

			statement.executeUpdate();
			System.out.println("\nUser has been added to the banking system!\n");
			return true;

		}

		catch (PSQLException e) {
			logger.warn(e);
			System.out.println("Username already exists!\n");
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<Account> viewAccounts() {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "select * from account_table";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			List<Account> accList = new ArrayList<Account>();

			while (result.next()) {
				Account a = new Account(result.getInt("account_id"), result.getDouble("account_balance"),
						result.getString("account_type"));

				accList.add(a);
			}
			System.out.println("\n");
//			for (int i = 0; i < accList.size(); i++) {
//				System.out.println("-----------------------------------------------------------");
//				System.out.println(accList.get(i));
//			}
//			System.out.println("-----------------------------------------------------------\n");

			return accList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean deleteUser(int id) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			// it will delete user and his/her accounts
			// if there were shared accounts, they will be still available to co-owners

			// foreign key constraint
			// 1) first delete the record with the foreign key (join_table)
			String sql = "delete from user_account_join_table where user_id = ?;\r\n" +
			// 2) delete accounts from account table if not present in join table
					"delete from account_table where account_id not in(select account_id from user_account_join_table);"
					// 3) then delete the record with the primary key (user_table)
					+ "delete from user_table where user_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setInt(1, id);
			statement.setInt(2, id);

			statement.executeUpdate();
			logger.warn("User number " + id + " has been deleted from the bank.");
			System.out.println("\nUser assigned to id #" + id + " has been deleted from the bank.\r\n"
					+ "All associated accounts have been deleted.\n");
			return true;

		}

		catch (PSQLException e) {
			e.printStackTrace();
			logger.warn(e);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean updateUser(User u) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql =

					"update user_table set user_fname = ?,\r\n" + "user_lname = ?,\r\n" + "user_username = ?,\r\n"
							+ "user_password = ?\r\n" + "where user_id = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, u.getFirstName());
			statement.setString(2, u.getLastName());
			statement.setString(3, u.getUsername());
			statement.setString(4, u.getPassword());
			statement.setInt(5, u.getId());

			statement.executeUpdate();

			System.out.println("\nUser information has been updated!\n");
			return true;

		}

		catch (PSQLException e) {
			logger.warn(e);
			System.out.println("Username already exists!\n");
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public ArrayList<String> viewUsersAndAccounts() {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "select * from user_table ut\r\n"
					+ "inner join user_account_join_table uajt on ut.user_id = uajt.user_id \r\n"
					+ "inner join account_table at2 on at2.account_id = uajt.account_id \r\n"
					+ "order by ut.user_username";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);
			Map<User, Account> myMap = new HashMap<User, Account>();
			
			ArrayList<String> arrayOfStrings = new ArrayList<String>();

			while (result.next()) {
				User u = new User(result.getInt("user_id"), result.getString("user_fname"),
						result.getString("user_lname"), result.getString("user_username"),
						result.getString("user_password"));
				Account a = new Account(result.getInt("account_id"), result.getDouble("account_balance"),
						result.getString("account_type"));

				myMap.put(u, a);

			}
																							//			Map<User, Account> myMap = new HashMap<User, Account>() {{
			Set entrySet = myMap.entrySet();

			Iterator it = entrySet.iterator();
																							//			System.out.println("\n");
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				System.out.println(
						"----------------------------------------------------------------------------------------------------------------------------");
				System.out.println(me.getKey() + " " + me.getValue());
				arrayOfStrings.add((me.getKey() + " " + me.getValue()));
			}
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------\n");

//			return myMap;
			return arrayOfStrings;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean approveUser(int id) {

		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql =

					"update user_table set user_status = 1 where user_id = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setInt(1, id);

			statement.executeUpdate();

			System.out.println("\nUser assigned to id #" + id + " has been approved!\n");
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	@Override
	public List<User> viewNewUsers() {
		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "select * from user_table where user_status = 0";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			List<User> userList = new ArrayList<User>();

			while (result.next()) {
				User u = new User(result.getInt("user_id"), result.getString("user_fname"),
						result.getString("user_lname"), result.getString("user_username"),
						result.getString("user_password"));

				userList.add(u);
			}
			System.out.println("\n");
			for (int i = 0; i < userList.size(); i++) {
				System.out.println(
						"---------------------------------------------------------------------------------------");
				System.out.println(userList.get(i));
			}
			System.out.println(
					"---------------------------------------------------------------------------------------\n");
			return userList;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean depositAccountsAdm(double money, int accountId) {
		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "Update account_table set account_balance = account_balance + ? where account_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setDouble(1, money);
			statement.setInt(2, accountId);

			statement.executeUpdate();

			logger.info("\n$%,.2f have been deposited into account #" + accountId + ".\n", money);
			System.out.printf("\n$%,.2f have been deposited into account #" + accountId + ".\n", money);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean withdrawAccountsAdm(double money, int accountId) {
		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "Update account_table set account_balance = account_balance - ? where account_id = ?";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setDouble(1, money);
			statement.setInt(2, accountId);

			statement.executeUpdate();
			logger.info("\n$%,.2f have been withdrawn from account #" + accountId + ".\n", money);
			System.out.printf("\n$%,.2f have been withdrawn from account #" + accountId + ".\n", money);
			return true;

		} catch (SQLException e) {
			logger.error(e);
			System.out.println("\n   " + ANSI_YELLOW_BACKGROUND + ANSI_BLACK + "OVERDRAFT PROTECTION!" + ANSI_RESET
					+ "  Requested amount exceeds your available funds. Please enter a lower amount.\r\n");
		}
		return false;
	}

	@Override
	public boolean transferFundsAdm(double money, int accountFrom, int accountTo) {
		try (Connection conn = ConnectionUtils.getConnection()) {

			String sql = "Update account_table set account_balance = account_balance - ? where account_id = ?;"+
					"Update account_table set account_balance = account_balance + ? where account_id = ?;";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setDouble(1, money);
			statement.setInt(2, accountFrom);
			statement.setDouble(3, money);
			statement.setInt(4, accountTo);

			statement.executeUpdate();
			logger.info("Amount of $" + money + " has been transferred from account #" + accountFrom + " to account #" + accountTo + ".");
			System.out.printf("\n$%,.2f have been transferred from account #" + accountFrom + " to account #" + accountTo + ".\n", money);
			return true;

		} catch (SQLException e) {
			logger.error(e);
			System.out.println("\n   " + ANSI_YELLOW_BACKGROUND + ANSI_BLACK + "OVERDRAFT PROTECTION!" + ANSI_RESET
					+ "  Requested amount exceeds your available funds. Please enter a lower amount.\r\n");
		}
		
		
		return false;
	}

	@Override
	public boolean cancelAccount(int accNum) {
		
		try (Connection conn = ConnectionUtils.getConnection()) {

			// will delete account completely even if this is joint account! 
			//(will delete more than 1 entry in join_table if there were 2 or more users)
			
			// as SQL query
			// foreign key constraint:
			 //1) first delete the record with the foreign key
			String sql = "delete from user_account_join_table where account_id = ?;\r\n"
					// 2) then delete the record with the primary key
					+ "delete from account_table where account_id = ?";

			// OR as SQL query with user-defined function
//			String sql = "create or replace function delete_user_account(accId integer)\r\n" + "returns void\r\n"
//					+ "language plpgsql\r\n" + "as \r\n"
//					+ "$$ --It is a way for plpgsql to know that we are about to define a function\r\n"
//					+ "declare --where we put our variable declarations to be used in the function\r\n"
//					+ "--	total integer;\r\n" + "	begin\r\n"
//					+ "delete from user_account_join_table where account_id = accId;\r\n"
//					+ "delete from account_table where account_id = accId;		\r\n" + "	end;\r\n" + "$$;"
//					+ "select delete_user_account(?)";

			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setInt(1, accNum);
			// this below is needed for regular SQL query
			statement.setInt(2, accNum);

			statement.executeUpdate();
			logger.info("Account #" + accNum + " has been cancelled!");
			System.out.println("\nAccount #" + accNum + " has been cancelled!");
			return true;

		} 
//		catch (PSQLException e) {
//			// this is only for SQL query with user-defined function
//			logger.warn(e);
//			logger.info("" + user.getUsername() + "'s account#" + accNumber + " has been deleted!");
//			System.out.println("\n" + ANSI_CYAN + user.getUsername() + ANSI_RESET + " your account #" + accNumber
//					+ " has been deleted!");
//		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
		
		
	}
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

}
