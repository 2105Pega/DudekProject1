package com.revature.bankdriver;

//import com.revature.controllers.CORSResponseFilter;

//import com.revature.model.User;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Properties;
//import java.util.Scanner;
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Invocation;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import org.glassfish.jersey.client.ClientConfig;
//import com.revature.dao.AdminDAOImpl;
//
//import com.revature.controllers.MyController;
//import com.revature.model.Account;
//import com.revature.model.Admin;


import com.revature.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class BankDriver {
	
//	public static AdminDAOImpl aDao = new AdminDAOImpl();

	private static final Logger logger = LogManager.getLogger(BankDriver.class);

	public static void main(String[] args) {
		

		logger.info("Welcome to JDBC Bank © Copyright 2021");
		UserService userServ = new UserService();
//		Scanner sc = new Scanner(System.in);

//		System.out.println("\n   Welcome \r\n");

		
		// Main Menu
		userServ.showMainMenu();

//		User user1 = new User(1, "Peter", "Dudek", "Peter123", "123");
//		userServ.depositAccount(user1);
//		userServ.withdrawAccount(user1);
//		userServ.getListOfAccount(user1);
//		userServ.createUser();
//		userServ.deleteAccount(user1);
//		userServ.loginUser();		
//		userServ.createAccount(user1);
//		userServ.viewUsers();
//		userServ.viewAccounts();
//		userServ.deleteUser();
//		userServ.updateUser();
//		userServ.createUsers();
		
		
		
		
//	    Client client = ClientBuilder.newClient( new ClientConfig() );
//	    WebTarget webTarget = client.target("http://localhost:8080/JerseyJDBCBank/api/controller").path("accounts");
//	      
//	    Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
//	    Response response = invocationBuilder.get();
//	      
//	    Account account = response.readEntity(Account.class);
//	    List<Account> listOfAccounts = aDao.viewAccounts();
//	          
//	    System.out.println(response.getCookies());
//	    System.out.println(response.getStatus());
//	    System.out.println(Arrays.toString( listOfAccounts.toArray(new Account[listOfAccounts.size()]) ));
		
		

	}

}
