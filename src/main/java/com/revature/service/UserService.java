package com.revature.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//my import
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.revature.dao.UserDAOImpl;
//my import
import com.revature.model.Account;
import com.revature.model.Admin;
import com.revature.model.User;

//my import
import com.revature.dao.AdminDAOImpl;

public class UserService {

	private UserDAOImpl uDao = new UserDAOImpl();

	private AdminDAOImpl aDao = new AdminDAOImpl();

	boolean displayedAccounts = false;


	public void depositAccount(User user) {
		
		List<Account> accList = aDao.viewAccounts();
		
//		for (int i = 0; i < accList.size(); i++) {
//			System.out.println("-----------------------------------------------------------");
//			System.out.println(accList.get(i));
//		}
//		System.out.println("-----------------------------------------------------------\n");

		Scanner sc = new Scanner(System.in);
		System.out.println("Which account would you like to deposit to? (Enter Account #)");
		int accountId = sc.nextInt();
		
		for (int i = 0; i < accList.size(); i++) {
			
			if(accountId == accList.get(i).getId()) {
				System.out.println("Enter amount you would like to deposit ($)");
				double money = sc.nextDouble();
				
				while(money<=0) {
					System.out.println("Please enter a positive amount ($)");
					money = sc.nextDouble();
				}

				uDao.depositAccount(user, money, accountId);
				System.out.println("Would you like to make another deposit in this session? [Y/N]");
				String choice = sc.next();
				if (choice.equals("y") || choice.equals("Y")) {
					depositAccount(user);
				}
				showUserMenu(user);
			}
			
			
			
	}
		System.out.println("Invalid account number. Would you like to try again? [Y/N]");
		String selection = sc.next();
		if(selection.equals("y") ||selection.equals("Y")) {
		depositAccount(user);
		}
		else {
			showUserMenu(user);
		}
//		System.out.println("Enter amount you would like to deposit ($)");
//		double money = sc.nextDouble();
//		
//		while(money<=0) {
//			System.out.println("Please enter a positive amount ($)");
//			money = sc.nextDouble();
//		}
//
//		uDao.depositAccount(user, money, accountId);
//		System.out.println("Would you like to make another deposit in this session? [Y/N]");
//		String choice = sc.next();
//		if (choice.equals("y") || choice.equals("Y")) {
//			depositAccount(user);
//		}
//		showUserMenu(user);

	}

	public void createUser() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter your first name");
		String fName = sc.next();
		System.out.println("Enter your last name");
		String lName = sc.next();
		System.out.println("Enter your username");
		String username = sc.next();
		System.out.println("Enter your password");
		String password = sc.next();
		System.out.println("Re-enter your password");
		String passwordVal = sc.next();

		if (password.equals(passwordVal)) {
			uDao.createUser(fName, lName, username, password);
		} else {
			while (!password.equals(passwordVal)) {
				System.out.println("\nYour passwords don't match. Please try again\n");
				System.out.println("Enter your password");
				password = sc.next();
				System.out.println("Re-enter your password");
				passwordVal = sc.next();

			}
			uDao.createUser(fName, lName, username, password);
			// adding this feature - bring user back to register user menu
			showMainMenu();

		}

	}

	public void withdrawAccount(User user) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Which account would you like to withdraw from? (Enter Account #)");

		int accountId = sc.nextInt();
		System.out.println("Enter amount you would like to withdraw ($)");
		double money = sc.nextDouble();
		
		while(money<=0) {
			System.out.println("Please enter a positive amount ($)");
			money = sc.nextDouble();
		}

		uDao.withdrawAccount(user, money, accountId);
		System.out.println("Would you like to make another withdrawal in this session? [Y/N]");
		String choice = sc.next();
		if (choice.equals("y") || choice.equals("Y")) {
			withdrawAccount(user);
		}
		showUserMenu(user);

	}

	public void getListOfAccount(User user) {

		displayedAccounts = true;

		uDao.getListOfAccount(user);
		showUserMenu(user);

	}

	public void deleteAccount(User user) {

		Scanner sc = new Scanner(System.in);
		List<Account> accountList = uDao.getListOfAccount(user);

		System.out.println("Which account would you like to delete? (Enter Account #)");
		int accNumber = sc.nextInt();

		for (Account a : accountList) {
			if (accNumber == a.getId()) {
				Account tempAccount = new Account(a.getId(), a.getBalance(), a.getType());

				if (tempAccount.getBalance() == 0) {
					uDao.deleteAccount(user, accNumber);

					showUserMenu(user);
				}

				if (tempAccount.getBalance() > 0) {
					System.out.println(ANSI_BLACK + ANSI_YELLOW_BACKGROUND
							+ "\nYour account balance must be $0.00 in order to delete the account.\n" + ANSI_RESET);
					System.out.println("Do you still want to delete an account? [Y/N]");
					String choice = sc.next();
					if (choice.equals("y") || choice.equals("Y")) {
						this.deleteAccount(user);
					}
					showUserMenu(user);

				}

			}

		}

		System.out.println("Invalid entry. Enter correct account number.\n");
		this.deleteAccount(user);

	}

	public void loginUser() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter your username");
		String username = sc.next();
		System.out.println("Enter your password");
		String password = sc.next();

		String aName = "";
		String aPass = "";

		try {
			FileInputStream fileStream = new FileInputStream(
					"../JerseyJDBCBank/src/test/resources/DB_Properties.properties");

			Properties prop = new Properties();
			prop.load(fileStream);

			aName = prop.getProperty("Admin_uname");

			aPass = prop.getProperty("Admin_pass");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e2) {
			e2.printStackTrace();
		}

		catch (Exception e3) {
			e3.printStackTrace();
		}

		if (username.equals(aName) && password.equals(aPass)) {
			System.out.println("\nHello Admin!\n");
			showAdminMenu();
		}

		// This is important to understand
		else {
			User b = uDao.loginUser(username, password);
			if (b.getUserStatus() != 0) {
				showUserMenu(b);
			}
		}

	}

	public void createAccount(User user) {

		Scanner sc = new Scanner(System.in);
		System.out.println("What type of account would you like to open?");

		System.out.println("1. Checking\n2. Savings");
		System.out.println("Choose the account type (1 = Checking or 2 = Savings)");
		int accountType = sc.nextInt();

		if (accountType == 1) {
			uDao.createAccount(user, "Checking");
			showUserMenu(user);
		}

		else {
			uDao.createAccount(user, "Savings");
			showUserMenu(user);
		}

	}

	// My Admin methods
	public void viewUsers() {
		System.out.println("Bank users:");
		aDao.viewUsers();
		showAdminMenu();
	}

	public void viewAccounts() {
		System.out.println("Bank accounts:");
		List<Account> accList = aDao.viewAccounts();
		
		for (int i = 0; i < accList.size(); i++) {
			System.out.println("-----------------------------------------------------------");
			System.out.println(accList.get(i));
		}
		System.out.println("-----------------------------------------------------------\n");
//		aDao.viewAccounts();
		showAdminMenu();
	}

	public void deleteUser() {
		System.out.println("Bank users:");
		aDao.viewUsers();
		Scanner sc = new Scanner(System.in);
		System.out.println("Which user would you like to delete (enter user ID)?");

		int userId = sc.nextInt();

		aDao.deleteUser(userId);
		showAdminMenu();
	}

	public void updateUser() {
		int userId;
		String fName;
		String lName;
		String uName;
		String pass;

		System.out.println("Bank users:");
		aDao.viewUsers();

		Scanner sc = new Scanner(System.in);
		System.out.println("Which user would you like to update (enter user ID)?");
		userId = sc.nextInt();

		System.out.println("Enter updated username");
		uName = sc.next();

		System.out.println("Enter updated first name");
		fName = sc.next();

		System.out.println("Enter updated last name");
		lName = sc.next();

		System.out.println("Enter updated password");
		pass = sc.next();

		User curr = new User(userId, fName, lName, uName, pass);

		aDao.updateUser(curr);
		showAdminMenu();

	}

	public void createUsers() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter user first name");
		String fName = sc.next();
		System.out.println("Enter user last name");
		String lName = sc.next();
		System.out.println("Enter username");
		String username = sc.next();
		System.out.println("Enter user password");
		String password = sc.next();
		System.out.println("Re-enter user password");
		String passwordVal = sc.next();

		if (password.equals(passwordVal)) {
			aDao.createUsers(fName, lName, username, password);
			showAdminMenu();
		} else {
			// password validation
			while (!password.equals(passwordVal)) {
				System.out.println("\nYour passwords don't match. Please try again\n");
				System.out.println("Enter user password");
				password = sc.next();
				System.out.println("Re-enter user password");
				passwordVal = sc.next();
			}

			aDao.createUsers(fName, lName, username, password);
			showAdminMenu();
		}

		showAdminMenu();

	}

	// Main Menu
	public void showMainMenu() {
		Scanner sc = new Scanner(System.in);

		System.out.println(
				"\n   Welcome to " + ANSI_YELLOW + "JDBC " + ANSI_CYAN + "Bank" + ANSI_RESET + " ©Copyright 2021");
		System.out.println("   Java Developer Best Community Bank\n");

		System.out.println(ANSI_YELLOW + "   1. Login");
		System.out.println("   2. Register");
		System.out.println("   3. Exit\n" + ANSI_RESET);

		int option = sc.nextInt();

		if (option == 1) {
			loginUser();
		}

		if (option == 2) {
			createUser();
		}

		if (option == 3) {

			System.out.println("\n   " + ANSI_BLUE_BACKGROUND + "*  *  *  *  *  * " + ANSI_RESET + ""
					+ ANSI_RED_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + " *  *  *  *  *   " + ANSI_RESET + ""
					+ ANSI_WHITE_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + "*  *  *  *  *  * " + ANSI_RESET + ""
					+ ANSI_RED_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + " *  *  *  *  *   " + ANSI_RESET + ""
					+ ANSI_WHITE_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + "*  *  *  *  *  * " + ANSI_RESET + ""
					+ ANSI_RED_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + " *  *  *  *  *   " + ANSI_RESET + ""
					+ ANSI_WHITE_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + "*  *  *  *  *  * " + ANSI_RESET + ""
					+ ANSI_RED_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + " *  *  *  *  *   " + ANSI_RESET + ""
					+ ANSI_WHITE_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println("   " + ANSI_BLUE_BACKGROUND + "*  *  *  *  *  * " + ANSI_RESET + ""
					+ ANSI_RED_BACKGROUND + "                                  " + ANSI_RESET);
			System.out.println(
					"   " + ANSI_WHITE_BACKGROUND + "                                                   " + ANSI_RESET);
			System.out.println(
					"   " + ANSI_RED_BACKGROUND + "                                                   " + ANSI_RESET);
			System.out.println(
					"   " + ANSI_WHITE_BACKGROUND + "                                                   " + ANSI_RESET);
			System.out.println(
					"   " + ANSI_RED_BACKGROUND + "                                                   " + ANSI_RESET);
			System.out.println(
					"   " + ANSI_WHITE_BACKGROUND + "                                                   " + ANSI_RESET);
			System.out.println(
					"   " + ANSI_RED_BACKGROUND + "                                                   \n" + ANSI_RESET);

			System.out.println(ANSI_YELLOW + "\n   Thank you for using our services. See you next time!" + ANSI_RESET);
			System.out.println(
					"\n              " + ANSI_YELLOW + "JDBC " + ANSI_CYAN + "Bank" + ANSI_RESET + " ©Copyright 2021");
			System.exit(0);
		}

		showMainMenu();

	}

	// User Menu
	public void showUserMenu(User user) {
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
		Date date = new Date();

		if (displayedAccounts == false) {
			System.out.println("\nHi, " + ANSI_CYAN + user.getFirstName() + ANSI_RESET
					+ "!                                          " + ANSI_CYAN + formatter.format(date) + ANSI_RESET);
			System.out.println("Here's your Account Summary:");
			getListOfAccount(user);
		}
		displayedAccounts = false;

		Scanner sc = new Scanner(System.in);

		System.out.println(ANSI_YELLOW + "   1. View Account/s");
		System.out.println("   2. Deposit");
		System.out.println("   3. Withdraw");
		System.out.println("   4. Transfer");
		System.out.println("   5. Create New Account");
		System.out.println("   6. Joint Account");
		System.out.println("   7. Delete Account");
		System.out.println("   8. Logout\n" + ANSI_RESET);

		int option = sc.nextInt();

		switch (option) {
		case 1:
			getListOfAccount(user);
			break;
		case 2:
			depositAccount(user);
			break;
		case 3:
			withdrawAccount(user);
			break;
		case 4:
			transferFunds(user);
			break;
		case 5:
			createAccount(user);
			break;
		case 6:
			openJointAccount(user);
			break;
		case 7:
			deleteAccount(user);
			break;
		case 8:
			System.out.println("\nThank you for using our services. See you next time!\n");
			showMainMenu();
			break;

		default:
			System.out.println("Invalid entry. Please enter number from 1-6.");
			showUserMenu(user);

		}
		sc.close();

	}

	// Admin Menu
	public void showAdminMenu() {
		Scanner sc = new Scanner(System.in);

		System.out.println(ANSI_YELLOW + "   1. View Accounts");
		System.out.println("   2. View Users");
		System.out.println("   3. View Users and Accounts");
		System.out.println("   4. Approve User");
		System.out.println("   5. Create New User");
		System.out.println("   6. Update User");
		System.out.println("   7. Delete User");
		System.out.println("   8. Deposit");
		System.out.println("   9. Withdraw");
		System.out.println("   10. Transfer");
		System.out.println("   11. Cancel Account");
		System.out.println("   12. Logout\n" + ANSI_RESET);

		int option = sc.nextInt();

		switch (option) {
		case 1:
			viewAccounts();
			break;
		case 2:
			viewUsers();
			break;
		case 3:
			viewUsersAndAccounts();
			break;
		case 4:
			approveUser();
			break;
		case 5:
			createUsers();
			break;
		case 6:
			updateUser();
			break;
		case 7:
			deleteUser();
			break;
		case 8:
			depositAccountsAdm();
			break;
		case 9:
			withdrawAccountsAdm();
			break;
		case 10:
			transferFundsAdm();
			break;
		case 11:
			cancelAccount();
			break;
		case 12:
			System.out.println("\nThank you for using our services. See you next time!\n");
			showMainMenu();
			break;

		default:
			System.out.println("Invalid entry. Please enter number from 1-8.\n");
			showAdminMenu();

		}
		sc.close();

	}

	public void viewUsersAndAccounts() {
		System.out.println("Bank users and their accounts:\n");
		aDao.viewUsersAndAccounts();
		showAdminMenu();
	}

	public void approveUser() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Pending user approvals:");
		aDao.viewNewUsers();
		System.out.println("Which user would you like to approve? (Enter Id#)\n");
		int id = sc.nextInt();

		aDao.approveUser(id);

		showAdminMenu();
	}
	
	
	public void transferFunds(User user) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Which account would you like to transfer from? (Enter Account #)");
		int accountFrom = sc.nextInt();
		
		System.out.println("Which account would you like to transfer to? (Enter Account #)");
		int accountTo = sc.nextInt();
		
		
		System.out.println("Enter amount you would like to transfer ($)");
		double money = sc.nextDouble();
		
		while(money<=0) {
			System.out.println("Please enter a positive amount ($)");
			money = sc.nextDouble();
		}

		uDao.transferFunds(money, accountFrom, accountTo);
		System.out.println("Would you like to make another transfer in this session? [Y/N]");
		String choice = sc.next();
		if (choice.equals("y") || choice.equals("Y")) {
			transferFunds(user);
		}
		showUserMenu(user);
		sc.close();
	}
	
	public void depositAccountsAdm() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Which account would you like to deposit to? (Enter Account #)");

		int accountId = sc.nextInt();
		System.out.println("Enter amount you would like to deposit ($)");
		double money = sc.nextDouble();
		
		while(money<=0) {
			System.out.println("Please enter a positive amount ($)");
			money = sc.nextDouble();
		}

		aDao.depositAccountsAdm(money, accountId);
		System.out.println("Would you like to make another deposit in this session? [Y/N]");
		String choice = sc.next();
		if (choice.equals("y") || choice.equals("Y")) {
			depositAccountsAdm();
		}
		showAdminMenu();
	}
	
	public void withdrawAccountsAdm() {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Which account would you like to withdraw from? (Enter Account #)");

		int accountId = sc.nextInt();
		System.out.println("Enter amount you would like to withdraw ($)");
		double money = sc.nextDouble();
		
		while(money<=0) {
			System.out.println("Please enter a positive amount ($)");
			money = sc.nextDouble();
		}

		aDao.withdrawAccountsAdm(money, accountId);
		System.out.println("Would you like to make another withdrawal in this session? [Y/N]");
		String choice = sc.next();
		if (choice.equals("y") || choice.equals("Y")) {
			withdrawAccountsAdm();
		}
		showAdminMenu();
		
	}
	
	public void transferFundsAdm() {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Which account would you like to transfer from? (Enter Account #)");
		int accountFrom = sc.nextInt();
		
		System.out.println("Which account would you like to transfer to? (Enter Account #)");
		int accountTo = sc.nextInt();
		
		
		System.out.println("Enter amount you would like to transfer ($)");
		double money = sc.nextDouble();
		
		while(money<=0) {
			System.out.println("Please enter a positive amount ($)");
			money = sc.nextDouble();
		}

		aDao.transferFundsAdm(money, accountFrom, accountTo);
		System.out.println("Would you like to make another transfer in this session? [Y/N]");
		String choice = sc.next();
		if (choice.equals("y") || choice.equals("Y")) {
			transferFundsAdm();
		}
		showAdminMenu();
		sc.close();
		
	}
	
	public void cancelAccount() {
		Scanner sc = new Scanner(System.in);
		List<Account> accountList = aDao.viewAccounts();

		System.out.println("Which account would you like to cancel? (Enter Account #)");
		int accNumber = sc.nextInt();

		for (Account a : accountList) {
			if (accNumber == a.getId()) {
				Account tempAccount = new Account(a.getId(), a.getBalance(), a.getType());

				if (tempAccount.getBalance() == 0) {
					aDao.cancelAccount(accNumber);

					showAdminMenu();
				}

				if (tempAccount.getBalance() > 0) {
					System.out.println(ANSI_BLACK + ANSI_YELLOW_BACKGROUND
							+ "\n This account balance is more than $0.00. Would you like to proceed to cancel this account? [Y/N]\n" + ANSI_RESET);
//					System.out.println("Do you still want to delete an account? [Y/N]");
					String choice = sc.next();
					if (choice.equals("y") || choice.equals("Y")) {
						aDao.cancelAccount(accNumber);

						showAdminMenu();
					}
					showAdminMenu();

				}

			}

		}

		System.out.println("Invalid entry. Enter correct account number.\n");
		cancelAccount();
	}
	
	public void openJointAccount(User user) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Which account would you like to use as a joint account?");
		int accNumber = sc.nextInt();
		System.out.println("Please enter the following information of a person you would like to share this account with.");
		System.out.println("First name");
		String fName = sc.next();
		System.out.println("Last name");
		String lName = sc.next();
		System.out.println("Username");
		String uName = sc.next();
		
		User jointUser = new User(fName, lName, uName, "pass_placeholder"); 
		
		uDao.openJointAccount(jointUser, accNumber);
		showUserMenu(user);
		
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
