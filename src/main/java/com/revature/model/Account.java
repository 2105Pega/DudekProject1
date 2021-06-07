package com.revature.model;

public class Account {

	private int Id;
	private double balance;
	private String type;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	@Override
//	public String toString() {
//		return String.format("  " + type + " Account #" + Id + "     Balance: $%,.2f", balance);
//	}
	
	@Override
	public String toString() {
		return String.format("" + type + " " + Id + " $%,.2f", balance);
	}
	
	// $%,.2f

	public Account(int id, double balance, String type) {
		super();
		Id = id;
		this.balance = balance;
		this.type = type;

	}

	public Account(double balance, String type) {
		super();
		this.balance = balance;
		this.type = type;
	}

}
