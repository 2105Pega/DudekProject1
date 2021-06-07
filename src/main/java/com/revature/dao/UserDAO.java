package com.revature.dao;

import java.util.List;

import com.revature.model.Account;
import com.revature.model.User;

public interface UserDAO {

	public List<Account> getListOfAccount(User user);

	public boolean createAccount(User user, String accountType);

	public boolean deleteAccount(User user, int accNum);

	public boolean depositAccount(User user, double money, int accountId);

	public boolean withdrawAccount(User user, double money, int accountId);

	public User loginUser(String username, String password);

	public boolean createUser(String firstName, String lastName, String username, String password);

	// additional methods

	public boolean showAllAssets(int userId);
	
	public boolean transferFunds(double money, int accountFrom, int accountTo);
	
	public boolean openJointAccount(User user, int accNum);
	
}
