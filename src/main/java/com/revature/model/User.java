package com.revature.model;

public class User {

	private int Id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private int userStatus;
	
	
//	public int Id;
//	public String firstName;
//	public String lastName;
//	public String username;
//	public String password;
//	public int userStatus;

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	
	
	// for validation
	public void setPassword(String password) {
		this.password = password;
	}

	public User(int id, String firstName, String lastName, String username, String password) {
		super();
		Id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
	}

	public User(String firstName, String lastName, String username, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
	}

//	@Override
//	public String toString() {
//		return "Id#: " + Id + ", Username: " + username + ", First name: " + firstName + ", Last name: " + lastName
//				+ ", Password: " + password + "";
//	}
	
	@Override
	public String toString() {
		return "" + Id + " " + username + " " + firstName + " " + lastName+ "";
	}
	

	public User(int id, String firstName, String lastName, String username, String password, int userStatus) {
		super();
		Id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.userStatus = userStatus;
	}


	
	
	// new constructor for Jersey?
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User(int id, String username, String password) {
		super();
		Id = id;
		this.username = username;
		this.password = password;
	}

	public User() {
		super();
	}
	

	
	
}
