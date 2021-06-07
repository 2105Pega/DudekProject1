package com.revature.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.revature.model.Account;
import com.revature.model.Admin;
import com.revature.model.User;

public interface AdminDAO {

	public List<Account> viewAccounts();

	public List<User> viewUsers();

	public ArrayList<String> viewUsersAndAccounts();

	public boolean deleteUser(int id);

	public boolean updateUser(User user);

	public boolean createUsers(String firstName, String lastName, String username, String password);

	// additional methods:

	public boolean approveUser(int id);

	public List<User> viewNewUsers();
	
	//newer methods
	
	public boolean depositAccountsAdm(double money, int accountId);

	public boolean withdrawAccountsAdm(double money, int accountId);
	 
	public boolean transferFundsAdm(double money, int accountFrom, int accountTo);
	
	public boolean cancelAccount(int accNum);
	
}
