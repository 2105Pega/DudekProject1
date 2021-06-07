package com.revature.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//my imports
import com.revature.dao.AdminDAOImpl;
import com.revature.dao.UserDAOImpl;
//import com.revature.service.UserService;
import com.revature.model.Account;
import com.revature.model.User;

//Log4J Logger
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.glassfish.jersey.server.ResourceConfig;

@Path("/controller")
public class MyController {
	
//	@Provider
//	public void RestDemoJaxRsApplication(){
//		   register(CORSResponseFilter.class);
//		   //other registrations omitted for brevity
//		}
//	resourceConfig.getContainerResponseFilters().add(new CORSFilter());
//	final ResourceConfig resourceConfig = new ResourceConfig();
//	resourceConfig.add(new CORSFilter());

	//	final URI uri = ...;
//	final HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig);
	
	private static final Logger logger = LogManager.getLogger(MyController.class);
	
	public AdminDAOImpl aDao = new AdminDAOImpl();
	public UserDAOImpl uDao = new UserDAOImpl();
//	UserService userServ = new UserService();

//	@Path("/hello")
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String helloWorld() {
//		return "Hello!";
//	}
//	
//	@Path("{id}")
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public String getId(@PathParam("id") int id) {
//		return "Your ID: " + id;
//	}
	
//	@Path("/user")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public String getUser() {
//		
//		User2 u = new User2();
//		u.name = "Jacob";
//		u.age = 100;
//		u.email = "jacob@site.com";
//		
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//
//			return mapper.writeValueAsString(u);
//		} catch (JsonProcessingException e) {
//			
//			e.printStackTrace();
//			return "";
//		}
//	}
	
	@Path("/user/{name}/{pass}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserLogged(@PathParam("name") String name, @PathParam("pass") String pass) {
		
		
		User loggedUser = uDao.loginUser(name, pass);
		
		uDao.getListOfAccount(loggedUser);
//		getUserAccounts(loggedUser);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
//			getUserAccounts(loggedUser);
			return mapper.writeValueAsString(loggedUser);
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			return "";
		}
	}
	
	@Path("/user/{name}/{pass}/{userId}/acc")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserAccounts(@PathParam("name") String name, @PathParam("pass") String pass, @PathParam("userId") int id){
//		List<Account> list = new ArrayList<Account>();
		User user = new User(id, name, pass);
		
		List<Account> list = uDao.getListOfAccount(user);
		
//	    list.setAccountList(new ArrayList<Account>());
	     
//	    list.add(new Account(100, "Checking"));
//	    list.add(new Account(1250, "Savings"));
		
//	    list.getAccountList().add(new Account(2, "Alex Kolenchiskey"));
//	    list.getAccountList().add(new Account(3, "David Kameron"));
	     
	    return Response.ok().entity(list).cookie(new NewCookie("cookieResponse", "cookieValueInReturn")).build();
	}
	
	
	
//	@Path("/user_easy")
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	public void postUserEasy(User2 user) {
//		System.out.println("User name: " + user.name);
//	}
//	
//	@Path("/user_easy")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public User2 getUserEasy() {
//		User2 u = new User2();
//		u.name = "Jacob2";
//		u.age = 100;
//		u.email = "jacob@site.com";
//		
//		return u;
//	}
	
	@Path("/accounts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllAccounts(){

		
		List<Account> list = aDao.viewAccounts();

	     
	    return Response.ok().entity(list).cookie(new NewCookie("cookieResponse", "cookieValueInReturn")).build();
	}
	
	@Path("/usersandaccounts")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUsersAndAccounts(){

		
//		Map<User, Account> myMap = aDao.viewUsersAndAccounts();
//	    return Response.ok().entity(myMap).cookie(new NewCookie("Users and Accounts", "Success")).build();
	    
		
		ArrayList<String> myList = aDao.viewUsersAndAccounts();
	    return Response.ok().entity(myList).cookie(new NewCookie("Users and Accounts", "Success")).build();
	    
	    
	}
	
	@Path("/users")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllUsers(){
//		List<Account> list = new ArrayList<Account>();
		
		List<User> listU = aDao.viewUsers();
		
//	    list.setAccountList(new ArrayList<Account>());
	     
//	    list.add(new Account(100, "Checking"));
//	    list.add(new Account(1250, "Savings"));
		
//	    list.getAccountList().add(new Account(2, "Alex Kolenchiskey"));
//	    list.getAccountList().add(new Account(3, "David Kameron"));
	     
	    return Response.ok().entity(listU).cookie(new NewCookie("cookieResponse", "cookieValueInReturn")).build();
	}
	
	
	@Path("/users")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(User user){


	    boolean approval = uDao.createUser(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());
	    
	    return Response.ok()
	    		.entity(approval)
//				.header("Access-Control-Allow-Origin", "*")
//				.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
//				.allow("OPTIONS")
//	    		.cookie(new NewCookie("User registration submitted. Username", user.getUsername().toString()))
	    		.build();

	}
	
//	@Path("/users2")
//	@POST
//	@Consumes(MediaType.APPLICATION_XML)
//	@Produces(MediaType.APPLICATION_XML)
//	public Response registerUser( User u ) throws URISyntaxException 
//	{
//	    if(u == null){
//	        return Response.status(400).entity("Please add user details !!").build();
//	    }
//	     
//	    if(u.getFirstName() == null) {
//	        return Response.status(400).entity("Please provide the user name !!").build();
//	    }
//	    uDao.createUser(u.getFirstName(), u.getLastName(), u.getUsername(), u.getPassword());
//	    return Response.created(new URI("/api/controller/users2/"+u.getId())).build();
//	}
	
	@Path("/users/{id}/{accType}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response openAccount(@PathParam("id") int id, @PathParam("accType") String accType){


	    User tempUser = new User(id, "uname", "pass");
	    uDao.createAccount(tempUser, accType);
	    
	    return Response.ok().cookie(new NewCookie("Account created", accType)).build();

	}
//	/users/${id}/${accType}
	
	@Path("/users/{accId}")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAccount(@PathParam("accId") int id){

		User x = new User();

	    uDao.deleteAccount(x, id);
	    
	    return Response.ok().cookie(new NewCookie("Account deleted", "Success")).build();

	}
	
	
	@Path("/users/{money}/{accId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response depositAccount(@PathParam("money") double money, @PathParam("accId") int id){

		User x = new User();

	    uDao.depositAccount(x, money, id);
	    
		logger.error("Deposited " + money + "to " + id + ".");
	    
	    return Response.ok().cookie(new NewCookie("Account deposited", "Success")).build();

	}
	
	@Path("/users/acc/{money}/{accId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response withdrawAccount(@PathParam("money") double money, @PathParam("accId") int id){

		User x = new User();

	    uDao.withdrawAccount(x, money, id);
	    
		logger.info("Withdrawn " + money + "to " + id + ".");
	    
	    return Response.ok().cookie(new NewCookie("Account withdrawn", "Success")).build();

	}
	
	
	@Path("/users/transfer/{money}/{accFrom}/{accTo}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response transferFunds(@PathParam("money") double money, @PathParam("accFrom") int fromId, @PathParam("accTo") int toId){

//		User x = new User();

	    uDao.transferFunds(money, fromId, toId);
	    
//		logger.info("Withdrawn " + money + "to " + id + ".");
	    
	    return Response.ok().cookie(new NewCookie("Funds transferred", "Success")).build();

	}
	
	
	@Path("/users/joint/{username}/{accNum}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response openJointAccount(@PathParam("username") String username, @PathParam("accNum") int accNum){


	    User tempUser = new User(0, username, "pass");
	    uDao.openJointAccount(tempUser, accNum);
	    
	    return Response.ok().cookie(new NewCookie("Joint Account created", "Success")).build();

	}
	
	
	@Path("approve/{userId}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response approveUser(@PathParam("userId") int userId){

		

	    aDao.approveUser(userId);
	    
		logger.info("User approved! User ID: " + userId + ".");
	    
	    return Response.ok().cookie(new NewCookie("User approved", "Success")).build();

	}
	
	
	
}

